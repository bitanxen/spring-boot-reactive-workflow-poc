package in.bitanxen.app.config;

import in.bitanxen.app.dto.CaseDTO;
import in.bitanxen.app.service.WorkflowClientService;

import java.util.List;
import java.util.UUID;

public abstract class AbstractWorkflowOperation {

    private final String operationId;

    public AbstractWorkflowOperation() {
        this.operationId = UUID.randomUUID().toString();
    }

    public final WorkflowClientService getWorkflowClientService() {
        return StaticApplicationContext.getApplicationContext().getBean(WorkflowClientService.class);
    }

    public String getOperationId() {
        return this.operationId;
    }

    public abstract List<String> getWorkflows();

    public abstract void onCasePreCreation(CaseEvent caseEvent);

    public abstract void onCasePostCreation(CaseEvent caseEvent);

    public abstract void onCaseAction(CaseEvent caseEvent);

    public CaseDTO getCaseDetails(String caseId) {
        String test = this.getWorkflowClientService().test();
        System.out.println(test);
        return this.getWorkflowClientService().getCase(this, caseId);
    }
}
