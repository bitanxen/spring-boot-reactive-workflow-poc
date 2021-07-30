package in.bitanxen.app.dto.action;

import in.bitanxen.app.model.CaseStatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateActionDTO {
    private String actionName;
    private String workflowId;
    private String sourceCaseStatusId;
    private String destinationWorkflowId;
    private String destinationCaseStatusId;
    private String destinationCaseStatusName;
    private CaseStatusType destinationCaseStatusType;
    private boolean mandatoryComment;
}
