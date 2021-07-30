package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tb_action")
@Getter
@Setter
@NoArgsConstructor
public class Action {

    @Id
    private String id;

    @Field(name = "action_name")
    private String actionName;

    @Field(name = "workflow_id")
    private String workflowId;

    @Field(name = "source_case_status_id")
    private String sourceCaseStatusId;

    @Field(name = "destination_workflow_id")
    private String destinationWorkflowId;

    @Field(name = "destination_case_status_id")
    private String destinationCaseStatusId;

    @Field(name = "mandatory_comment")
    private boolean mandatoryComment;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    @Field(name = "updated_by")
    private String updatedBy;

    @Field(name = "updated_on")
    private LocalDateTime updatedOn;

    public Action(String actionName, boolean mandatoryComment, String createdBy) {
        this.actionName = actionName;
        this.mandatoryComment = mandatoryComment;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }
}
