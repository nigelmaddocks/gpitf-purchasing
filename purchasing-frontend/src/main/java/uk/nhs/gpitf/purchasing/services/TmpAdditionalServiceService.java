package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class TmpAdditionalServiceService {

    @Autowired
    private TmpAdditionalServiceRepository thisRepository;

    public List<TmpAdditionalService> getAllBySolutionIdOrderByName(String solutionId) {
        List<TmpAdditionalService> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByName(solutionId).forEach(coll::add);
        return coll;
    }    

}
