package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.ProcSrvRecipient;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;


@Repository
public interface ProcSrvRecipientRepository extends CrudRepository<ProcSrvRecipient, Long> {
	Iterable<ProcSrvRecipient> findAllByProcurementOrderByOrganisationName(Procurement procurement);

}
