package in.bitanxen.app.dto.casestatus;

import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.model.CaseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseStatusDTO {
    private String caseStatusId;
    private String workflowId;
    private String caseStatusName;
    private CaseStatusType caseStatusType;
    private List<TemplateFieldDTO> templateFields;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;
}
