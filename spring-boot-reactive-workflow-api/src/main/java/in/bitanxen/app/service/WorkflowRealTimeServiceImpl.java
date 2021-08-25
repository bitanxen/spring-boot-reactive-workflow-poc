package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import in.bitanxen.app.dto.caseworkflow.CaseEventType;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.event.WorkflowEventListener;
import in.bitanxen.app.model.Case;
import in.bitanxen.app.model.CaseStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class WorkflowRealTimeServiceImpl implements WorkflowRealTimeService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Flux<WorkflowEvent> workflowEvents;
    private final CaseStatusService caseStatusService;
    private final ActionService actionService;

    public WorkflowRealTimeServiceImpl(ApplicationEventPublisher applicationEventPublisher, WorkflowEventListener workflowEventListener,
                                       CaseStatusService caseStatusService, ActionService actionService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.workflowEvents = Flux.create(workflowEventListener).share();
        this.caseStatusService = caseStatusService;
        this.actionService = actionService;
    }

    @Override
    public Flux<WorkflowEvent> getRealtimeWorkflowEvent() {
        return this.workflowEvents.log();
    }

    @Override
    public Disposable publishCasePreCreateEvent(Case caseDetails, User user) {
        return Mono.just(caseDetails)
                .flatMap(this::convertInto)
                .map(caseEventDTO -> {
                    caseEventDTO.setCaseEventType(CaseEventType.CASE_PRE_CREATE);
                    caseEventDTO.setEventPerformedBy(user.getMemberId());
                    caseEventDTO.setEventPerformedOn(LocalDateTime.now());
                    return caseEventDTO;
                })
                .flatMap(c -> {
                    applicationEventPublisher.publishEvent(new WorkflowEvent(this, c));
                    return Mono.empty();
                })
                .subscribe();
    }

    @Override
    public Disposable publishCasePostCreateEvent(Case caseDetails, User user) {
        return Mono.just(caseDetails)
                .flatMap(this::convertInto)
                .map(caseEventDTO -> {
                    caseEventDTO.setCaseEventType(CaseEventType.CASE_POST_CREATE);
                    caseEventDTO.setEventPerformedBy(user.getMemberId());
                    caseEventDTO.setEventPerformedOn(LocalDateTime.now());
                    return caseEventDTO;
                })
                .flatMap(c -> {
                    applicationEventPublisher.publishEvent(new WorkflowEvent(this, c));
                    return Mono.empty();
                })
                .subscribe();
    }

    @Override
    public Disposable publishCaseActionEvent(Case caseDetails, String actionId, User user) {
        return Mono.just(caseDetails)
                .flatMap(this::convertInto)
                .map(caseEventDTO -> {
                    caseEventDTO.setCaseEventType(CaseEventType.CASE_ACTION_PERFORMED);
                    caseEventDTO.setEventPerformedBy(user.getMemberId());
                    caseEventDTO.setEventPerformedOn(LocalDateTime.now());
                    return caseEventDTO;
                })
                .zipWith(actionService.getActionDetails(actionId))
                .map(objects -> {
                    CaseEventDTO caseData = objects.getT1();
                    ActionDTO action = objects.getT2();
                    caseData.setActionId(actionId);
                    caseData.setPreviousWorkflowId(action.getWorkflowId());
                    caseData.setPreviousCaseStatusId(action.getSourceCaseStatusId());
                    return caseData;
                })
                .flatMap(c -> {
                    applicationEventPublisher.publishEvent(new WorkflowEvent(this, c));
                    return Mono.empty();
                })
                .subscribe();
    }

    public Mono<CaseEventDTO> convertInto(Case caseDetails) {
        return Mono.just(caseDetails)
                .zipWith(caseStatusService.getCaseStatus(caseDetails.getCaseStatusId()))
                .map(objects -> {
                    Case caseData = objects.getT1();
                    CaseStatus caseStatus = objects.getT2();
                    return new CaseEventDTO(caseDetails.getId(), caseDetails.getWorkflowId(), caseDetails.getCaseStatusId(),
                            caseStatus.getCaseStatusName(), caseStatus.getCaseStatusType(), caseData.getCreatedBy(), caseData.getCreatedOn());
                });
    }
}
