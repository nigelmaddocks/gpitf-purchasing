package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository("procurementRepository")
public interface ProcurementRepository extends CrudRepository<Procurement, Long> {

	Optional<Procurement> findById(Long id);

	Iterable<Procurement> findAllByOrgContactOrderByLastUpdated(OrgContact orgContact);
	
	@Query("SELECT proc FROM Procurement proc WHERE NOT proc.status IN (" + ProcStatus.COMPLETED + "," + ProcStatus.DELETED  + ") AND proc.orgContact = :orgContact ORDER BY proc.lastUpdated DESC")
	List<Procurement> findUncompletedByOrgContactOrderByLastUpdated(@Param("orgContact") OrgContact orgContact);
	
	Iterable<Procurement> findAllByOrgContactAndStatusOrderByLastUpdatedDesc(OrgContact orgContact, ProcStatus status);

}
