package in.bitanxen.app.service;

import in.bitanxen.app.config.AbstractWorkflowOperation;
import in.bitanxen.app.dto.CaseEventDTO;

public interface WorkflowClientService {
    String getCase(AbstractWorkflowOperation abstractWorkflowOperation, String caseId);
    void streamData(CaseEventDTO s);
}
