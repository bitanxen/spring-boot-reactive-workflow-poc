package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.caseworkflow.CaseActionDTO;
import in.bitanxen.app.dto.caseworkflow.CaseDTO;
import in.bitanxen.app.dto.caseworkflow.CreateCaseDTO;
import in.bitanxen.app.model.Case;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CaseService {
    Mono<Case> getCase(String caseId);
    Mono<CaseDTO> getCaseDetails(String caseId);
    Flux<CaseDTO> getWorkflowCases(String workflowId, Integer page, Integer size);

    Mono<CaseDTO> createCase(CreateCaseDTO createCase, User user);

    Mono<CaseDTO> getCasePerformAction(String caseId, CaseActionDTO caseAction, User user);
}
