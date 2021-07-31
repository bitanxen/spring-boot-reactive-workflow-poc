package in.bitanxen.app.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.List;

public class TestReactor {

    public static void main(String[] args) {
        //Mono<String> hello = Mono.just("");
        Mono<String> hello = Mono.just("Bitan");
        int i = 11;

        String bitan = Mono.just("Bitan")
                .zipWith(i > 10 ? hello : Mono.just(""))
                .map(objects -> objects.getT2() + " " + objects.getT1())
                .block();

        Flux<Object> map = Flux.just("1", "2", "5")
                .zipWith(Flux.just("3", "4"))
                .map(objects -> {
                    List<Object> list = objects.toList();

                    System.out.println(objects+": "+objects.getT1());
                    System.out.println(objects+": "+objects.getT2());

                    return list;
                });
    }
}
