package in.bitanxen.app.dto.casestatus.field;

import in.bitanxen.app.model.TemplateFieldValidation;
import in.bitanxen.app.model.WorkflowFieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCaseStatusFieldDTO {
    private String caseStatusId;
    private String fieldId;
    private String fieldName;
    private String fieldDescription;
    private WorkflowFieldType fieldType;
    private TemplateFieldValidation fieldValidation;
}
