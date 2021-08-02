package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_case_status_fields")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CaseStatusFieldTemplate {

    @Id
    private String id;

    @Field(name = "case_status_id")
    private String caseStatusId;

    @Field(name = "workflow_field_id")
    private String workflowFieldId;

    @Field(name = "template_field_validation")
    private TemplateFieldValidation fieldValidation;

    public CaseStatusFieldTemplate(String caseStatusId, String workflowFieldId, TemplateFieldValidation templateFieldValidation) {
        this.caseStatusId = caseStatusId;
        this.workflowFieldId = workflowFieldId;
        this.fieldValidation = templateFieldValidation;
    }
}
