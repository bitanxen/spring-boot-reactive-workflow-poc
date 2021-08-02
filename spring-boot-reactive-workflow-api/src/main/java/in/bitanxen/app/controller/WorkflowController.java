package in.bitanxen.app.controller;

import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.caseworkflow.CaseDTO;
import in.bitanxen.app.dto.workflow.CreateWorkflowDTO;
import in.bitanxen.app.dto.workflow.WorkflowDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.service.*;
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
    private final CaseService caseService;

    private final WorkflowFieldTemplateService workflowFieldTemplateService;

    public WorkflowController(WorkflowService workflowService, CaseStatusService caseStatusService, ActionService actionService,
                              CaseService caseService, WorkflowFieldTemplateService workflowFieldTemplateService) {
        this.workflowService = workflowService;
        this.caseStatusService = caseStatusService;
        this.actionService = actionService;
        this.caseService = caseService;
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

    @GetMapping("/id/{workflowId}/case")
    public Flux<CaseDTO> getWorkflowCases(@PathVariable String workflowId, @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size) {
        return caseService.getWorkflowCases(workflowId, page, size);
    }

    @GetMapping("/id/{workflowId}/field/create")
    public Flux<WorkflowFieldDTO> getWorkflowCaseFields(@PathVariable String workflowId) {
        return workflowFieldTemplateService.getWorkflowCaseFields(workflowId);
    }

    @PostMapping("/")
    public Mono<WorkflowDTO> createWorkflow(@RequestBody Mono<CreateWorkflowDTO> createWorkflow) {
        return workflowService.createWorkflow(createWorkflow, CommonUtil.getUser());
    }

}
