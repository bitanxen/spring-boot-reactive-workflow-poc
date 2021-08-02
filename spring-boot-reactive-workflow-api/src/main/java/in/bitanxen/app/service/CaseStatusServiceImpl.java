package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.casestatus.field.CreateCaseStatusFieldDTO;
import in.bitanxen.app.dto.workflow.field.CreatedWorkflowFieldDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.exception.WorkflowException;
import in.bitanxen.app.model.*;
import in.bitanxen.app.repository.CaseStatusFieldTemplateRepository;
import in.bitanxen.app.repository.CaseStatusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CaseStatusServiceImpl implements CaseStatusService {

    private final WorkflowFieldTemplateService workflowFieldTemplateService;

    private final CaseStatusRepository caseStatusRepository;
    private final CaseStatusFieldTemplateRepository caseStatusFieldTemplateRepository;

    public CaseStatusServiceImpl(WorkflowFieldTemplateService workflowFieldTemplateService, CaseStatusRepository caseStatusRepository,
                                 CaseStatusFieldTemplateRepository caseStatusFieldTemplateRepository) {
        this.workflowFieldTemplateService = workflowFieldTemplateService;
        this.caseStatusRepository = caseStatusRepository;
        this.caseStatusFieldTemplateRepository = caseStatusFieldTemplateRepository;
    }

    @Override
    public Mono<CaseStatus> getCaseStatus(String caseStatusId) {
        return caseStatusRepository.findById(caseStatusId);
    }

    @Override
    public Mono<CaseStatusDTO> getCaseStatusDetails(String caseStatusId) {
        return getCaseStatus(caseStatusId)
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<CaseStatus> getInitialCaseStatusForWorkflow(String workflowId) {
        return caseStatusRepository.findByWorkflowIdAndCaseStatusType(workflowId, CaseStatusType.STARTED);
    }

    @Override
    public Mono<CaseStatus> createCaseStatus(Workflow workflow, String destinationCaseStatusName, CaseStatusType destinationCaseStatusType, User user) {
        if(workflow != null && destinationCaseStatusName != null && destinationCaseStatusType != null && user != null) {
            return caseStatusRepository.save(new CaseStatus(workflow.getId(), destinationCaseStatusName, destinationCaseStatusType, user.getMemberId()));
        }
        return Mono.error(new WorkflowException("Case Status parameters are not valid"));
    }

    @Override
    public Flux<CaseStatusDTO> getWorkflowCaseStatus(String workflowId) {
        return caseStatusRepository.findAllByWorkflowId(workflowId).flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<CaseStatusDTO> createCaseStatusField(CreateCaseStatusFieldDTO createCaseStatusField, User user) {
        return Mono.just(createCaseStatusField)
                .flatMap(cs -> getCaseStatus(cs.getCaseStatusId()))
                .zipWith(createCaseStatusField.getFieldId() != null ?
                        workflowFieldTemplateService.getFieldTemplate(createCaseStatusField.getFieldId()) :
                        getCaseStatus(createCaseStatusField.getCaseStatusId())
                                .flatMap(a -> workflowFieldTemplateService.createFieldTemplateDirect(
                                        Mono.just(new CreatedWorkflowFieldDTO(
                                                a.getWorkflowId(), createCaseStatusField.getFieldName(), createCaseStatusField.getFieldDescription(),
                                                createCaseStatusField.getFieldType(), WorkflowFieldValidation.OPTIONAL
                                        )),
                                        user
                                ))
                )
                .flatMap(tuple -> {
                    CaseStatus caseStatus = tuple.getT1();
                    WorkflowFieldTemplate workflowFieldTemplate = tuple.getT2();

                    return caseStatusFieldTemplateRepository.findByCaseStatusIdAndWorkflowFieldId(caseStatus.getId(), workflowFieldTemplate.getId())
                            .flatMap(a -> Mono.error(new WorkflowException("Field already exist in case status")))
                            .switchIfEmpty(Mono.defer(() -> caseStatusFieldTemplateRepository.save(new CaseStatusFieldTemplate(caseStatus.getId(), workflowFieldTemplate.getId(), createCaseStatusField.getFieldValidation()))));
                })
                .flatMap(actionFieldTemplate -> getCaseStatus(createCaseStatusField.getCaseStatusId()))
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Flux<CaseStatusFieldTemplate> getCaseStatusFields(String destinationCaseStatusId) {
        return caseStatusFieldTemplateRepository.findAllByCaseStatusId(destinationCaseStatusId);
    }

    @Override
    public Flux<TemplateFieldDTO> getCaseStatusField(String caseStatusId) {
        return getCaseStatusFields(caseStatusId)
                .flatMap(this::convertCaseStatusFieldToDTO);
    }

    private Mono<CaseStatusDTO> convertIntoDTO(CaseStatus caseStatus) {
        if(caseStatus == null) {
            return Mono.empty();
        }
        return Mono.just(caseStatus)
                .map(cs -> new CaseStatusDTO(cs.getId(), cs.getWorkflowId(), cs.getCaseStatusName(),
                        cs.getCaseStatusType(), null, cs.getCreatedBy(), cs.getCreatedOn(),
                        cs.getUpdatedBy(), cs.getUpdatedOn()))
                .zipWith(getCaseStatusFields(caseStatus.getId())
                        .flatMap(this::convertCaseStatusFieldToDTO)
                        .collectList()
                )
                .map(tuple -> {
                    CaseStatusDTO caseStatusDTO = tuple.getT1();
                    List<TemplateFieldDTO> caseStatusFields = tuple.getT2();
                    caseStatusDTO.setTemplateFields(caseStatusFields);
                    return caseStatusDTO;
                });
    }

    @Override
    public Mono<TemplateFieldDTO> convertCaseStatusFieldToDTO(CaseStatusFieldTemplate caseStatusFieldTemplate) {
        return Mono.just(caseStatusFieldTemplate)
                .zipWith(workflowFieldTemplateService.getFieldTemplate(caseStatusFieldTemplate.getWorkflowFieldId()))
                .map(tuple -> {
                    CaseStatusFieldTemplate t1 = tuple.getT1();
                    WorkflowFieldTemplate t2 = tuple.getT2();

                    return new TemplateFieldDTO(t2.getId(), t2.getWorkflowId(), t2.getFieldName(), t2.getFieldDescription(),
                            t2.getFieldType(), t2.getFieldValidation(), t1.getFieldValidation(),
                            t2.getCreatedBy(), t2.getCreatedOn(), t2.getUpdatedBy(), t2.getUpdatedOn());
                });
    }
}
