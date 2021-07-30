package in.bitanxen.app.controller;

import in.bitanxen.app.dto.workflow.field.CreatedWorkflowFieldDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.service.WorkflowFieldTemplateService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/workflow/field")
public class WorkflowFieldTemplateController {

    private final WorkflowFieldTemplateService workflowFieldTemplateService;

    public WorkflowFieldTemplateController(WorkflowFieldTemplateService workflowFieldTemplateService) {
        this.workflowFieldTemplateService = workflowFieldTemplateService;
    }

    @PostMapping("/")
    public Mono<WorkflowFieldDTO> createWorkflowTemplateField(@RequestBody Mono<CreatedWorkflowFieldDTO> createdWorkflowField) {
        return workflowFieldTemplateService.createFieldTemplate(createdWorkflowField, CommonUtil.getUser());
    }


}
