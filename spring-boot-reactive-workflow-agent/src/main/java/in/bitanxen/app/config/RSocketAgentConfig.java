package in.bitanxen.app.config;

import in.bitanxen.app.dto.CaseEventDTO;
import in.bitanxen.app.service.WorkflowClientService;
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
        Resume resume = new Resume()
                .sessionDuration(Duration.ofMinutes(15))
                .retry(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                        .doBeforeRetry(s -> log.debug("Disconnected. Trying to resume...")));
        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

        return builder
                //.metadataMimeType(MimeType.valueOf("application/vnd.spring.rsocket.metadata+json"))
                .dataMimeType(MimeType.valueOf("application/json"))
                .rsocketConnector(rSocketConnector -> {
                    rSocketConnector.reconnect(Retry.backoff(10, Duration.ofSeconds(20)));
                    rSocketConnector.resume(resume);
                    rSocketConnector.acceptor(responder);
                })
                .websocket(URI.create("ws://localhost:9190/api/workflow/realtime"));
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
