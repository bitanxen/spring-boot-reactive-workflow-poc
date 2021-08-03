package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.event.WorkflowEventListener;
import in.bitanxen.app.model.Case;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WorkflowRealTimeServiceImpl implements WorkflowRealTimeService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Flux<WorkflowEvent> workflowEvents;

    public WorkflowRealTimeServiceImpl(ApplicationEventPublisher applicationEventPublisher, WorkflowEventListener workflowEventListener) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.workflowEvents = Flux.create(workflowEventListener).share();
    }

    @Override
    public Flux<WorkflowEvent> getRealtimeWorkflowEvent() {
        return this.workflowEvents;
    }

    @Override
    public Mono<Void> publishCasePreCreateEvent(Case caseDetails, User user) {
        return null;
    }

    @Override
    public Mono<Void> publishCasePostCreateEvent(Case caseDetails, User user) {
        return null;
    }

    public Mono<CaseEventDTO> convertInto(Case caseDetails) {
        return Mono.just(caseDetails)
                .map(aCase -> {
                    return new CaseEventDTO();
                });
    }
}
