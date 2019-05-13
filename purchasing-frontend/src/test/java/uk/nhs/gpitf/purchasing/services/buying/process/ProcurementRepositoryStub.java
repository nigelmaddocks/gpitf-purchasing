package uk.nhs.gpitf.purchasing.services.buying.process;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;

import java.util.List;
import java.util.Optional;

@Component("procurementrepositoryStub")
public class ProcurementRepositoryStub implements ProcurementRepository {

    @Override
    public Iterable<Procurement> findAllByOrgContactOrderByLastUpdated(OrgContact orgContact) {
        return null;
    }

    @Override
    public List<Procurement> findUncompletedByOrgContactOrderByLastUpdated(OrgContact orgContact) {
        return null;
    }

    @Override
    public Iterable<Procurement> findAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status) {
        return null;
    }

    @Override
    public <S extends Procurement> S save(S s) {
        return null;
    }

    @Override
    public <S extends Procurement> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Procurement> findById(Long id) {
        Procurement procurement = new Procurement();
        if(id==1L) {
            CompetitionType onCat = new CompetitionType();
            onCat.setId(1);
            procurement.setCompetitionType(onCat);
            procurement.setCsvCapabilities(StringUtils.EMPTY);
            procurement.setCsvInteroperables(StringUtils.EMPTY);
            procurement.setFoundation(true);
        } else {
            CompetitionType offCat = new CompetitionType();
            offCat.setId(2);
            procurement.setCompetitionType(offCat);
            procurement.setCsvCapabilities(StringUtils.EMPTY);
            procurement.setCsvInteroperables(StringUtils.EMPTY);
            procurement.setFoundation(true);
        }
        OrgContact orgContact = new OrgContact();
        Organisation organisation = new Organisation();
        organisation.setId(0);
        orgContact.setOrganisation(organisation);
        procurement.setOrgContact(orgContact);
        ProcStatus procStatus = new ProcStatus();
        procStatus.setId(1L);
        procurement.setStatus(procStatus);
        return Optional.of(procurement);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Procurement> findAll() {
        return null;
    }

    @Override
    public Iterable<Procurement> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Procurement procurement) {

    }

    @Override
    public void deleteAll(Iterable<? extends Procurement> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
