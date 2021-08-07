package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.model.Case;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkflowRealTimeService {
    Flux<WorkflowEvent> getRealtimeWorkflowEvent();
    Disposable publishCasePreCreateEvent(Case caseDetails, User user);
    Disposable publishCasePostCreateEvent(Case caseDetails, User user);
    Disposable publishCaseActionEvent(Case caseDetails, String actionId, User user);
}
