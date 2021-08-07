package in.bitanxen.app.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WorkflowRegistration {
    private String workflowId;
    private AbstractWorkflowOperation abstractWorkflowOperation;
}
