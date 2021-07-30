package in.bitanxen.app.repository;

import in.bitanxen.app.model.WorkflowFieldTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkflowFieldTemplateRepository extends ReactiveMongoRepository<WorkflowFieldTemplate, String> {
    Mono<WorkflowFieldTemplate> findByWorkflowIdAndFieldName(String workflowId, String fieldName);
    Flux<WorkflowFieldTemplate> findByWorkflowId(String workflowId);
}
