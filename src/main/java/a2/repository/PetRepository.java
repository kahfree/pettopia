package a2.repository;

import a2.model.Customer;
import a2.model.Pet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PetRepository extends CrudRepository<Pet, Integer> {
    List<Pet> findByCustomerId_CustomerId(int customerID);

    @Query("SELECT p FROM Pet p WHERE p.petHealth.nextCheckUp BETWEEN CURRENT_DATE AND FUNCTION('ADD_DATE', CURRENT_DATE, 7, 'DAY')")
    List<Pet> getPetsForUpcomingCheckup();
}
