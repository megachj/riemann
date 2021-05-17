package sunset.spring.reactor;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxTest {

    @Test
    public void 시퀀스_만들기() {
        // elements 로 만들기
        Flux<String> stream1 = Flux.just("Hello", "world");
        Flux<Integer> stream2 = Flux.fromArray(new Integer[]{1, 2, 3});
        Flux<Integer> stream3 = Flux.fromIterable(Arrays.asList(9, 8, 7));

        // range 로 만들기: 2010, 2010+1, ..., 2010+8
        Flux<Integer> stream4 = Flux.range(2010, 9);
    }
}
