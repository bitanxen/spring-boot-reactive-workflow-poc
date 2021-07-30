package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tb_workflow_fields")
@Getter
@Setter
@NoArgsConstructor
public class WorkflowFieldTemplate {

    @Id
    private String id;

    @Field(name = "workflow_id")
    private String workflowId;

    @Field(name = "field_name")
    private String fieldName;

    @Field(name = "field_description")
    private String fieldDescription;

    @Field(name = "field_type")
    private WorkflowFieldType fieldType;

    @Field(name = "field_validation")
    private WorkflowFieldValidation fieldValidation;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    @Field(name = "updated_by")
    private String updatedBy;

    @Field(name = "updated_on")
    private LocalDateTime updatedOn;

    public WorkflowFieldTemplate(String workflowId, String fieldName, String fieldDescription, WorkflowFieldType fieldType, WorkflowFieldValidation fieldValidation, String createdBy) {
        this.workflowId = workflowId;
        this.fieldName = fieldName;
        this.fieldDescription = fieldDescription;
        this.fieldType = fieldType;
        this.fieldValidation = fieldValidation;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }
}
