package in.bitanxen.app.dto.action;

import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.model.CaseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionDTO {
    private String actionId;
    private String actionName;
    private String workflowId;
    private String workflowName;
    private String sourceCaseStatusId;
    private String sourceCaseStatusName;
    private CaseStatusType sourceCaseStatusType;
    private String destinationWorkflowId;
    private String destinationWorkflowName;
    private String destinationCaseStatusId;
    private String destinationCaseStatusName;
    private CaseStatusType destinationCaseStatusType;
    private boolean mandatoryComment;
    private List<TemplateFieldDTO> templateFields;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
}
