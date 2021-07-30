package in.bitanxen.app.controller;

import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.CreateActionDTO;
import in.bitanxen.app.service.ActionService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
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
        return actionService.getAction(actionId);
    }

    @PostMapping("/")
    public Mono<ActionDTO> createAction(@RequestBody CreateActionDTO createAction) {
        return actionService.createAction(createAction, CommonUtil.getUser());
    }
}
