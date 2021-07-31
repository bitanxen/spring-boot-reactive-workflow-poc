package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "tb_action_fields")
@Getter
@Setter
@NoArgsConstructor
public class ActionFieldTemplate {

    @Id
    private String id;

    @Field(name = "action_id")
    private String actionId;

    @Field(name = "workflow_field_id")
    private String workflowFieldId;

    @Field(name = "template_field_validation")
    private TemplateFieldValidation fieldValidation;

    public ActionFieldTemplate(String actionId, String workflowFieldId, TemplateFieldValidation templateFieldValidation) {
        this.actionId = actionId;
        this.workflowFieldId = workflowFieldId;
        this.fieldValidation = templateFieldValidation;
    }
}
