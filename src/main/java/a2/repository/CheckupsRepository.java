package a2.repository;

import a2.model.Checkups;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface CheckupsRepository extends CrudRepository<Checkups, LocalDateTime> {
}
