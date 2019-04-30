package uk.nhs.gpitf.purchasing.services;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.Procurement.PrimitiveProcurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Service
public class ProcurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ProcurementRepository thisRepository;

    @Autowired
	OrgContactRepository orgContactRepository;

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

    public Procurement saveCurrentPosition(
                          long procurementId,
                          long orgContactId,
                          Optional<String> searchKeyword,
                          Optional<String> csvCapabilities,
                          Optional<String> csvInteroperables,
                          Optional<EvaluationTypeEnum> evaluationType,
                          Optional<Boolean> foundation,
                          Optional<String> csvPractices) throws Exception {
    	final Procurement procurement;
    	if (procurementId == 0) {
    		procurement = createNewProcurement(orgContactId);
    	} else {
    		procurement = findById(procurementId);
    	}

    	searchKeyword.ifPresent(procurement::setSearchKeyword);
    	csvCapabilities.ifPresent(procurement::setCsvCapabilities);
    	csvInteroperables.ifPresent(procurement::setCsvInteroperables);
    	evaluationType.ifPresent(procurement::setEvaluationType);
    	foundation.ifPresent(procurement::setFoundation);
    	csvPractices.ifPresent(practices -> procurement.setCsvPractices(GUtils.trimCommas(practices)));
    	procurement.setLastUpdated(LocalDateTime.now());

    	return save(procurement);
    }

    public Procurement saveCurrentPosition(Procurement.PrimitiveProcurement prim) throws Exception {
    	Procurement procurement = null;
    	procurement = saveCurrentPosition(
    	    0,
    	    prim.getOrgContactId(),
    	    Optional.empty(),
			prim.getCsvCapabilities() == null ? Optional.empty() : Optional.of(prim.getCsvCapabilities()),
			prim.getCsvInteroperables() == null ? Optional.empty() : Optional.of(prim.getCsvInteroperables()),
			prim.getEvaluationType() == null ? Optional.empty() : Optional.of(prim.getEvaluationType()),
			prim.getFoundation() == null ? Optional.empty() : Optional.of(prim.getFoundation()),
			prim.getCsvPractices() == null ? Optional.empty() : Optional.of(GUtils.trimCommas(prim.getCsvPractices()))
		);
    	return procurement;
    }

    public Procurement findById(Long procurementId) throws ProcurementNotFoundException {
      // TODO Validation required to check User has access to requested procurement.
      // Throw UnauthorizedDataAccessException if the case.
      return thisRepository.findById(procurementId)
                           .orElseThrow(() -> {
                             LOGGER.warn("An attempt to retrieve Procurement \"{}\" occurred. But could not be found.", procurementId);
                             return new ProcurementNotFoundException("Procurement " + procurementId + " not found");
                           });
    }

    public Procurement save(Procurement procurement) {
      procurement.setLastUpdated(LocalDateTime.now());
      // TODO Validation required to check User has access to requested procurement.
      // Throw UnauthorizedDataAccessException if the case.
      return thisRepository.save(procurement);
    }

    public void delete(Long procurementId) throws ProcurementNotFoundException, Exception {
      delete(findById(procurementId));
    }

    public void delete(Procurement procurement) throws Exception {
      // TODO Validation required to check User has access to requested procurement.
      // Throw UnauthorizedDataAccessException if the case.
      // TODO refactor ProcStatus.DELETED etc to enum?
      procurement.setStatus((ProcStatus)GUtils.makeObjectForId(ProcStatus.class, ProcStatus.DELETED));
      LOGGER.info("Deleted procurement: {}", procurement.getId());
      save(procurement);

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

	/** Persists a procurement that is in its early stages and hasn't yet been saved to the database, to the session */
	public void persistToSession(HttpSession session, Procurement procurement) {
		PrimitiveProcurement prim = new PrimitiveProcurement();
		BeanUtils.copyProperties(procurement, prim);
		session.setAttribute(Procurement.SESSION_ATTR_NAME, prim);
	}

	/** Restores a procurement that is in its early stages and hasn't yet been saved to the database, from the session */
	public Procurement restoreFromSession(HttpSession session) {
		PrimitiveProcurement prim = (PrimitiveProcurement) session.getAttribute(Procurement.SESSION_ATTR_NAME);
		if (prim == null) {
			return null;
		}
		Procurement proc = new Procurement();
		BeanUtils.copyProperties(prim, proc);
		try {
			OrgContact orgContact = null;
			if (prim.getOrgContactId() != 0) {
				orgContact = orgContactRepository.findById(prim.getOrgContactId()).get();
			}

			proc.setStatus((ProcStatus)GUtils.makeObjectForId(ProcStatus.class, ProcStatus.DRAFT));
			if (prim.getEvaluationType() != null) {
			  proc.setEvaluationType(prim.getEvaluationType());
			}

			proc.setOrgContact(orgContact);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proc;
	}

    public Procurement.PrimitiveProcurement createNewPrimitiveProcurement(SecurityInfo secInfo) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
    	LocalDateTime now = LocalDateTime.now();

    	Procurement.PrimitiveProcurement prim = new Procurement.PrimitiveProcurement();
		prim.setName("Procurement-for-" + secInfo.getOrgContactId() + "-" + now.format(formatter));
		prim.setOrgContactId(secInfo.getOrgContactId());
    	return prim;
    }

    public Procurement.PrimitiveProcurement getPrimitiveProcurement(HttpSession session, SecurityInfo secInfo) {
      Procurement.PrimitiveProcurement procurement = (Procurement.PrimitiveProcurement) session.getAttribute(Procurement.SESSION_ATTR_NAME);
      if (procurement == null) {
        procurement = createNewPrimitiveProcurement(secInfo);
      }
      return procurement;
    }

    public Procurement.PrimitiveProcurement setPrimitiveProcurement(HttpSession session, Procurement.PrimitiveProcurement procurement) {
      session.setAttribute(Procurement.SESSION_ATTR_NAME, procurement);
      return procurement;
  }

}
