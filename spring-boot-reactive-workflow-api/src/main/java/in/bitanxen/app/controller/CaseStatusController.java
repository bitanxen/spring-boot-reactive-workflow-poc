package in.bitanxen.app.controller;

import in.bitanxen.app.dto.action.ActionDTO;
import in.bitanxen.app.dto.action.field.CreateActionFieldDTO;
import in.bitanxen.app.dto.casestatus.CaseStatusDTO;
import in.bitanxen.app.dto.casestatus.field.CreateCaseStatusFieldDTO;
import in.bitanxen.app.dto.workflow.field.TemplateFieldDTO;
import in.bitanxen.app.service.CaseStatusService;
import in.bitanxen.app.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/workflow/casestatus")
public class CaseStatusController {

    private final CaseStatusService caseStatusService;

    public CaseStatusController(CaseStatusService caseStatusService) {
        this.caseStatusService = caseStatusService;
    }

    @GetMapping("/id/{caseStatusId}")
    public Mono<CaseStatusDTO> getCaseStatusDetails(@PathVariable String caseStatusId) {
        return caseStatusService.getCaseStatusDetails(caseStatusId);
    }

    @PostMapping("/field")
    public Mono<CaseStatusDTO> createCaseStatusField(@RequestBody CreateCaseStatusFieldDTO createCaseStatusField) {
        return caseStatusService.createCaseStatusField(createCaseStatusField, CommonUtil.getUser());
    }

    @GetMapping("/id/{caseStatusId}/field")
    public Flux<TemplateFieldDTO> getCaseStatusField(@PathVariable String caseStatusId) {
        return caseStatusService.getCaseStatusField(caseStatusId);
    }
}
