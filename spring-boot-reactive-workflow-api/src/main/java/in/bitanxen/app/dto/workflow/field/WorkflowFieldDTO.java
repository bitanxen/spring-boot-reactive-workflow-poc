package in.bitanxen.app.dto.workflow.field;

import in.bitanxen.app.model.WorkflowFieldType;
import in.bitanxen.app.model.WorkflowFieldValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowFieldDTO {
    private String fieldId;
    private String workflowId;
    private String fieldName;
    private String fieldDescription;
    private WorkflowFieldType fieldType;
    private WorkflowFieldValidation fieldValidation;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
}
