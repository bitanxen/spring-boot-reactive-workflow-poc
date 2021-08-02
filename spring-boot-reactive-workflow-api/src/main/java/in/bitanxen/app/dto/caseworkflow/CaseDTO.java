package in.bitanxen.app.dto.caseworkflow;

import in.bitanxen.app.model.CaseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseDTO {
    private String caseId;
    private String workflowId;
    private String caseStatusId;
    private String caseStatusName;
    private CaseStatusType caseStatusType;
    private Map<String, String> caseDetails;
    private String createdBy;
    private LocalDateTime createdOn;
}
