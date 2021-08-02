package in.bitanxen.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "tb_case_workflow")
@Getter
@Setter
@NoArgsConstructor
public class Case {

    @Id
    private String id;

    @Field(name = "workflow_id")
    private String workflowId;

    @Field(name = "case_status_id")
    private String caseStatusId;

    @Field(name = "case_details")
    private Map<String, String> caseDetails;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    public Case(String workflowId, String createdBy) {
        this.workflowId = workflowId;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }
}
