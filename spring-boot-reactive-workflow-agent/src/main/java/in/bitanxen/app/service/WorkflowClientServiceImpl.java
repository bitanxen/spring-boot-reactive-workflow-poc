package in.bitanxen.app.service;

import in.bitanxen.app.config.AbstractWorkflowOperation;
import in.bitanxen.app.config.CaseEvent;
import in.bitanxen.app.config.WorkflowRegistration;
import in.bitanxen.app.config.WorkflowRegistrationContextHolder;
import in.bitanxen.app.dto.CaseEventDTO;
import in.bitanxen.app.dto.GetCaseDetailsDTO;
import in.bitanxen.app.exception.WorkflowClientException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkflowClientServiceImpl implements WorkflowClientService {

    private final RSocketClientOperation rSocketClientOperation;

    public WorkflowClientServiceImpl(RSocketClientOperation rSocketClientOperation) {
        this.rSocketClientOperation = rSocketClientOperation;
    }

    @Override
    public String getCase(AbstractWorkflowOperation abstractWorkflowOperation, String caseId) {
        List<String> workflows = getWorkflowLists(abstractWorkflowOperation);
        GetCaseDetailsDTO getCaseDetails = GetCaseDetailsDTO.builder()
                .caseId(caseId)
                .workflowIds(workflows)
                .build();
        return rSocketClientOperation.fireAndForget("workflow.case.details", caseId);
    }

    @Override
    public void streamData(CaseEventDTO caseEvent) {
        List<WorkflowRegistration> workflowRegistrations = WorkflowRegistrationContextHolder.getRegistrations()
                .stream()
                .filter(workflowRegistration -> workflowRegistration.getWorkflowId().equals(caseEvent.getCurrentWorkflowId()) ||
                        workflowRegistration.getWorkflowId().equals(caseEvent.getPreviousWorkflowId()))
                .collect(Collectors.toList());
        for(WorkflowRegistration workflowRegistration : workflowRegistrations) {
            AbstractWorkflowOperation abstractWorkflowOperation = workflowRegistration.getAbstractWorkflowOperation();
            deligateCaseData(abstractWorkflowOperation, caseEvent);
        }
    }

    private void deligateCaseData(AbstractWorkflowOperation abstractWorkflowOperation, CaseEventDTO caseEvent) {
        switch (caseEvent.getCaseEventType()) {
            case CASE_PRE_CREATE:
                abstractWorkflowOperation.onCasePreCreation(convertIntoCaseEventDTO(caseEvent));
                break;
            case CASE_POST_CREATE:
                abstractWorkflowOperation.onCasePostCreation(convertIntoCaseEventDTO(caseEvent));
                break;
            case CASE_ACTION_PERFORMED:
                abstractWorkflowOperation.onCaseAction(convertIntoCaseEventDTO(caseEvent));
        }
    }

    private CaseEvent convertIntoCaseEventDTO(CaseEventDTO caseEvent) {
        return CaseEvent.builder()
                .caseEventType(caseEvent.getCaseEventType())
                .eventPerformedBy(caseEvent.getEventPerformedBy())
                .eventPerformedOn(caseEvent.getEventPerformedOn())
                .caseId(caseEvent.getCaseId())
                .currentWorkflowId(caseEvent.getCurrentWorkflowId())
                .currentCaseStatusId(caseEvent.getCurrentCaseStatusId())
                .currentCaseStatusName(caseEvent.getCurrentCaseStatusName())
                .currentCaseStatusType(caseEvent.getCurrentCaseStatusType())
                .actionId(caseEvent.getActionId())
                .previousWorkflowId(caseEvent.getPreviousWorkflowId())
                .previousCaseStatusId(caseEvent.getPreviousCaseStatusId())
                .caseCreatedBy(caseEvent.getCaseCreatedBy())
                .caseCreatedOn(caseEvent.getCaseCreatedOn())
                .build();
    }

    private List<String> getWorkflowLists(AbstractWorkflowOperation abstractWorkflowOperation) {
        List<String> workflows = abstractWorkflowOperation.getWorkflows();
        if(workflows == null || workflows.isEmpty()) {
            throw new WorkflowClientException("No workflows provided");
        }
        return workflows;
    }
}
