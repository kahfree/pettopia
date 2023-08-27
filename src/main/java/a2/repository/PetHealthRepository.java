package a2.repository;

import a2.model.Pet;
import a2.model.PetHealth;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetHealthRepository extends CrudRepository<PetHealth, Integer> {
}
