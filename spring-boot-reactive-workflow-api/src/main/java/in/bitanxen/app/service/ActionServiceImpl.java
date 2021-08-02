package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.CreateActionDTO;
import in.bitanxen.app.dto.action.field.CreateActionFieldDTO;
import in.bitanxen.app.dto.workflow.field.CreatedWorkflowFieldDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.exception.WorkflowException;
import in.bitanxen.app.model.*;
import in.bitanxen.app.repository.ActionFieldTemplateRepository;
import in.bitanxen.app.repository.ActionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionServiceImpl implements ActionService {

    private final WorkflowService workflowService;
    private final CaseStatusService caseStatusService;
    private final WorkflowFieldTemplateService workflowFieldTemplateService;

    private final ActionRepository actionRepository;
    private final ActionFieldTemplateRepository actionFieldTemplateRepository;

    public ActionServiceImpl(WorkflowService workflowService, CaseStatusService caseStatusService, WorkflowFieldTemplateService workflowFieldTemplateService,
                             ActionRepository actionRepository, ActionFieldTemplateRepository actionFieldTemplateRepository) {
        this.workflowService = workflowService;
        this.caseStatusService = caseStatusService;
        this.workflowFieldTemplateService = workflowFieldTemplateService;
        this.actionRepository = actionRepository;
        this.actionFieldTemplateRepository = actionFieldTemplateRepository;
    }

    @Override
    public Mono<ActionDTO> getActionDetails(String actionId) {
        return getAction(actionId).flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<Action> getAction(String actionId) {
        return actionRepository.findById(actionId);
    }

    @Override
    public Mono<ActionDTO> createAction(CreateActionDTO createAction, User user) {
        return Mono.just(createAction)
                .map(ca -> new Action(ca.getActionName(), createAction.isMandatoryComment(), user.getMemberId()))
                .zipWith(workflowService.getWorkflow(createAction.getWorkflowId()).switchIfEmpty(Mono.error(new WorkflowException("Workflow not provided"))))
                .map(tuple -> {
                    Action action = tuple.getT1();
                    action.setWorkflowId(tuple.getT2().getId());
                    return action;
                })
                .zipWith(caseStatusService.getCaseStatus(createAction.getSourceCaseStatusId()).switchIfEmpty(Mono.error(new WorkflowException("Case Status not provided"))))
                .map(tuple -> {
                    Action action = tuple.getT1();
                    CaseStatus caseStatus = tuple.getT2();
                    if(!action.getWorkflowId().equals(caseStatus.getWorkflowId())) {
                        throw new WorkflowException("Source Case Status doesn't belong to provided Workflow");
                    }

                    if(caseStatus.getCaseStatusType().equals(CaseStatusType.COMPLETE)) {
                        throw new WorkflowException("Action cannot be created on a END case status");
                    }
                    action.setSourceCaseStatusId(caseStatus.getId());
                    return action;
                })
                .zipWith(getDestination(createAction, user))
                .map(tuple -> {
                    Action action = tuple.getT1();
                    CaseStatus destinationCaseStatus = tuple.getT2();
                    action.setDestinationWorkflowId(destinationCaseStatus.getWorkflowId());
                    action.setDestinationCaseStatusId(destinationCaseStatus.getId());
                    return action;
                })
                .flatMap(actionRepository::save)
                .flatMap(this::convertIntoDTO);
    }

    @Override
    public Flux<ActionDTO> getWorkflowActions(String workflowId) {
        return actionRepository.findAllByWorkflowId(workflowId).flatMap(this::convertIntoDTO);
    }

    @Override
    public Mono<ActionDTO> createActionField(CreateActionFieldDTO createActionField, User user) {
        return Mono.just(createActionField)
                .flatMap(acf -> getAction(acf.getActionId()))
                .zipWith(createActionField.getFieldId() != null ?
                        workflowFieldTemplateService.getFieldTemplate(createActionField.getFieldId()) :
                                getAction(createActionField.getActionId())
                                .flatMap(a -> workflowFieldTemplateService.createFieldTemplateDirect(
                                        Mono.just(new CreatedWorkflowFieldDTO(
                                                a.getWorkflowId(), createActionField.getFieldName(), createActionField.getFieldDescription(),
                                                createActionField.getFieldType(), WorkflowFieldValidation.OPTIONAL
                                        )),
                                        user
                                ))
                        )
                .flatMap(tuple -> {
                    Action action = tuple.getT1();
                    WorkflowFieldTemplate workflowFieldTemplate = tuple.getT2();

                    return actionFieldTemplateRepository.findByActionIdAndWorkflowFieldId(action.getId(), workflowFieldTemplate.getId())
                            .flatMap(a -> Mono.error(new WorkflowException("Field already exist in action")))
                            .switchIfEmpty(Mono.defer(() -> actionFieldTemplateRepository.save(new ActionFieldTemplate(action.getId(), workflowFieldTemplate.getId(), createActionField.getFieldValidation()))));
                })
                .flatMap(actionFieldTemplate -> getAction(createActionField.getActionId()))
                .flatMap(this::convertIntoDTO);

    }

    @Override
    public Flux<TemplateFieldDTO> getActionField(String actionId) {
        return convertIntoActionField(actionId)
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<CaseStatus> getDestination(CreateActionDTO createAction, User user) {
        if(createAction == null) {
            return Mono.empty();
        }
        return Mono.just(createAction)
                .zipWith(createAction.getDestinationWorkflowId() != null ? workflowService.getWorkflow(createAction.getDestinationWorkflowId()) : Mono.just(new Workflow()))
                .zipWith(createAction.getDestinationCaseStatusId() != null ? caseStatusService.getCaseStatus(createAction.getDestinationCaseStatusId()) : Mono.just(new CaseStatus()))
                .flatMap(tuple -> {
                    Workflow workflow = tuple.getT1().getT2().getId() != null ? tuple.getT1().getT2() : null;
                    CaseStatus caseStatus = tuple.getT2().getId() != null ? tuple.getT2() : null;

                    if(caseStatus == null && workflow == null) {
                        return Mono.error(new WorkflowException("Destination properties are null"));
                    }

                    if(caseStatus != null) {
                        return Mono.just(caseStatus);
                    }

                    return caseStatusService.createCaseStatus(workflow, createAction.getDestinationCaseStatusName(), createAction.getDestinationCaseStatusType(), user);
                });
    }

    private Mono<ActionDTO> convertIntoDTO(Action action) {
        if(action == null) {
            return null;
        }

        return Mono.just(new ActionDTO())
                .map(actionDTO -> {
                    actionDTO.setActionId(action.getId());
                    actionDTO.setActionName(action.getActionName());
                    actionDTO.setMandatoryComment(action.isMandatoryComment());
                    actionDTO.setCreatedBy(action.getCreatedBy());
                    actionDTO.setCreatedOn(action.getCreatedOn());
                    actionDTO.setUpdatedBy(action.getUpdatedBy());
                    actionDTO.setUpdatedOn(action.getUpdatedOn());
                    return actionDTO;
                })
                .zipWith(caseStatusService.getCaseStatus(action.getSourceCaseStatusId()))
                .map(tuple -> {
                    ActionDTO actionDTO = tuple.getT1();
                    CaseStatus caseStatus = tuple.getT2();
                    actionDTO.setSourceCaseStatusId(caseStatus.getId());
                    actionDTO.setSourceCaseStatusName(caseStatus.getCaseStatusName());
                    actionDTO.setSourceCaseStatusType(caseStatus.getCaseStatusType());
                    return actionDTO;
                })
                .zipWith(workflowService.getWorkflow(action.getWorkflowId()))
                .map(tuple -> {
                    ActionDTO actionDTO = tuple.getT1();
                    Workflow workflow = tuple.getT2();
                    actionDTO.setWorkflowId(workflow.getId());
                    actionDTO.setWorkflowName(workflow.getWorkflowName());
                    return actionDTO;
                })
                .zipWith(caseStatusService.getCaseStatus(action.getDestinationCaseStatusId()))
                .map(tuple -> {
                    ActionDTO actionDTO = tuple.getT1();
                    CaseStatus caseStatus = tuple.getT2();
                    actionDTO.setDestinationCaseStatusId(caseStatus.getId());
                    actionDTO.setDestinationCaseStatusName(caseStatus.getCaseStatusName());
                    actionDTO.setDestinationCaseStatusType(caseStatus.getCaseStatusType());
                    return actionDTO;
                })
                .zipWith(workflowService.getWorkflow(action.getDestinationWorkflowId()))
                .map(tuple -> {
                    ActionDTO actionDTO = tuple.getT1();
                    Workflow workflow = tuple.getT2();
                    actionDTO.setDestinationWorkflowId(workflow.getId());
                    actionDTO.setDestinationWorkflowName(workflow.getWorkflowName());
                    return actionDTO;
                })
                .flatMap(actionDTO -> convertIntoActionField(action.getId())
                        .map(templateFields -> {
                            actionDTO.setTemplateFields(templateFields);
                            return actionDTO;
                        })
                );
    }

    private Mono<List<TemplateFieldDTO>> convertIntoActionField(String actionId) {
        if(actionId == null) {
            return Mono.empty();
        }

        return Mono.just(actionId)
                .flatMap(this::getAction)
                .flatMap(action -> Mono.zip(
                        actionFieldTemplateRepository.findAllByActionId(action.getId()).collectList(),
                        caseStatusService.getCaseStatusFields(action.getDestinationCaseStatusId()).collectList()
                ))
                .flatMap(tuple -> {
                    List<ActionFieldTemplate> actionFields = tuple.getT1();
                    List<CaseStatusFieldTemplate> caseStatusFields = tuple.getT2();

                    Mono<List<TemplateFieldDTO>> caseStatusSelectedFields = Mono.just(caseStatusFields)
                            .flatMapMany(Flux::fromIterable)
                            //.filter(caseStatusFieldTemplate -> caseStatusFieldTemplate.getFieldValidation().equals(TemplateFieldValidation.MANDATORY))
                            .filter(caseStatusFieldTemplate -> !fieldInAction(caseStatusFieldTemplate, actionFields))
                            .flatMap(caseStatusService::convertCaseStatusFieldToDTO)
                            .collectList();

                    Mono<List<TemplateFieldDTO>> actionSelectedFields = Mono.just(actionFields)
                            .flatMapMany(Flux::fromIterable)
                            //.filter(actionFieldTemplate -> actionFieldTemplate.getFieldValidation().equals(TemplateFieldValidation.MANDATORY))
                            .flatMap(this::convertActionFieldToDTO)
                            .collectList();
                    return Mono.zip(caseStatusSelectedFields, actionSelectedFields)
                            .map(selectedTuple -> {
                                List<TemplateFieldDTO> t1 = selectedTuple.getT1();
                                List<TemplateFieldDTO> t2 = selectedTuple.getT2();

                                List<TemplateFieldDTO> finalData = new ArrayList<>();
                                finalData.addAll(t1);
                                finalData.addAll(t2);
                                return finalData;
                            });
                });
    }

    private boolean fieldInAction(CaseStatusFieldTemplate caseStatusFieldTemplate, List<ActionFieldTemplate> actionFields) {
        return actionFields.stream()
                .anyMatch(actionFieldTemplate -> actionFieldTemplate.getWorkflowFieldId().equals(caseStatusFieldTemplate.getWorkflowFieldId()));
    }

    private Mono<TemplateFieldDTO> convertActionFieldToDTO(ActionFieldTemplate actionFieldTemplate) {
        return Mono.just(actionFieldTemplate)
                .zipWith(workflowFieldTemplateService.getFieldTemplate(actionFieldTemplate.getWorkflowFieldId()))
                .map(tuple -> {
                    ActionFieldTemplate t1 = tuple.getT1();
                    WorkflowFieldTemplate t2 = tuple.getT2();

                    return new TemplateFieldDTO(t2.getId(), t2.getWorkflowId(), t2.getFieldName(), t2.getFieldDescription(),
                            t2.getFieldType(), t2.getFieldValidation(), t1.getFieldValidation(),
                            t2.getCreatedBy(), t2.getCreatedOn(), t2.getUpdatedBy(), t2.getUpdatedOn());
                });
    }


}
