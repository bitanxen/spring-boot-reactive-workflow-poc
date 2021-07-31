package in.bitanxen.app.repository;

import in.bitanxen.app.model.ActionFieldTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionFieldTemplateRepository extends ReactiveMongoRepository<ActionFieldTemplate, String> {
    Mono<ActionFieldTemplate> findByActionIdAndWorkflowFieldId(String actionId, String fieldId);
    Flux<ActionFieldTemplate> findAllByActionId(String actionId);
}
