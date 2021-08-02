package in.bitanxen.app.dto.caseworkflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreateCaseDTO {
    private String workflowId;
    private Map<String, String> caseDetails;
}
