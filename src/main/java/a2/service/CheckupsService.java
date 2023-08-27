package a2.service;

import a2.model.Checkups;
import a2.repository.CheckupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckupsService {
    @Autowired
    private CheckupsRepository checkupsRepository;
    public Checkups addCheckup(Checkups checkup){ return checkupsRepository.save(checkup);}
}
