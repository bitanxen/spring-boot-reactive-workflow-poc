package in.bitanxen.app.repository;

import in.bitanxen.app.model.Case;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CaseRepository extends ReactiveMongoRepository<Case, String> {
    Flux<Case> findAllByWorkflowId(String workflowId, Pageable pageable);
}
