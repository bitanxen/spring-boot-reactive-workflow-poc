package in.bitanxen.app.repository;

import in.bitanxen.app.model.CaseStatus;
import in.bitanxen.app.model.CaseStatusType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CaseStatusRepository extends ReactiveMongoRepository<CaseStatus, String> {
    Flux<CaseStatus> findAllByWorkflowId(String workflowId);
    Mono<CaseStatus> findByWorkflowIdAndCaseStatusType(String workflowId, CaseStatusType caseStatusType);
}
