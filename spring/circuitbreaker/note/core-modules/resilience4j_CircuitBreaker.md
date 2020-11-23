## Introduction
서킷브레이커는 3가지 보통 상태와 2가지 특별 상태를 가진 finite state machine 이다.
* 3가지 보통 상태: CLOSED(정상), OPEN(차단), HALF_OPEN(절반 차단)
* 2가지 특별 상태: DISABLED(모든 호출 허용), FORCED_OPEN(모든 호출 거절)

서킷브레이커는 sliding window 를 이용해 호출의 결과를 저장하고, 계산한다.
count-based sliding window, time-based sliding window 가 있어서 선택할 수 있다.
* count-based sliding window: 지난 N 번 호출의 결과를 모은다.
* time-based sliding window: 지난 N 초간의 호출의 결과를 모은다.

아래부턴 count-window, time-window 라고 줄여서 말한다.

## Count-based sliding window
count-window 는 N 개 원소를 가진 circular array 로 구현되어 있다.
만약 count-window 크기가 10이라면, circular array 의 원소는 10개이다.

window 의 동작방식은 새로운 호출 결과가 생길때마다 total aggregation 을 갱신한다.
즉 크기가 10이라면 지난 10번의 total aggregation(실패 횟수)를 계산하고 있다가, 새로운 결과가 들어오면 가장 예전 기록을 지우고 새로운 결과를
total aggregation 에 계산한다. (Subtract-on-Evict)

미리 계산하고 있으므로 스냅샷을 가져오는 시간은 O(1) 이고 메모리는 O(n) 이다.

## Time-based sliding window
time-window 는 N 개 partial aggregations 의 circular array 로 구현되어 있다.
만약 time-window 크기가 10초라면, circular array 는 10 개의 partial aggregations(bucket) 을 갖는다.
모든 bucket 은 정확히 1초동안의 결과를 계산하고 이를 partial aggregation 이라 한다.
circular array 의 head bucket 에는 현재 1초의 결과를 갖고, 다른 bucket 들에는 이전의 결과를 갖고 있다.

window 는 개별 호출 결과를 저장하지 않고, partial aggregations 와 total aggregation 을 갱신한다.
새로운 결과가 기록되면 total aggregation 이 갱신되는데, 가장 오래된 bucket 은 제거되고, 새로운 결과로 대체된다. (Subtract-on-Evict)

미리 계산하고 있으므로 스냅샷을 가져오는 시간은 O(1) 이고 메모리는 O(n) 이다.
공간은 N 개의 Partial aggregations 와 1 개의 total aggregation 만 생성된다.

partial aggregation 은 3개의 integer 와 1개의 long 타입으로 구성된다.
* 3개 integer 타입: 실패 호출 수, 슬로우 호출 수, 전체 호출 수
* 1개 long 타입: 전체 호출의 total duration

## Failure rate and slow call rate thresholds
실패율이 설정된 threshold 이상이면 서킷브레이커 상태가 CLOSED 에서 OPEN 으로 바뀐다.
기본으론 모든 예외가 실패로 기록되고, 설정을 통해 실패로 볼 예외 리스트를 정의할 수 있다. 
이 경우 다른 예외들은 성공이거나 무시처리할 수 있다. 무시되는건 성공, 실패 둘다 기록되지 않는다.

지연률이 설정된 threshold 이상이면 서킷브레이커 상태가 CLOSED 에서 OPEN 으로 바뀐다.
예를 들면, 5초 이상 걸린 호출들이 50% 이상인 경우 OPEN 으로 바뀐다. 이렇게 하면 외부 시스템 부하를 줄이는데 도움이 된다.

실패율, 지연률 모두 최소 호출수를 충족해야 계산된다. 만약 최소 호출수가 10이라면 9번까지 아무리 실패를 했더라도 서킷브레이커가 OPEN 으로 바뀌진 않는다.

서킷브레이커가 OPEN 이 되면 호출이 거절되며 `CallNotPermittedException` 이 발생한다. 얼마 시간이 지나면 OPEN 에서 HALF_OPEN 으로 바뀐다.
그러면 설정된 호출 수만 허용이 되고 허용된 호출이 모두 완료될때까지 다른 호출들은 거절된다.
실패율, 지연률이 threshold 이상이면 다시 OPEN 으로 바뀌고, threshold 이하이면 CLOSED 로 바뀐다.

서킷브레이커는 2개의 특별 상태 DISABLED(모든 호출 허용), FORCED_OPEN(모든 호출 거절)이 있다.
이 두가지 상태안에선 서킷브레이커 이벤트는 생성되지 않고, metrics 도 기록되지 않는다.
이 상태에서 나가는 방법은 서킷브레이커 state transition 또는 reset 오직 2가지 뿐이다.

서킷브레이커는 thread-safe 이다.
* 서킷브레이커의 상태는 AtomicReference 이 저장되어 있다.
* 서킷브레이커는 상태 갱신에 atomic operation 을 사용하고 이는 사이드이펙트가 없다.
* sliding window 로 부터 기록을 저장하고 스냅샷을 읽는 것은 동기화된다.  

이 의미는 원자성이 보장되고, 한 순간에 오직 한 스레드만 상태, Sliding window 를 갱신한다는 말이다.
하지만 서킷브레이커는 함수 호출을 동기화하지 않는다. 이 말은 함수 호출 자체는 critical section 부분이 아니라는 의미이다.
그렇지 않으면 성능 패널티 이슈가 발생할 것이고, 이는 전체 성능 감소가 될 것이기 때문이다.

서킷브레이커가 CLOSED 일때 20개의 동시 스레드가 함수 호출을 하기위해 퍼미션을 요청한다면, 모든 스레드들은 함수 호출을 허용받는다.
sliding window 크기가 15 라고 하더라도 말이다. sliding window 크기만큼 동시에 함수 호출을 허용한다는 말이 아니기 때문이다.
만약 동시에 호출할 스레드 수를 제한하고 싶으면 Bulkhead 를 사용해야한다.

## Create a CircuitBreakerRegistry
Resilience4j 는 in-memory `CircuitBreakerRegistry`를 제공한다.
이것은 ConcurrentHashMap 을 base 로 하여, thread safety 와 원자성 보장을 한다.

서킷브레이커레지스트리는 서킷브레이커 인스턴스를 매니지하는데 사용하고, 이 레지스트리는 글로벌 기본 설정 `CircuitBreakerConfig` 으로
생성할 수 있다.

```java
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
```

## Create and configure a CircuitBreaker
https://resilience4j.readme.io/docs/circuitbreaker#create-and-configure-a-circuitbreaker


## Decorate and execute a functional interface

## Consume emitted RegistryEvents

## Consume emitted CircuitBreakerEvents

## Override the RegistryStore
