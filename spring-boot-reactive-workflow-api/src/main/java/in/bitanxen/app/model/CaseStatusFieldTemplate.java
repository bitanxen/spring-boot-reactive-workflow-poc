package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_case_status_fields")
@Getter
@Setter
@NoArgsConstructor
public class CaseStatusFieldTemplate {

    @Id
    private String id;

    @Field(name = "workflow_field_id")
    private String workflowFieldId;

    @Field(name = "template_field_validation")
    private TemplateFieldValidation fieldValidation;

    public CaseStatusFieldTemplate(String workflowFieldId, TemplateFieldValidation templateFieldValidation) {
        this.workflowFieldId = workflowFieldId;
        this.fieldValidation = templateFieldValidation;
    }
}
