package in.bitanxen.app.service;

import in.bitanxen.app.config.provider.User;
import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.field.CreateActionFieldDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.casestatus.field.CreateCaseStatusFieldDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.model.CaseStatus;
import in.bitanxen.app.model.CaseStatusFieldTemplate;
import in.bitanxen.app.model.CaseStatusType;
import in.bitanxen.app.model.Workflow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CaseStatusService {
    Mono<CaseStatus> getCaseStatus(String caseStatusId);
    Mono<CaseStatusDTO> getCaseStatusDetails(String caseStatusId);
    Mono<CaseStatus> getInitialCaseStatusForWorkflow(String workflowId);

    Mono<CaseStatus> createCaseStatus(Workflow workflow, String destinationCaseStatusName, CaseStatusType destinationCaseStatusType, User user);

    Flux<CaseStatusDTO> getWorkflowCaseStatus(String workflowId);

    Mono<CaseStatusDTO> createCaseStatusField(CreateCaseStatusFieldDTO createCaseStatusField, User user);
    Flux<CaseStatusFieldTemplate> getCaseStatusFields(String destinationCaseStatusId);
    Flux<TemplateFieldDTO> getCaseStatusField(String caseStatusId);

    Mono<TemplateFieldDTO> convertCaseStatusFieldToDTO(CaseStatusFieldTemplate caseStatusFieldTemplate);

}
