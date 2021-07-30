package in.bitanxen.app.dto.casestatus;

import in.bitanxen.app.model.CaseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseStatusDTO {
    private String caseStatusId;
    private String workflowId;
    private String caseStatusName;
    private CaseStatusType caseStatusType;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
}
