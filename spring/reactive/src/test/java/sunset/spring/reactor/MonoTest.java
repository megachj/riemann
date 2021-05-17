package sunset.spring.reactor;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoTest {

    @Test
    public void 시퀀스_만들기() {
        // element 로 만들기
        Mono<String> stream1 = Mono.just("One");
        Mono<String> stream2 = Mono.justOrEmpty(null);
        Mono<String> stream3 = Mono.justOrEmpty(Optional.empty());

        // 비동기 래핑: HTTP 요청, DB 쿼리 같은 작업시에 사용
        Mono<String> stream4 = Mono.fromCallable(() -> "http request.. response!");
        Mono<String> stream6 = Mono.fromSupplier(() -> "http request.. response!");
        Mono<String> stream5 = Mono.fromRunnable(() -> {log.info("Hello world");});
    }
}
