package in.bitanxen.app.service;

import in.bitanxen.app.config.StaticApplicationContext;
import in.bitanxen.app.config.WorkflowRegistration;
import in.bitanxen.app.config.WorkflowRegistrationContextHolder;
import in.bitanxen.app.config.WorkflowServiceRegistry;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class RSocketClientOperation {

    private final RSocketRequester rSocketRequester;
    private final WorkflowServiceRegistry workflowServiceRegistry;

    public RSocketClientOperation(RSocketRequester rSocketRequester, WorkflowServiceRegistry workflowServiceRegistry) {
        this.rSocketRequester = rSocketRequester;
        this.workflowServiceRegistry = workflowServiceRegistry;
    }

    public String fireAndForget(String route, Object data) {
        return rSocketRequester
                .route(route)
                .data(data)
                .retrieveMono(String.class)
                .subscribeOn(Schedulers.boundedElastic())
                .block();
    }

    public List<WorkflowRegistration> getService() {
         return workflowServiceRegistry.getRegistry()
                .stream()
                .flatMap(abstractWorkflowOperation -> abstractWorkflowOperation.getWorkflows()
                        .stream()
                        .map(workFlowIds -> new WorkflowRegistration(workFlowIds, abstractWorkflowOperation)))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 1000)
    public void registerWorkflowOperation() {
        List<WorkflowRegistration> service = getService();
        System.out.println(service);
        WorkflowRegistrationContextHolder.setRegistrations(service);
    }
}
