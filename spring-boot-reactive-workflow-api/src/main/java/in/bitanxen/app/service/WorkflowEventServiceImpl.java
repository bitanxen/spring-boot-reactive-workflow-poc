package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.model.Case;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WorkflowEventServiceImpl implements WorkflowEventService {

    @Override
    public Mono<Void> publishCasePreCreateEvent(Case caseDetails, User user) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> publishCasePostCreateEvent(Case caseDetails, User user) {
        return Mono.empty();
    }
}
