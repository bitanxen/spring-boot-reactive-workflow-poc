package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.caseworkflow.CaseActionDTO;
import in.bitanxen.app.dto.caseworkflow.CaseDTO;
import in.bitanxen.app.dto.caseworkflow.CreateCaseDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.dto.workflow.field.WorkflowFieldDTO;
import in.bitanxen.app.exception.CaseWorkflowException;
import in.bitanxen.app.model.*;
import in.bitanxen.app.repository.CaseRepository;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaseServiceImpl implements CaseService {

    private final WorkflowService workflowService;
    private final WorkflowFieldTemplateService workflowFieldTemplateService;
    private final WorkflowEventService workflowEventService;
    private final CaseStatusService caseStatusService;
    private final ActionService actionService;

    private final CaseRepository caseRepository;

    public CaseServiceImpl(WorkflowService workflowService, WorkflowFieldTemplateService workflowFieldTemplateService, WorkflowEventService workflowEventService,
                           CaseStatusService caseStatusService, ActionService actionService, CaseRepository caseRepository) {
        this.workflowService = workflowService;
        this.workflowFieldTemplateService = workflowFieldTemplateService;
        this.workflowEventService = workflowEventService;
        this.caseStatusService = caseStatusService;
        this.actionService = actionService;
        this.caseRepository = caseRepository;
    }

    @Override
    public Mono<CaseDTO> createCase(CreateCaseDTO createCase, User user) {
        return Mono.just(createCase)
                .zipWith(workflowService.getWorkflow(createCase.getWorkflowId()))
                .zipWith(workflowFieldTemplateService.getWorkflowCaseFields(createCase.getWorkflowId()).collectList())
                .flatMap(objects -> {
                    Tuple2<CreateCaseDTO, Workflow> t1 = objects.getT1();
                    CreateCaseDTO createCaseDTO = t1.getT1();
                    Workflow workflow = t1.getT2();
                    List<WorkflowFieldDTO> workflowInitialFields = objects.getT2();
                    Case caseDetails = new Case(workflow.getId(), user.getMemberId());
                    caseDetails.setCaseDetails(validateAndCreateCaseParameters(createCaseDTO, workflowInitialFields));
                    return Mono.just(caseDetails);
                })
                .zipWith(getInitialCaseStatus(createCase.getWorkflowId()))
                .flatMap(objects -> {
                    Case caseDetails = objects.getT1();
                    CaseStatus caseStatus = objects.getT2();
                    caseDetails.setCaseStatusId(caseStatus.getId());
                    return Mono.just(caseDetails);
                })
                .doOnNext(caseDetails -> workflowEventService.publishCasePreCreateEvent(caseDetails, user))
                .flatMap(caseRepository::save)
                .doOnNext(caseDetails -> workflowEventService.publishCasePostCreateEvent(caseDetails, user))
                .flatMap(this::convertIntoDTO);

    }

    @Override
    public Mono<CaseDTO> getCasePerformAction(String caseId, CaseActionDTO caseAction) {
        return getCase(caseId)
                .zipWith(actionService.getActionDetails(caseAction.getActionId()))
                .flatMap(objects -> {
                    Case caseDetails = objects.getT1();
                    ActionDTO actionDetails = objects.getT2();

                    if(!caseDetails.getWorkflowId().equals(actionDetails.getWorkflowId())) {
                        return Mono.error(new CaseWorkflowException("Action cannot be taken on the target case as they belong from different workflow"));
                    }

                    if(!caseDetails.getCaseStatusId().equals(actionDetails.getSourceCaseStatusId())) {
                        return Mono.error(new CaseWorkflowException("Action cannot be taken on the target case as case is in a different status"));
                    }

                    Map<String, String> actionParameters = validateAndCreateCaseActionParameters(caseAction, actionDetails.getTemplateFields());

                    Map<String, String> caseDetailsParameters = caseDetails.getCaseDetails();
                    caseDetailsParameters.putAll(actionParameters);
                    caseDetails.setCaseDetails(caseDetailsParameters);

                    caseDetails.setCaseStatusId(actionDetails.getDestinationCaseStatusId());
                    caseDetails.setWorkflowId(actionDetails.getDestinationWorkflowId());
                    return Mono.just(caseDetails);
                })
                .flatMap(caseRepository::save)
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<Case> getCase(String caseId) {
        return caseRepository.findById(caseId);
    }

    @Override
    public Mono<CaseDTO> getCaseDetails(String caseId) {
        return getCase(caseId)
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Flux<CaseDTO> getWorkflowCases(String workflowId, Integer page, Integer size) {
        return caseRepository.findAllByWorkflowId(workflowId, CommonUtil.getPageRequest(page, size))
                .flatMap(this::convertIntoDTO);
    }

    private Mono<CaseDTO> convertIntoDTO(Case aCase) {
        return Mono.just(aCase)
                .zipWith(caseStatusService.getCaseStatus(aCase.getCaseStatusId()))
                .flatMap(objects -> {
                    Case caseDetails = objects.getT1();
                    CaseStatus caseStatus = objects.getT2();

                    CaseDTO caseDTO = new CaseDTO();
                    caseDTO.setCaseId(caseDetails.getId());
                    caseDTO.setWorkflowId(caseDetails.getWorkflowId());
                    caseDTO.setCaseStatusId(caseStatus.getId());
                    caseDTO.setCaseStatusName(caseStatus.getCaseStatusName());
                    caseDTO.setCaseStatusType(caseStatus.getCaseStatusType());
                    caseDTO.setCaseDetails(caseDetails.getCaseDetails());
                    caseDTO.setCreatedBy(caseDetails.getCreatedBy());
                    caseDTO.setCreatedOn(caseDetails.getCreatedOn());
                    return Mono.just(caseDTO);
                });
    }

    private Mono<CaseStatus> getInitialCaseStatus(String workflowId) {
        return caseStatusService.getInitialCaseStatusForWorkflow(workflowId);
    }

    public Map<String, String> validateAndCreateCaseActionParameters(CaseActionDTO caseActionDTO, List<TemplateFieldDTO> actionTemplateFields) {
        Map<String, String> actionFieldDetails = caseActionDTO.getActionFieldDetails();
        return actionTemplateFields.stream()
                .map(templateField -> {
                    String fieldId = templateField.getFieldId();
                    String fieldValue = actionFieldDetails.get(fieldId);
                    TemplateFieldValidation overriddenFieldValidation = templateField.getOverriddenFieldValidation();

                    if(overriddenFieldValidation.equals(TemplateFieldValidation.MANDATORY) && ((fieldValue == null || fieldValue.trim().length() == 0))) {
                        throw new CaseWorkflowException(templateField.getFieldName() + " ["+fieldId+"] is a mandatory field for which a valid value is not provided");
                    }
                    return Tuples.of(fieldId, Optional.ofNullable(fieldValue));
                })
                .collect(Collectors.toMap(Tuple2::getT1, tuple -> tuple.getT2().isPresent() ? tuple.getT2().get() : ""));
    }

    public Map<String, String> validateAndCreateCaseParameters(CreateCaseDTO createCaseDTO, List<WorkflowFieldDTO> workflowInitialFields) {
        Map<String, String> caseDetails = createCaseDTO.getCaseDetails();
        return workflowInitialFields.stream()
                .filter(workflowField -> workflowField.getFieldValidation().equals(WorkflowFieldValidation.INITIAL) ||
                        workflowField.getFieldValidation().equals(WorkflowFieldValidation.INITIAL_MANDATORY))
                .map(workflowField -> {
                    String fieldId = workflowField.getFieldId();
                    String fieldValue = caseDetails.get(fieldId);
                    WorkflowFieldValidation fieldValidation = workflowField.getFieldValidation();
                    if(fieldValidation.equals(WorkflowFieldValidation.INITIAL_MANDATORY) && (fieldValue == null || fieldValue.trim().length() == 0)) {
                        throw new CaseWorkflowException(workflowField.getFieldName() + " ["+fieldId+"] is a mandatory field for which a valid value is not provided");
                    }
                    return Tuples.of(fieldId, Optional.ofNullable(fieldValue));
                })
                .collect(Collectors.toMap(Tuple2::getT1, tuple -> tuple.getT2().isPresent() ? tuple.getT2().get() : ""));
    }
}
