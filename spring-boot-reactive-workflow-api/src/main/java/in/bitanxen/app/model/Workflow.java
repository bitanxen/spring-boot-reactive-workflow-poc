package in.bitanxen.app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tb_workflow")
@Getter
@Setter
public class Workflow {

    @Id
    private String id;

    @Field(name = "workflow_name")
    private String workflowName;

    @Field(name = "workflow_description")
    private String workflowDescription;

    @Field(name = "subscriber_id")
    private String subscriberId;

    @Field(name = "enabled")
    private boolean enabled;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    @Field(name = "updated_by")
    private String updatedBy;

    @Field(name = "updated_on")
    private String updatedOn;

    public Workflow(String workflowName, String workflowDescription, String subscriberId, String createdBy) {
        this.workflowName = workflowName;
        this.workflowDescription = workflowDescription;
        this.subscriberId = subscriberId;
        this.enabled = true;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }
}
