package uk.nhs.gpitf.purchasing.services;

import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IProcurementService {
    List<Procurement> getAll();
    List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(long iOrgContact, long iStatus);
    List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status);
    List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(long iOrgContact);
    List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(OrgContact orgContact);
    Procurement findById(Long procurementId) throws ProcurementNotFoundException;
    Procurement saveCurrentPosition(
    		long procurementId, 
    		long orgContactId, 
    		Optional<String> searchKeyword, 
    		Optional<String> csvCapabilities, 
    		Optional<String> csvInteroperables, 
            Optional<EvaluationTypeEnum> evaluationType,
            Optional<Boolean> singleSiteContinuity,
    		Optional<Boolean> foundation, 
    		Optional<String> csvPractices) throws Exception;
    Procurement save(Procurement procurement);
}
