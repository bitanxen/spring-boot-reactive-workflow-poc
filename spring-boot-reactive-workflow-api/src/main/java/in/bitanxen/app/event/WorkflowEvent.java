package in.bitanxen.app.event;

import in.bitanxen.app.dto.caseworkflow.CaseEventDTO;
import org.springframework.context.ApplicationEvent;

public class WorkflowEvent extends ApplicationEvent {

    private final CaseEventDTO caseEvent;

    public WorkflowEvent(Object source, CaseEventDTO caseEvent) {
        super(source);
        this.caseEvent = caseEvent;
        System.out.println(caseEvent);
    }

    public CaseEventDTO getCaseEvent() {
        return this.caseEvent;
    }
}
