package in.bitanxen.app.controller;

import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.workflow.CreateWorkflowDTO;
import in.bitanxen.app.dto.workflow.WorkflowDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.service.ActionService;
import in.bitanxen.app.service.CaseStatusService;
import in.bitanxen.app.service.WorkflowFieldTemplateService;
import in.bitanxen.app.service.WorkflowService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final CaseStatusService caseStatusService;
    private final ActionService actionService;
    private final WorkflowFieldTemplateService workflowFieldTemplateService;

    public WorkflowController(WorkflowService workflowService, CaseStatusService caseStatusService, ActionService actionService,
                              WorkflowFieldTemplateService workflowFieldTemplateService) {
        this.workflowService = workflowService;
        this.caseStatusService = caseStatusService;
        this.actionService = actionService;
        this.workflowFieldTemplateService = workflowFieldTemplateService;
    }

    @GetMapping("/")
    public Flux<WorkflowDTO> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }

    @GetMapping("/id/{workflowId}")
    public Mono<WorkflowDTO> getWorkflow(@PathVariable String workflowId) {
        return workflowService.getWorkflowDetails(workflowId);
    }

    @GetMapping("/id/{workflowId}/casestatus")
    public Flux<CaseStatusDTO> getWorkflowCaseStatus(@PathVariable String workflowId) {
        return caseStatusService.getWorkflowCaseStatus(workflowId);
    }

    @GetMapping("/id/{workflowId}/action")
    public Flux<ActionDTO> getWorkflowActions(@PathVariable String workflowId) {
        return actionService.getWorkflowActions(workflowId);
    }

    @GetMapping("/id/{workflowId}/field")
    public Flux<WorkflowFieldDTO> getWorkflowFields(@PathVariable String workflowId) {
        return workflowFieldTemplateService.getWorkflowFields(workflowId);
    }

    @PostMapping("/")
    public Mono<WorkflowDTO> createWorkflow(@RequestBody Mono<CreateWorkflowDTO> createWorkflow) {
        return workflowService.createWorkflow(createWorkflow, CommonUtil.getUser());
    }


}
