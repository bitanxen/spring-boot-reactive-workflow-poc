package in.bitanxen.app.controller;

import in.bitanxen.app.dto.caseworkflow.CaseActionDTO;
import in.bitanxen.app.dto.caseworkflow.CaseDTO;
import in.bitanxen.app.dto.caseworkflow.CreateCaseDTO;
import in.bitanxen.app.service.CaseService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/workflow/case")
public class CaseWorkflowController {

    private final CaseService caseService;

    public CaseWorkflowController(CaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping("/")
    public Mono<CaseDTO> createCase(@RequestBody CreateCaseDTO createCase) {
        return caseService.createCase(createCase, CommonUtil.getUser());
    }

    @GetMapping("/id/{caseId}")
    public Mono<CaseDTO> getCaseDetails(@PathVariable String caseId) {
        return caseService.getCaseDetails(caseId);
    }

    @PostMapping("/id/{caseId}/action")
    public Mono<CaseDTO> performAction(@PathVariable String caseId, @RequestBody CaseActionDTO caseAction) {
        return caseService.getCasePerformAction(caseId, caseAction);
    }
}
