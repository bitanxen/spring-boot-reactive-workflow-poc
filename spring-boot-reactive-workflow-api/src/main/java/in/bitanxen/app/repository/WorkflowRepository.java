package in.bitanxen.app.repository;

import in.bitanxen.app.model.Workflow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends ReactiveMongoRepository<Workflow, String> {

}
