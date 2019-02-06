package uk.nhs.gpitf.purchasing.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.utils.GUtils;

@Service
public class ProcurementService {

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
    		Optional<String> csvCapabilities, Optional<String> csvPractices) throws Exception {
    	Procurement procurement = null;
    	if (procurementId == 0) {
    		procurement = createNewProcurement(orgContactId);

    	} else {
    		Optional<Procurement>optProcurement = thisRepository.findById(procurementId);
    		if (optProcurement.isEmpty()) {
    			throw new Exception("Procurement " + procurementId + " not found");
    		} else {
    			procurement = optProcurement.get();
    		}
    	}

    	if (searchKeyword.isPresent()) {
          procurement.setSearchKeyword(searchKeyword.get());
        }
    	if (csvCapabilities.isPresent()) {
          procurement.setCsvCapabilities(csvCapabilities.get());
        }
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
    	procurement = thisRepository.save(procurement);

    	return procurement;
    }

    public Optional<Procurement> findById(Long procurementId) {
      return thisRepository.findById(procurementId);
    }

    public Procurement save(Procurement procurement) {
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
