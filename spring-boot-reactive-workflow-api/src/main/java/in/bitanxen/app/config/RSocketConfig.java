package in.bitanxen.app.config;

import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
@Slf4j
public class RSocketConfig {

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        ArgumentResolverConfigurer args = messageHandler.getArgumentResolverConfigurer();
        //args.addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

    /*
    @Bean
    RSocketServerCustomizer rSocketResume() {
        Resume resume =
                new Resume()
                        .sessionDuration(Duration.ofMinutes(15))
                        .retry(
                                Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                                        .doBeforeRetry(s -> log.debug("Disconnected. Trying to resume...")));
        return rSocketServer -> rSocketServer.resume(resume);
    }

     */
}
