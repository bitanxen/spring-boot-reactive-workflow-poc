package in.bitanxen.app.service;

import in.bitanxen.app.config.AbstractWorkflowOperation;
import in.bitanxen.app.config.CaseEvent;
import in.bitanxen.app.config.WorkflowRegistrationContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserWorkflowServiceImpl extends AbstractWorkflowOperation implements UserWorkflowService {

    @Override
    public List<String> getWorkflows() {
        return Arrays.asList("6103c7f271ba0c62adf9ecd6");
    }

    @Override
    public void onCasePreCreation(CaseEvent caseEvent) {
        System.out.println("onCasePreCreation: "+caseEvent);
    }

    @Override
    public void onCasePostCreation(CaseEvent caseEvent) {
        System.out.println("onCasePostCreation: "+caseEvent);
    }

    @Override
    public void onCaseAction(CaseEvent caseEvent) {
        System.out.println("onCaseAction: "+caseEvent);
    }

    @Override
    public String getCaseDetails(String caseId) {
        System.out.println(WorkflowRegistrationContextHolder.getRegistrations());
        return super.getCaseDetails(caseId);
    }
}
