package uk.nhs.gpitf.purchasing.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.utils.GUtils;

@Service
public class ProcurementService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementService.class);

    @Autowired
    private ProcurementRepository thisRepository;

    public List<Procurement> getAll() {
        List<Procurement> coll = new ArrayList<>();
        thisRepository.findAll().forEach(coll::add);
        return coll;
    }

    public List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(long iOrgContact, long iStatus) {
        OrgContact orgContact = new OrgContact();
        orgContact.setId(iOrgContact);
        ProcStatus status = new ProcStatus();
        status.setId(iStatus);

        List<Procurement> coll = new ArrayList<>();
        thisRepository.findAllByOrgContactAndStatusOrderByLastUpdatedDesc(orgContact, status).forEach(coll::add);
        return coll;
    }

    public List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status) {
        List<Procurement> coll = new ArrayList<>();
        thisRepository.findAllByOrgContactAndStatusOrderByLastUpdatedDesc(orgContact, status).forEach(coll::add);
        return coll;
    }

    public List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(long iOrgContact) {
        OrgContact orgContact = new OrgContact();
        orgContact.setId(iOrgContact);
        return thisRepository.findUncompletedByOrgContactOrderByLastUpdated(orgContact);
    }

    public List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(OrgContact orgContact) {
       return thisRepository.findUncompletedByOrgContactOrderByLastUpdated(orgContact);
    }

    public Procurement saveCurrentPosition(long procurementId, long orgContactId, Optional<String> searchKeyword, 
    		Optional<String> csvCapabilities, Optional<Boolean> foundation, Optional<String> csvPractices) throws Exception {
    	Procurement procurement = null;
    	if (procurementId == 0) {
    		procurement = createNewProcurement(orgContactId);
    	} else {
    		procurement = findById(procurementId);
    	}

    	
    	if (searchKeyword.isPresent()) procurement.setSearchKeyword(searchKeyword.get());
    	if (csvCapabilities.isPresent()) procurement.setCsvCapabilities(csvCapabilities.get());
    	if (foundation.isPresent()) procurement.setFoundation(foundation.get());
    	if (csvPractices.isPresent()) {
    		String sCsvPractices = csvPractices.get();
    		if (sCsvPractices.startsWith(",")) {
    			sCsvPractices = sCsvPractices.substring(1);
    		}
    		if (sCsvPractices.endsWith(",")) {
    			sCsvPractices = sCsvPractices.substring(0, sCsvPractices.length()-1);
    		}
    		procurement.setCsvPractices(sCsvPractices);
    	}
    	procurement.setLastUpdated(LocalDateTime.now());
    	procurement = save(procurement);

    	return procurement;
    }

    public Procurement findById(Long procurementId) throws ProcurementNotFoundException {
      Optional<Procurement> optProcurement = thisRepository.findById(procurementId);
      if (optProcurement.isEmpty()) {
        LOGGER.warn("An attempt to retrieve Procurement \"{}\" occurred. But could not be found.", procurementId);
        throw new ProcurementNotFoundException("Procurement " + procurementId + " not found");
      }

      // Validation required to check User has access to requested procurement.
      // Throw UnauthorizedDataAccessException if the case.
      return optProcurement.get();
    }

    public Procurement save(Procurement procurement) {
      // Validation required to check User has access to requested procurement.
      // Throw UnauthorizedDataAccessException if the case.
      return thisRepository.save(procurement);
    }

    private Procurement createNewProcurement(long orgContactId) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
    	LocalDateTime now = LocalDateTime.now();
    	Procurement procurement = new Procurement();
		procurement.setName("Procurement-for-" + orgContactId + "-" + now.format(formatter));
    	procurement.setStartedDate(now);
    	procurement.setOrgContact((OrgContact)GUtils.makeObjectForId(OrgContact.class, orgContactId));
    	procurement.setStatus((ProcStatus)GUtils.makeObjectForId(ProcStatus.class, ProcStatus.DRAFT));
    	procurement.setStatusLastChangedDate(now);
    	procurement.setLastUpdated(now);

    	procurement = thisRepository.save(procurement);

		procurement.setName("Procurement-" + procurement.getId() + "-" + now.format(formatter));
    	procurement = thisRepository.save(procurement);

    	return procurement;
    }

}
