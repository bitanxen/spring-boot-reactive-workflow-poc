package in.bitanxen.app.config;

import in.bitanxen.app.service.WorkflowClientService;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.Resume;
import lombok.extern.slf4j.Slf4j;
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
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        ArgumentResolverConfigurer args = messageHandler.getArgumentResolverConfigurer();
        //args.addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder, RSocketStrategies rSocketStrategies) {
        Resume resume = new Resume()
                .sessionDuration(Duration.ofMinutes(15))
                .retry(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                        .doBeforeRetry(s -> System.out.println("Resume: Before Retry : "+s))
                        .doAfterRetry(s -> System.out.println("Resume: After Retry : "+s))
                );
        SocketAcceptor responder = RSocketMessageHandler.responder(rSocketStrategies, new ClientHandler());
        return builder
                //.metadataMimeType(MimeType.valueOf("application/vnd.spring.rsocket.metadata+json"))
                .dataMimeType(MimeType.valueOf("application/json"))
                .rsocketConnector(rSocketConnector -> {
                    rSocketConnector.reconnect(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(5))
                            .doBeforeRetry(s -> System.out.println("Reconnect : Disconnected. Trying to reconnect..."+s))
                            .doAfterRetry(s -> System.out.println("Reconnect : tried to reconnect..."+s))
                    ).interceptors(interceptorRegistry -> {
                        interceptorRegistry.forConnection(new CustomDuplexConnectionInterceptor());
                    });
                    //rSocketConnector.resume(resume);
                    //rSocketConnector.acceptor(responder);
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
