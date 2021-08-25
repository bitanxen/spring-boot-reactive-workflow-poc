package in.bitanxen.app.service;

import in.bitanxen.app.config.AbstractWorkflowOperation;
import in.bitanxen.app.config.CaseEvent;
import in.bitanxen.app.config.WorkflowRegistrationContextHolder;
import in.bitanxen.app.dto.CaseDTO;
import in.bitanxen.app.model.ServiceWorkflow;
import in.bitanxen.app.repository.ServiceWorkflowRepository;
import in.bitanxen.app.repository.UserCreationRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserWorkflowServiceImpl extends AbstractWorkflowOperation implements UserWorkflowService {

    private final ServiceWorkflowRepository serviceWorkflowRepository;
    private final UserCreationRepository userCreationRepository;

    public UserWorkflowServiceImpl(ServiceWorkflowRepository serviceWorkflowRepository, UserCreationRepository userCreationRepository) {
        this.serviceWorkflowRepository = serviceWorkflowRepository;
        this.userCreationRepository = userCreationRepository;
    }

    @Override
    public List<String> getWorkflows() {

        List<String> collect = serviceWorkflowRepository.findAll()
                .stream()
                .map(ServiceWorkflow::getWorkflowId)
                .collect(Collectors.toList());
        collect.add("6103c7f271ba0c62adf9ecd6");
        return collect;
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
    public CaseDTO getCaseDetails(String caseId) {
        System.out.println(WorkflowRegistrationContextHolder.getRegistrations());
        return super.getCaseDetails(caseId);
    }
}
