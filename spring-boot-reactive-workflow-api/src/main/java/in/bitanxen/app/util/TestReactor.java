package in.bitanxen.app.util;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class TestReactor {

    public static void main(String[] args) {
        //Mono<String> hello = Mono.just("");
        Mono<String> hello = Mono.just("Bitan");
        int i = 11;

        String bitan = Mono.just("Bitan")
                .zipWith(i > 10 ? hello : Mono.just(""))
                .map(objects -> objects.getT2() + " " + objects.getT1())
                .block();
        System.out.println(bitan);
    }
}
