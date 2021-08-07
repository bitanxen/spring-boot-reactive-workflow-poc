package in.bitanxen.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetCaseDetailsDTO {
    private String caseId;
    private List<String> workflowIds;
}
