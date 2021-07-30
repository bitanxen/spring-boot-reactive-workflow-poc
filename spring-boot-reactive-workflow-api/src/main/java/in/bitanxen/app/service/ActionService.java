package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.CreateActionDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionService {
    Mono<ActionDTO> getAction(String actionId);

    Mono<ActionDTO> createAction(CreateActionDTO createAction, User user);
    Flux<ActionDTO> getWorkflowActions(String workflowId);
}
