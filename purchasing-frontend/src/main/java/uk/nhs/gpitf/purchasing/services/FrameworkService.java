package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FrameworkService {

    @Autowired
    private FrameworkRepository thisRepository;
	
	@Value("${sysparam.initialFrameworkId}")
	public String FRAMEWORK_ID;

    public Framework getDefaultFramework() {
        Optional<Framework> optFramework = thisRepository.findByFrameworkId(FRAMEWORK_ID);
        if (optFramework.isEmpty()) {
        	return null;
        } else {
        	return optFramework.get();
        }
    }    

}
