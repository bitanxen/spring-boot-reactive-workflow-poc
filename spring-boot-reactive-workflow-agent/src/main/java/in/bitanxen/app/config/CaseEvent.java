package in.bitanxen.app.config;

import in.bitanxen.app.dto.CaseEventType;
import in.bitanxen.app.dto.CaseStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CaseEvent {
    private CaseEventType caseEventType;
    private String eventPerformedBy;
    private LocalDateTime eventPerformedOn;
    private String caseId;
    private String currentWorkflowId;
    private String currentCaseStatusId;
    private String currentCaseStatusName;
    private CaseStatusType currentCaseStatusType;
    private String actionId;
    private String previousWorkflowId;
    private String previousCaseStatusId;
    private String caseCreatedBy;
    private LocalDateTime caseCreatedOn;
}
