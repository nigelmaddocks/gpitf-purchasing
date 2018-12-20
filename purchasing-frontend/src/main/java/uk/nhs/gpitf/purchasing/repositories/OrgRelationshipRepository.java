package uk.nhs.gpitf.purchasing.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.nhs.gpitf.purchasing.entities.*;

@Repository
public interface OrgRelationshipRepository extends CrudRepository<OrgRelationship, Long> {
	Iterable<OrgRelationship> findAllByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByChildOrgAndRelationshipType(Organisation childOrg, RelationshipType relationshipType);
	Iterable<OrgRelationship> findAllByParentOrgAndChildOrgAndRelationshipType(Organisation parentOrg, Organisation childOrg, RelationshipType relationshipType);
}
