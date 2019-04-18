package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcSrvRecipientService {

    @Autowired
    private ProcSrvRecipientRepository thisRepository;

    public List<ProcSrvRecipient> getAllByProcurementOrderByOrganisationName(Procurement procurement) {
        List<ProcSrvRecipient> coll = new ArrayList<>();
        thisRepository.findAllByProcurementOrderByOrganisationName(procurement).forEach(coll::add);
        return coll;
    }    

}
