package in.bitanxen.app.repository;

import in.bitanxen.app.model.UserCreation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreationRepository extends JpaRepository<UserCreation, String> {

}
