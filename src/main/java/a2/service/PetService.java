package a2.service;

import a2.model.Customer;
import a2.model.Pet;
import a2.model.PetHealth;
import a2.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public PetHealth getPetHealthByPetID(int id){ return petRepository.findById(id).get().getPetHealth();}
    public Optional<Pet> getPetByID(int id){ return petRepository.findById(id);}
    public List<Pet> getPetsByCustomerID(int id){ return petRepository.findByCustomerId_CustomerId(id);}
    public List<Pet> getPetsForCheckupReminder(){ return petRepository.getPetsForUpcomingCheckup();}
    public List<Pet> getAllPets(){ return (List<Pet>) petRepository.findAll();}
}
