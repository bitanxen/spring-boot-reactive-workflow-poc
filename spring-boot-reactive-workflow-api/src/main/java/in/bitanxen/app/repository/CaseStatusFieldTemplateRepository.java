package in.bitanxen.app.repository;

import in.bitanxen.app.model.CaseStatusFieldTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CaseStatusFieldTemplateRepository extends ReactiveMongoRepository<CaseStatusFieldTemplate, String> {
    Flux<CaseStatusFieldTemplate> findAllByCaseStatusId(String destinationCaseStatusId);
    Mono<CaseStatusFieldTemplate> findByCaseStatusIdAndWorkflowFieldId(String id, String id1);
}
