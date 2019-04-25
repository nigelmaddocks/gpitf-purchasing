package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class TmpAssociatedServiceService {

    @Autowired
    private TmpAssociatedServiceRepository thisRepository;

    public List<TmpAssociatedService> getAllBySolutionIdOrderByName(String solutionId) {
        List<TmpAssociatedService> coll = new ArrayList<>();
        thisRepository.findAllBySolutionIdOrderByName(solutionId).forEach(coll::add);
        return coll;
    }    

}
