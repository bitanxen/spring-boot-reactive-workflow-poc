package in.bitanxen.app.controller;

import in.bitanxen.app.dto.workflow.CreateWorkflowDTO;
import in.bitanxen.app.dto.workflow.WorkflowDTO;
import in.bitanxen.app.service.WorkflowService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/")
    public Flux<WorkflowDTO> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }

    @GetMapping("/{workflowId}")
    public Mono<WorkflowDTO> getWorkflow(@PathVariable String workflowId) {
        return workflowService.getWorkflow(workflowId);
    }

    @PostMapping("/")
    public Mono<WorkflowDTO> createWorkflow(@RequestBody Mono<CreateWorkflowDTO> createWorkflow) {
        return workflowService.createWorkflow(createWorkflow, CommonUtil.getUser());
    }
}
