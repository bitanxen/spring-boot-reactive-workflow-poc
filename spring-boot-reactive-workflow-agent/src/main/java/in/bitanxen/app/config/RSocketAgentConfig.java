package in.bitanxen.app.config;

import in.bitanxen.app.dto.CaseEventDTO;
import in.bitanxen.app.service.WorkflowClientService;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeType;
import reactor.core.publisher.Hooks;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Configuration
@Slf4j
public class RSocketAgentConfig {

    private final WorkflowClientService workflowClientService;

    public RSocketAgentConfig(@Lazy WorkflowClientService workflowClientService) {
        this.workflowClientService = workflowClientService;
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder, RSocketStrategies strategies) {
        Hooks.onErrorDropped(System.out::println);
        Resume resume = new Resume()
                .sessionDuration(Duration.ofMinutes(15))
                .retry(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                        .doBeforeRetry(s -> log.debug("Disconnected. Trying to resume...")));
        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

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
                    //rSocketConnector.resume(resume);
                    rSocketConnector.acceptor(responder);
                })
                .websocket(URI.create("ws://localhost:9190/api/workflow/realtime"));

        rSocketRequester
                .rsocketClient()
                .source()
                .onErrorStop()
                .flatMap(RSocket::onClose)
                .repeat()
                .retry()
                .subscribe();
        return rSocketRequester;
    }

    @Bean
    public WorkflowServiceRegistry workflowServiceRegistry(final List<AbstractWorkflowOperation> workflowOperations) {
        return new WorkflowServiceRegistry(workflowOperations);
    }

    /*
    @Bean
    ApplicationListener<ApplicationReadyEvent> requestStream() {
        return workflowClientService::processStreamData;
    }

     */
}
