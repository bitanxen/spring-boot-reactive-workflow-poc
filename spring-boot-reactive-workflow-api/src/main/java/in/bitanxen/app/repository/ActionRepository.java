package in.bitanxen.app.repository;

import in.bitanxen.app.model.Action;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ActionRepository extends ReactiveMongoRepository<Action, String> {
    Flux<Action> findAllByWorkflowId(String workflowId);
}
