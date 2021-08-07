package in.bitanxen.app;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;

@Configuration
public class StartUp {

    @Bean
    ApplicationListener<ApplicationReadyEvent> client(RSocketRequester rSocketRequester) {
        return applicationReadyEvent -> rSocketRequester.route("workflow.case.realtime")
                .data("hello")
                .retrieveFlux(String.class)
                .log()
                .subscribe(System.out::println);
    }
}
