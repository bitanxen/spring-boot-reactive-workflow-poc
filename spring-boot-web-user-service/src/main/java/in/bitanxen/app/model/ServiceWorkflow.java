package in.bitanxen.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "tb_workflow_info")
@Getter
@Setter
public class ServiceWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_workflow_id")
    private String serviceWorkflowId;

    @Column(name = "workflow_id")
    private String workflowId;
}
