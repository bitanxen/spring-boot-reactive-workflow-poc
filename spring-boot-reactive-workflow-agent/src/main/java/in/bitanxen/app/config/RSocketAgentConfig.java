package in.bitanxen.app.config;

import in.bitanxen.app.service.WorkflowClientService;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeType;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Configuration
@Slf4j
public class RSocketAgentConfig {

    private final WorkflowClientService workflowClientService;

    public RSocketAgentConfig(@Lazy WorkflowClientService workflowClientService) {
        this.workflowClientService = workflowClientService;
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        Hooks.onErrorDropped(System.out::println);
        Resume resume = new Resume()
                .sessionDuration(Duration.ofMinutes(15))
                .retry(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                        .doBeforeRetry(s -> System.out.println("Resume: Before Retry : "+s))
                        .doAfterRetry(s -> System.out.println("Resume: After Retry : "+s))
                );

        RSocketRequester rSocketRequester = builder
                //.metadataMimeType(MimeType.valueOf("application/vnd.spring.rsocket.metadata+json"))
                .dataMimeType(MimeType.valueOf("application/json"))
                .rsocketConnector(rSocketConnector -> {
                    rSocketConnector.reconnect(Retry.backoff(100, Duration.ofSeconds(5))
                            .doBeforeRetry(s -> System.out.println("Reconnecting..."))
                            .doAfterRetry(s -> System.out.println("Tried to established connection"))
                    )
                            .interceptors(interceptorRegistry -> interceptorRegistry.forConnection(new CustomDuplexConnectionInterceptor()))

                    ;
                })
                //.websocket(getUri());
                .connectWebSocket(getUri())
                .subscribeOn(Schedulers.boundedElastic())
                .block();
        return rSocketRequester;
    }

    public URI getUri() {
        System.out.println("Connecting...");
        return URI.create("ws://localhost:7060/api/workflow/realtime");
        //ws://localhost:7060/api/workflow/realtime
        //ws://localhost:9190/api/workflow/realtime
    }

    @Bean
    public WorkflowServiceRegistry workflowServiceRegistry(final List<AbstractWorkflowOperation> workflowOperations) {
        return new WorkflowServiceRegistry(workflowOperations);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> requestStream() {
        return workflowClientService::processStreamData;
    }
}
