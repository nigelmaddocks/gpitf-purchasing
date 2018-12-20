package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
	Iterable<Organisation> findAllByOrgTypeOrderByName(OrgType orgType);
	Optional<Organisation> findByOrgCode(String orgCode);
	
	@Query("SELECT orgrel FROM OrgRelationship orgrel INNER JOIN Organisation o on o.id = orgrel.childOrg WHERE o.name LIKE CONCAT('%',:orgNamePattern,'%') AND orgrel.relationshipType = :relType ORDER BY o.name")
	List<OrgRelationship> findOrgAndParentsByRelTypeAndChildNameOrderByChildName(@Param("relType") RelationshipType relType, @Param("orgNamePattern") String orgNamePattern);
	
	@Query("SELECT orgrel FROM OrgRelationship orgrel INNER JOIN Organisation o on o.id = orgrel.childOrg WHERE o.orgCode = :orgCode AND orgrel.relationshipType = :relType ORDER BY o.name")
	List<OrgRelationship> findOrgAndParentsByRelTypeAndChildOrgCodeOrderByChildName(@Param("relType") RelationshipType relType, @Param("orgCode") String orgCode);
	
	@Query("SELECT orgrel FROM OrgRelationship orgrel INNER JOIN Organisation op on op.id = orgrel.parentOrg INNER JOIN Organisation o on o.id = orgrel.childOrg WHERE op.name LIKE CONCAT('%',:orgNamePattern,'%') AND orgrel.relationshipType = :relType ORDER BY o.name")
	List<OrgRelationship> findOrgAndParentsByRelTypeAndParentNameOrderByChildName(@Param("relType") RelationshipType relType, @Param("orgNamePattern") String orgNamePattern);
	
	@Query("SELECT orgrel FROM OrgRelationship orgrel INNER JOIN Organisation op on op.id = orgrel.parentOrg INNER JOIN Organisation o on o.id = orgrel.childOrg WHERE op.orgCode = :orgCode AND orgrel.relationshipType = :relType ORDER BY o.name")
	List<OrgRelationship> findOrgAndParentsByRelTypeAndParentOrgCodeOrderByChildName(@Param("relType") RelationshipType relType, @Param("orgCode") String orgCode);

}
