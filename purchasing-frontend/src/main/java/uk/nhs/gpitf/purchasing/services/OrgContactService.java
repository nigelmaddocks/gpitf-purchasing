package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgContactService {

    @Autowired
    private OrgContactRepository thisRepository;

    public List<OrgContact> getAllByOrganisation(Organisation organisation) {
        List<OrgContact> coll = new ArrayList<>();
        Iterable<OrgContact> itb = thisRepository.findAllByOrganisationAndDeletedOrderByContactSurnameAscContactForenameAsc(organisation, false);
        for (OrgContact orgContact : itb) {
        	if (!orgContact.getContact().isDeleted() ) {
        		coll.add(orgContact);
        	}
        }
        return coll;
    }    

}
