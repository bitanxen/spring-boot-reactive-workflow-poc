package in.bitanxen.app.dto.action.field;

import in.bitanxen.app.model.TemplateFieldValidation;
import in.bitanxen.app.model.WorkflowFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateActionFieldDTO {
    private String actionId;
    private String fieldId;
    private String fieldName;
    private String fieldDescription;
    private WorkflowFieldType fieldType;
    private TemplateFieldValidation fieldValidation;
}
