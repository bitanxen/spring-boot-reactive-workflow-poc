package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.model.CaseStatus;
import in.bitanxen.app.model.CaseStatusType;
import in.bitanxen.app.model.Workflow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CaseStatusService {
    Mono<CaseStatus> getCaseStatus(String caseStatusId);

    Mono<CaseStatus> createCaseStatus(Workflow workflow, String destinationCaseStatusName, CaseStatusType destinationCaseStatusType, User user);

    Flux<CaseStatusDTO> getWorkflowCaseStatus(String workflowId);
}
