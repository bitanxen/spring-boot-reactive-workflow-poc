package in.bitanxen.app.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class WorkflowEventListener implements ApplicationListener<WorkflowEvent>, Consumer<FluxSink<WorkflowEvent>> {

    private final Executor executor;
    private final BlockingQueue<WorkflowEvent> queue = new LinkedBlockingQueue<>();

    public WorkflowEventListener(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void accept(FluxSink<WorkflowEvent> workflowEventFluxSink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    WorkflowEvent event = queue.take();
                    workflowEventFluxSink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }

    @Override
    public void onApplicationEvent(WorkflowEvent workflowEvent) {
        this.queue.offer(workflowEvent);
    }
}
