package in.bitanxen.app.dto.caseworkflow;

import in.bitanxen.app.model.CaseStatusType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaseEventDTO {
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

    public CaseEventDTO(String caseId, String currentWorkflowId, String currentCaseStatusId, String currentCaseStatusName,
                        CaseStatusType currentCaseStatusType, String caseCreatedBy, LocalDateTime caseCreatedOn) {
        this.caseId = caseId;
        this.currentWorkflowId = currentWorkflowId;
        this.currentCaseStatusId = currentCaseStatusId;
        this.currentCaseStatusName = currentCaseStatusName;
        this.currentCaseStatusType = currentCaseStatusType;
        this.caseCreatedBy = caseCreatedBy;
        this.caseCreatedOn = caseCreatedOn;
    }
}
