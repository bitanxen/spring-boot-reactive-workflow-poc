package in.bitanxen.app.repository;

import in.bitanxen.app.model.CaseStatusFieldTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CaseStatusFieldTemplateRepository extends ReactiveMongoRepository<CaseStatusFieldTemplate, String> {

}
