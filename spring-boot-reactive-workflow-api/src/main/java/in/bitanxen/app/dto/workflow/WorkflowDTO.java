package in.bitanxen.app.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDTO {
    private String workflowId;
    private String workflowName;
    private boolean enabled;
}
