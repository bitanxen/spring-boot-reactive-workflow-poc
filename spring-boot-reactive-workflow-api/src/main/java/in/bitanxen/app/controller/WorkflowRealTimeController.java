package in.bitanxen.app.controller;

import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import in.bitanxen.app.event.WorkflowEvent;
import in.bitanxen.app.service.WorkflowRealTimeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

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
}
