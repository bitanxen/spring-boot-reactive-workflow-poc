package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.workflow.field.CreatedWorkflowFieldDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.model.WorkflowFieldTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkflowFieldTemplateService {
    Mono<WorkflowFieldDTO> getFieldTemplateDetails(String fieldTemplateId);
    Mono<WorkflowFieldTemplate> getFieldTemplate(String fieldTemplateId);

    Mono<WorkflowFieldDTO> createFieldTemplate(Mono<CreatedWorkflowFieldDTO> createdWorkflowField, User user);
    Mono<WorkflowFieldTemplate> createFieldTemplateDirect(Mono<CreatedWorkflowFieldDTO> createdWorkflowField, User user);

    Flux<WorkflowFieldDTO> getWorkflowFields(String workflowId);
}
