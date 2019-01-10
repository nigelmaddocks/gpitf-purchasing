package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface ProcurementRepository extends CrudRepository<Procurement, Long> {
	Iterable<Procurement> findAllByOrgContactOrderByLastUpdated(OrgContact orgContact);
	
	@Query("SELECT proc FROM Procurement proc WHERE NOT proc.status IN (" + ProcStatus.COMPLETED + "," + ProcStatus.DELETED  + ") AND proc.orgContact = :orgContact ORDER BY proc.lastUpdated DESC")
	List<Procurement> findUncompletedByOrgContactOrderByLastUpdated(@Param("orgContact") OrgContact orgContact);
	
	Iterable<Procurement> findAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status);

}
