package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository("orgContactRepository")
public interface OrgContactRepository extends CrudRepository<OrgContact, Long> {
	List<Object> findById(int intValue);
	Iterable<OrgContact> findAllByOrganisationAndDeletedOrderByContactSurnameAscContactForenameAsc(Organisation organisation, boolean deleted);
	Optional<OrgContact> findByOrganisationAndContact(Organisation organisation, Contact contact);
	Iterable<OrgContact> findAllByContactAndDeleted(Contact contact, boolean deleted);
}
