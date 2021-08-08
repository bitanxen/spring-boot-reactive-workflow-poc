package in.bitanxen.app.service;

import in.bitanxen.app.dto.CaseDTO;

public interface UserWorkflowService {
    CaseDTO getCaseDetails(String caseId);
}
