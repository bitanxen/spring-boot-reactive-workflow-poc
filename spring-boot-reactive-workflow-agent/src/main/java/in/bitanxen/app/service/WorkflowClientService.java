package in.bitanxen.app.service;

import in.bitanxen.app.config.AbstractWorkflowOperation;
import in.bitanxen.app.dto.CaseEventDTO;
import org.springframework.boot.context.event.ApplicationReadyEvent;

public interface WorkflowClientService {
    String getCase(AbstractWorkflowOperation abstractWorkflowOperation, String caseId);
    void processStreamData(ApplicationReadyEvent applicationReadyEvent);
}
