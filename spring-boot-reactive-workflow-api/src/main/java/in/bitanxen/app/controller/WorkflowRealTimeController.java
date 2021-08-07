package in.bitanxen.app.controller;

import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.service.WorkflowRealTimeService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class WorkflowRealTimeController {

    private final WorkflowRealTimeService workflowRealTimeService;

    public WorkflowRealTimeController(WorkflowRealTimeService workflowRealTimeService) {
        this.workflowRealTimeService = workflowRealTimeService;
    }

    @MessageMapping("workflow.case.realtime")
    public Flux<CaseEventDTO> notificationStream() {
        return this.workflowRealTimeService
                .getRealtimeWorkflowEvent()
                .map(WorkflowEvent::getCaseEvent);
    }

    @MessageMapping("workflow.case.details")
    public Mono<String> getCaseDetails(String data) {
        return Mono.just(data+" from server");
    }

    @MessageExceptionHandler
    public Mono<String> handleException(Exception e) {
        return Mono.just(e.getLocalizedMessage());
    }
}
