package in.bitanxen.app.dto.caseworkflow;

import in.bitanxen.app.model.CaseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseEventDTO {
    private String workflowId;
    private String caseId;
    private String currentStatusId;
    private String caseStatusName;
    private CaseStatusType caseStatusType;

}
