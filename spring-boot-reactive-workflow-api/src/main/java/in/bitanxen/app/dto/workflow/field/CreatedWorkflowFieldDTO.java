package in.bitanxen.app.dto.workflow.field;

import in.bitanxen.app.model.WorkflowFieldType;
import in.bitanxen.app.model.WorkflowFieldValidation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedWorkflowFieldDTO {
    private String workflowId;
    private String fieldName;
    private String fieldDescription;
    private WorkflowFieldType fieldType;
    private WorkflowFieldValidation fieldValidation;
}
