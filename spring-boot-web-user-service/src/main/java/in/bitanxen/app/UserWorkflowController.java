package in.bitanxen.app;

import in.bitanxen.app.service.UserWorkflowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserWorkflowController {

    private final UserWorkflowService userWorkflowService;

    public UserWorkflowController(UserWorkflowService userWorkflowService) {
        this.userWorkflowService = userWorkflowService;
    }

    @GetMapping("/workflow/case/{caseId}")
    private String getCaseDetails(@PathVariable String caseId) {
        return userWorkflowService.getCaseDetails(caseId);
    }
}
