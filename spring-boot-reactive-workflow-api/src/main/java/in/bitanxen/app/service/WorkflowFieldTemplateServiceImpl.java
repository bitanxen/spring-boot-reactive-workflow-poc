package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.workflow.field.CreatedWorkflowFieldDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.exception.WorkflowException;
import in.bitanxen.app.model.WorkflowFieldTemplate;
import in.bitanxen.app.repository.WorkflowFieldTemplateRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WorkflowFieldTemplateServiceImpl implements WorkflowFieldTemplateService {

    private final WorkflowService workflowService;

    private final WorkflowFieldTemplateRepository workflowFieldTemplateRepository;

    public WorkflowFieldTemplateServiceImpl(WorkflowService workflowService, WorkflowFieldTemplateRepository workflowFieldTemplateRepository) {
        this.workflowService = workflowService;
        this.workflowFieldTemplateRepository = workflowFieldTemplateRepository;
    }

    @Override
    public Mono<WorkflowFieldDTO> getFieldTemplateDetails(String fieldTemplateId) {
        return getFieldTemplate(fieldTemplateId).flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<WorkflowFieldTemplate> getFieldTemplate(String fieldTemplateId) {
        return workflowFieldTemplateRepository.findById(fieldTemplateId);
    }

    @Override
    public Mono<WorkflowFieldDTO> createFieldTemplate(Mono<CreatedWorkflowFieldDTO> createdWorkflowField, User user) {
        return createdWorkflowField
                .flatMap(cwf -> workflowService.getWorkflow(cwf.getWorkflowId())
                        .map(workflow -> new WorkflowFieldTemplate(workflow.getId(), cwf.getFieldName(), cwf.getFieldDescription(), cwf.getFieldType(),
                                cwf.getFieldValidation(), user.getMemberId())))
                .doOnNext(wft -> workflowFieldTemplateRepository.findByWorkflowIdAndFieldName(wft.getWorkflowId(), wft.getFieldName())
                        .hasElement()
                        .then(Mono.error(new WorkflowException("Duplicate field in this workflow"))))
                .flatMap(workflowFieldTemplateRepository::save)
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Flux<WorkflowFieldDTO> getWorkflowFields(String workflowId) {
        return workflowFieldTemplateRepository.findByWorkflowId(workflowId).flatMap(this::convertIntoDTO);
    }

    private Mono<WorkflowFieldDTO> convertIntoDTO(WorkflowFieldTemplate workflowFieldTemplate) {
        if(workflowFieldTemplate == null){
            return Mono.empty();
        }

        return Mono.just(workflowFieldTemplate)
                .map(wft -> new WorkflowFieldDTO(wft.getId(), wft.getWorkflowId(), wft.getFieldName(), wft.getFieldDescription(),
                        wft.getFieldType(), wft.getFieldValidation(), wft.getCreatedBy(), wft.getCreatedOn(),
                        wft.getUpdatedBy(), wft.getUpdatedOn()));
    }
}
