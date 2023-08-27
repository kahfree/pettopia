package a2.service;

import a2.model.Pet;
import a2.model.PetHealth;
import a2.repository.PetHealthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetHealthService {
    @Autowired
    private PetHealthRepository petHealthRepository;
}
