package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcShortlistRemovalReasonService {

    @Autowired
    private ProcShortlistRemovalReasonRepository thisRepository;

    public List<ProcShortlistRemovalReason> getAll() {
        List<ProcShortlistRemovalReason> coll = new ArrayList<>();
        thisRepository.findAll().forEach(coll::add);
        return coll;
    }    
}
