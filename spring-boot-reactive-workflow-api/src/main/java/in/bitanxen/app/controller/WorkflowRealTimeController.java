package in.bitanxen.app.controller;

import in.bitanxen.app.dto.caseworkflow.CaseDTO;
import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.service.CaseService;
import in.bitanxen.app.service.WorkflowRealTimeService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class WorkflowRealTimeController {

    private final WorkflowRealTimeService workflowRealTimeService;
    private final CaseService caseService;

    public WorkflowRealTimeController(WorkflowRealTimeService workflowRealTimeService, CaseService caseService) {
        this.workflowRealTimeService = workflowRealTimeService;
        this.caseService = caseService;
    }

    @MessageMapping("workflow.case.realtime")
    public Flux<CaseEventDTO> notificationStream() {
        return this.workflowRealTimeService
                .getRealtimeWorkflowEvent()
                .map(WorkflowEvent::getCaseEvent)
                .log();
    }

    @MessageMapping("workflow.case.details")
    public Mono<CaseDTO> getCaseDetails(String data) {
        return caseService.getCaseDetails(data);
    }

    @MessageExceptionHandler
    public Mono<String> handleException(Exception e) {
        return Mono.just(e.getLocalizedMessage());
    }
}
