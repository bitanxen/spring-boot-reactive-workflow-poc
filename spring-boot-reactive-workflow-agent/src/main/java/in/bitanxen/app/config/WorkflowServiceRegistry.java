package in.bitanxen.app.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;

public class WorkflowServiceRegistry {

    private final List<AbstractWorkflowOperation> registry;

    public WorkflowServiceRegistry(List<AbstractWorkflowOperation> registry) {
        this.registry = registry;
    }

    public List<AbstractWorkflowOperation> getRegistry() {
        return registry;
    }
}
