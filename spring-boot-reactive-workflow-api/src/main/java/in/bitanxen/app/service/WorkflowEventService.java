package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.model.Case;
import reactor.core.publisher.Mono;

public interface WorkflowEventService {
    Mono<Void> publishCasePreCreateEvent(Case caseDetails, User user);
    Mono<Void> publishCasePostCreateEvent(Case caseDetails, User user);
}
