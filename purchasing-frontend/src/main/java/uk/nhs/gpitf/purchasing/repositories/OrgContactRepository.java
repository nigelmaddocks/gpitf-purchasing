package uk.nhs.gpitf.purchasing.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface OrgContactRepository extends CrudRepository<OrgContact, Long> {
	Iterable<OrgContact> findAllByOrganisationAndDeletedOrderByContactSurnameAscContactForenameAsc(Organisation organisation, boolean deleted);
	Optional<OrgContact> findByOrganisationAndContact(Organisation organisation, Contact contact);
}
