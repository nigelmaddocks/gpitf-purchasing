package uk.nhs.gpitf.purchasing.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.results.OrgRelAndSolution;

@Repository
public interface OrgRelationshipRepository extends CrudRepository<OrgRelationship, Long> {
	Iterable<OrgRelationship> findAllByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByChildOrgAndRelationshipType(Organisation childOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByParentOrgAndChildOrgAndRelationshipType(Organisation parentOrg, Organisation childOrg, RelationshipType relationshipType);
	
	
//	@Query("SELECT NEW uk.nhs.gpitf.purchasing.repositories.results.OrgRelAndSolution(orgr) FROM OrgRelationship orgr " + 
//			"LEFT OUTER JOIN OrgSolution os ON os.organisation = orgr.childOrg " +
//			"WHERE orgr.parentOrg = :parentOrg AND orgr.relationshipType = :relationshipType " +
//			"ORDER BY orgr.childOrg")
	
	@Query("SELECT NEW uk.nhs.gpitf.purchasing.repositories.results.OrgRelAndSolution(orgr, os.solution, ls) FROM OrgRelationship orgr " + 
			"LEFT OUTER JOIN OrgSolution os ON os.organisation = orgr.childOrg " +
			"LEFT OUTER JOIN LegacySolution ls ON ls.id = os.legacySolution " +
			"WHERE orgr.parentOrg = :parentOrg AND orgr.relationshipType = :relationshipType " +
			"ORDER BY orgr.childOrg")
	Iterable<OrgRelAndSolution> findAllWithCoreSystemByParentOrgAndRelationshipType(@Param("parentOrg") Organisation parentOrg, @Param("relationshipType") RelationshipType relationshipType);

}
