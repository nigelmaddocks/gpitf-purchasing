package uk.nhs.gpitf.purchasing.services.buying.process;

import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.services.IProcurementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("stubProcurementService")
public class ProcurementServiceStub implements IProcurementService {

    @Override
    public List<Procurement> getAll() {
        return new ArrayList<>();
    }

    @Override
    public List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(long iOrgContact, long iStatus) {
        Procurement p = new Procurement();
        ProcStatus ps = new ProcStatus();
        ps.setId(80L);
        ps.setName("COMPLETED");
        p.setStatus(ps);
        p.setName("stubbyMcCompletedStubFace");
        List<Procurement> stubList = new ArrayList<>();
        stubList.add(p);
        return stubList;
    }

    @Override
    public List<Procurement> getAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status) {
        return new ArrayList<>();
    }

    @Override
    public List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(long iOrgContact) {
        Procurement p = new Procurement();
        ProcStatus ps = new ProcStatus();
        ps.setId(1L);
        ps.setName("DRAFT");
        p.setStatus(ps);
        p.setName("stubbyMcUncompletedStubFace");
        List<Procurement> stubList = new ArrayList<>();
        stubList.add(p);
        return stubList;
    }

    @Override
    public List<Procurement> getUncompletedByOrgContactOrderByLastUpdated(OrgContact orgContact) {
        return new ArrayList<>();
    }

    @Override
    public Procurement findById(Long procurementId) throws ProcurementNotFoundException {
        return new Procurement();
    }

    @Override
    public Procurement saveCurrentPosition(
    		long procurementId, 
    		long orgContactId, 
    		Optional<String> searchKeyword, 
    		Optional<String> csvCapabilities, 
    		Optional<String> csvInteroperables, 
            Optional<EvaluationTypeEnum> evaluationType,
            Optional<Boolean> singleSiteContinuity,
    		Optional<Boolean> foundation, 
    		Optional<String> csvPractices) throws Exception {
        return new Procurement();
    }

    @Override
    public Procurement save(Procurement procurement) {
        return new Procurement();
    }

}