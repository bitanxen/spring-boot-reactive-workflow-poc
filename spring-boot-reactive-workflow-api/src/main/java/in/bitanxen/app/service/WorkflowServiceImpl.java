package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.workflow.CreateWorkflowDTO;
import in.bitanxen.app.dto.workflow.UpdateWorkflowDTO;
import in.bitanxen.app.dto.workflow.WorkflowDTO;
import in.bitanxen.app.model.CaseStatus;
import in.bitanxen.app.model.CaseStatusType;
import in.bitanxen.app.model.Workflow;
import in.bitanxen.app.repository.CaseStatusRepository;
import in.bitanxen.app.repository.WorkflowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final CaseStatusRepository caseStatusRepository;

    public WorkflowServiceImpl(WorkflowRepository workflowRepository, CaseStatusRepository caseStatusRepository) {
        this.workflowRepository = workflowRepository;
        this.caseStatusRepository = caseStatusRepository;
    }

    @Override
    public Flux<WorkflowDTO> getAllWorkflows() {
        return workflowRepository.findAll().map(this::convertIntoDTO);
    }

    @Override
    public Flux<WorkflowDTO> getAllSubscriberWorkflows() {
        return null;
    }

    @Override
    public Mono<Workflow> getWorkflow(String workflowId) {
        return workflowRepository.findById(workflowId);
    }

    @Override
    public Mono<WorkflowDTO> getWorkflowDetails(String workflowId) {
        return getWorkflow(workflowId).map(this::convertIntoDTO);
    }

    @Override
    public Mono<WorkflowDTO> createWorkflow(Mono<CreateWorkflowDTO> createWorkflow, User user) {
        return createWorkflow.flatMap(cw -> {
                    Workflow workflow = new Workflow(cw.getWorkflowName(), cw.getWorkflowDescription(), user.getSubscriberId(), user.getMemberId());
                    return workflowRepository.save(workflow);
                })
                .flatMap(workflow -> {
                    String workflowId = workflow.getId();
                    System.out.println(workflowId);
                    CaseStatus initialCaseStatus = new CaseStatus(workflowId, "Started", CaseStatusType.STARTED, user.getMemberId());
                    return caseStatusRepository.save(initialCaseStatus);
                })
                .flatMap(caseStatus -> getWorkflowDetails(caseStatus.getWorkflowId()));
    }

    @Override
    public Mono<WorkflowDTO> updateWorkflow(String workflowId, Mono<UpdateWorkflowDTO> updateWorkflow, User user) {
        return null;
    }

    private WorkflowDTO convertIntoDTO(Workflow workflow) {
        if(workflow == null) {
            return null;
        }

        return new WorkflowDTO(workflow.getId(), workflow.getWorkflowName(), workflow.isEnabled(),
                workflow.getCreatedBy(), workflow.getCreatedOn(), workflow.getUpdatedBy(), workflow.getUpdatedOn());
    }
}
