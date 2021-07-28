package in.bitanxen.app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "tb_case_status")
@Getter
@Setter
public class CaseStatus {

    @Id
    private String id;

    @Field(name = "workflow_id")
    private String workflowId;

    @Field(name = "case_status_name")
    private String caseStatusName;

    @Field(name = "case_status_type")
    private CaseStatusType caseStatusType;

    @Field(name = "created_by")
    private String createdBy;

    @Field(name = "created_on")
    private LocalDateTime createdOn;

    @Field(name = "updated_by")
    private String updatedBy;

    @Field(name = "updated_on")
    private String updatedOn;

    public CaseStatus(String workflowId, String caseStatusName, CaseStatusType caseStatusType, String createdBy) {
        this.workflowId = workflowId;
        this.caseStatusName = caseStatusName;
        this.caseStatusType = caseStatusType;
        this.createdBy = createdBy;
        this.createdOn = LocalDateTime.now();
    }
}
