package in.bitanxen.app.repository;

import in.bitanxen.app.model.CaseStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseStatusRepository extends ReactiveMongoRepository<CaseStatus, String> {
}
