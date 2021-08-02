package in.bitanxen.app.controller;

import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.CreateActionDTO;
import in.bitanxen.app.dto.action.field.CreateActionFieldDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.service.ActionService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/workflow/action")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/id/{actionId}")
    public Mono<ActionDTO> getAction(@PathVariable String actionId) {
        return actionService.getActionDetails(actionId);
    }

    @PostMapping("/")
    public Mono<ActionDTO> createAction(@RequestBody CreateActionDTO createAction) {
        return actionService.createAction(createAction, CommonUtil.getUser());
    }

    @PostMapping("/field")
    public Mono<ActionDTO> createActionField(@RequestBody CreateActionFieldDTO createActionField) {
        return actionService.createActionField(createActionField, CommonUtil.getUser());
    }

    @GetMapping("/id/{actionId}/field")
    public Flux<TemplateFieldDTO> getActionField(@PathVariable String actionId) {
        return actionService.getActionField(actionId);
    }
}
