package uk.nhs.gpitf.purchasing.services.buying.process;

import org.springframework.stereotype.Component;
import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component("orgContactRepositoryStub")
public class OrgContactRepositoryStub implements OrgContactRepository {


    @Override
    public Iterable<OrgContact> findAllByOrganisationAndDeletedOrderByContactSurnameAscContactForenameAsc(Organisation organisation, boolean deleted) {
        return null;
    }

    @Override
    public Optional<OrgContact> findByOrganisationAndContact(Organisation organisation, Contact contact) {
        return Optional.empty();
    }

    @Override
    public Iterable<OrgContact> findAllByContactAndDeleted(Contact contact, boolean deleted) {
        Iterable i = () -> new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return new Object();
            }
        };
        return i;
    }

    @Override
    public List<Object> findById(int intValue) {
        return new ArrayList<>();
    }

    @Override
    public <S extends OrgContact> S save(S s) {
        return null;
    }

    @Override
    public <S extends OrgContact> Iterable<S> saveAll(Iterable<S> iterable) {
        Iterable i = () -> new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return new Object();
            }
        };
        return i;
    }

    @Override
    public Optional<OrgContact> findById(Long aLong) {
        OrgContact orgContact = new OrgContact();
        orgContact.setId(1L);
        orgContact.setDeleted(false);
        return Optional.of(orgContact);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<OrgContact> findAll() {
        Iterable i = () -> new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return new Object();
            }
        };
        return i;
    }

    @Override
    public Iterable<OrgContact> findAllById(Iterable<Long> iterable) {
        Iterable i = () -> new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
            @Override
            public Object next() {
                return new Object();
            }
        };
        return i;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(OrgContact orgContact) {

    }

    @Override
    public void deleteAll(Iterable<? extends OrgContact> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
