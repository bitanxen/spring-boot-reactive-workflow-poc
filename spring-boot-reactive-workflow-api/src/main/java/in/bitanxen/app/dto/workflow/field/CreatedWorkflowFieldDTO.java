package in.bitanxen.app.dto.workflow.field;

import in.bitanxen.app.model.WorkflowFieldType;
import in.bitanxen.app.model.WorkflowFieldValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatedWorkflowFieldDTO {
    private String workflowId;
    private String fieldName;
    private String fieldDescription;
    private WorkflowFieldType fieldType;
    private WorkflowFieldValidation fieldValidation;
}
