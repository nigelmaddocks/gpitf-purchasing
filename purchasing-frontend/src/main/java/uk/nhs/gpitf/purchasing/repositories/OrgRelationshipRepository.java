package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.results.Ids;
import uk.nhs.gpitf.purchasing.repositories.results.OrgAndCountAndSolution;

@Repository
public interface OrgRelationshipRepository extends CrudRepository<OrgRelationship, Long> {
	Iterable<OrgRelationship> findAllByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByChildOrgAndRelationshipType(Organisation childOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByParentOrgAndChildOrgAndRelationshipType(Organisation parentOrg, Organisation childOrg, RelationshipType relationshipType);
	
	@Query("SELECT NEW uk.nhs.gpitf.purchasing.repositories.results.Ids(orgr.childOrg.id) FROM OrgRelationship orgr " + 
			"WHERE orgr.parentOrg = :parentOrg AND orgr.relationshipType = :relationshipType ")
	Iterable<Ids> findAllChildIdsByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType);
	
	@Query("SELECT NEW uk.nhs.gpitf.purchasing.repositories.results.OrgAndCountAndSolution(child.id, child.name, child.orgCode, pc.patientCount, os.solution, ls, os.contractEndDate) " +
			"FROM OrgRelationship orgr " + 
			"INNER JOIN Organisation child ON child.id = orgr.childOrg " +
			"LEFT OUTER JOIN PatientCount pc ON pc.org = orgr.childOrg AND pc.run = (SELECT MAX(id) FROM PatientCountRun)" +
			"LEFT OUTER JOIN OrgSolution os ON os.organisation = orgr.childOrg " +
			"LEFT OUTER JOIN LegacySolution ls ON ls.id = os.legacySolution " +
			"WHERE orgr.parentOrg = :parentOrg AND orgr.relationshipType = :relationshipType " +
			"ORDER BY orgr.childOrg")
	Iterable<OrgAndCountAndSolution> findAllWithCoreSystemByParentOrgAndRelationshipType(@Param("parentOrg") Organisation parentOrg, @Param("relationshipType") RelationshipType relationshipType);

	@Query("SELECT NEW uk.nhs.gpitf.purchasing.repositories.results.OrgAndCountAndSolution(o.id, o.name, o.orgCode, pc.patientCount, os.solution, ls, os.contractEndDate) " +
			"FROM Organisation o " +
			"LEFT OUTER JOIN PatientCount pc ON pc.org = o AND pc.run = (SELECT MAX(id) FROM PatientCountRun)" +
			"LEFT OUTER JOIN OrgSolution os ON os.organisation = o " +
			"LEFT OUTER JOIN LegacySolution ls ON ls.id = os.legacySolution " +
			"WHERE o = :org " +
			"ORDER BY o")
	Iterable<OrgAndCountAndSolution> findAllWithCoreSystemByOrg(@Param("org") Organisation org);

}
