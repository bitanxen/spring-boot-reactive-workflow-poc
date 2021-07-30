package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.exception.WorkflowException;
import in.bitanxen.app.model.CaseStatus;
import in.bitanxen.app.model.CaseStatusType;
import in.bitanxen.app.model.Workflow;
import in.bitanxen.app.repository.CaseStatusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CaseStatusServiceImpl implements CaseStatusService {

    private final CaseStatusRepository caseStatusRepository;

    public CaseStatusServiceImpl(CaseStatusRepository caseStatusRepository) {
        this.caseStatusRepository = caseStatusRepository;
    }

    @Override
    public Mono<CaseStatus> getCaseStatus(String caseStatusId) {
        return caseStatusRepository.findById(caseStatusId);
    }

    @Override
    public Mono<CaseStatus> createCaseStatus(Workflow workflow, String destinationCaseStatusName, CaseStatusType destinationCaseStatusType, User user) {
        if(workflow != null && destinationCaseStatusName != null && destinationCaseStatusType != null && user != null) {
            return caseStatusRepository.save(new CaseStatus(workflow.getId(), destinationCaseStatusName, destinationCaseStatusType, user.getMemberId()));
        }
        return Mono.error(new WorkflowException("Case Status parameters are not valid"));
    }

    @Override
    public Flux<CaseStatusDTO> getWorkflowCaseStatus(String workflowId) {
        return caseStatusRepository.findAllByWorkflowId(workflowId).flatMap(this::convertIntoDTO);
    }

    private Mono<CaseStatusDTO> convertIntoDTO(CaseStatus caseStatus) {
        if(caseStatus == null) {
            return Mono.empty();
        }
        return Mono.just(caseStatus)
                .map(cs -> new CaseStatusDTO(cs.getId(), cs.getWorkflowId(), cs.getCaseStatusName(),
                        cs.getCaseStatusType(), cs.getCreatedBy(), cs.getCreatedOn(),
                        cs.getUpdatedBy(), cs.getUpdatedOn()));
    }
}
