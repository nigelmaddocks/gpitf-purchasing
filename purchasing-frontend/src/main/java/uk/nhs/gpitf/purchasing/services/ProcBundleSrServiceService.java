package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcBundleSrServiceService {

    @Autowired
    private ProcBundleSrServiceRepository thisRepository;

    public List<ProcBundleSrService>getAllForBundleOrderById(ProcSolutionBundle bundle) {
        List<ProcBundleSrService> coll = new ArrayList<>();
        thisRepository.findAllByBundleOrderById(bundle).forEach(coll::add);
        return coll;
    }
    
}
