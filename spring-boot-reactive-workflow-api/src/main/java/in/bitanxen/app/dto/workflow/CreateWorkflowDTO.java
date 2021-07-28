package in.bitanxen.app.dto.workflow;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkflowDTO {
    private String workflowName;
    private String workflowDescription;
}
