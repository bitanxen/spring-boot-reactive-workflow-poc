package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.workflow.CreateWorkflowDTO;
import in.bitanxen.app.dto.workflow.WorkflowDTO;
import in.bitanxen.app.dto.workflow.UpdateWorkflowDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkflowService {
    Flux<WorkflowDTO> getAllWorkflows();
    Flux<WorkflowDTO> getAllSubscriberWorkflows();
    Mono<WorkflowDTO> getWorkflow(String workflowId);

    Mono<WorkflowDTO> createWorkflow(Mono<CreateWorkflowDTO> createWorkflow, User user);
    Mono<WorkflowDTO> updateWorkflow(String workflowId, Mono<UpdateWorkflowDTO> updateWorkflow, User user);
}
