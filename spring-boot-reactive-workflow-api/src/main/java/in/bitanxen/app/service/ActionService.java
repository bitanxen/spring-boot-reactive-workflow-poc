package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.CreateActionDTO;
import in.bitanxen.app.dto.action.field.CreateActionFieldDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.model.Action;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionService {
    Mono<ActionDTO> getActionDetails(String actionId);
    Mono<Action> getAction(String actionId);

    Mono<ActionDTO> createAction(CreateActionDTO createAction, User user);
    Flux<ActionDTO> getWorkflowActions(String workflowId);

    Mono<ActionDTO> createActionField(CreateActionFieldDTO createActionField, User user);
}
