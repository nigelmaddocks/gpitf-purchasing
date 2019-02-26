package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;
import uk.nhs.gpitf.purchasing.repositories.results.Ids;
import uk.nhs.gpitf.purchasing.repositories.results.OrgAndCountAndSolution;
import uk.nhs.gpitf.purchasing.utils.GUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrgRelationshipService {

    @Autowired
    private OrgRelationshipRepository thisRepository;

    public List<OrgRelationship> getAll() {
        List<OrgRelationship> coll = new ArrayList<>();
        thisRepository.findAll().forEach(coll::add);
        return coll;
    }    

    public List<OrgRelationship> getAllByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType) {
        List<OrgRelationship> coll = new ArrayList<>();
        thisRepository.findAllByParentOrgAndRelationshipType(parentOrg, relationshipType).forEach(coll::add);
        return coll;
    }    

    public List<OrgRelationship> getAllByChildOrgAndRelationshipType(Organisation childOrg, RelationshipType relationshipType) {
        List<OrgRelationship> coll = new ArrayList<>();
        thisRepository.findAllByChildOrgAndRelationshipType(childOrg, relationshipType).forEach(coll::add);
        return coll;
    }    

    /**
     * Returns Organisations for a parent Organisation via a relationship type ordered by Name and OrgCode
     * @param parentOrg
     * @param relationshipType
     */
    public List<Organisation> getOrganisationsByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType) {
        List<Organisation> coll = new ArrayList<>();
        for (OrgRelationship orgRel : thisRepository.findAllByParentOrgAndRelationshipType(parentOrg, relationshipType)) {
        	coll.add(orgRel.getChildOrg());
        }
        coll.sort((object1, object2) -> (object1.getName()+object1.getOrgCode()).compareToIgnoreCase(object2.getName()+object2.getOrgCode()));
        return coll;
    }    

    /** Returns a collection of child Ids for the parent and relationship type.
     * This is intended to be a quick database call without any object lookups.
     */
    public List<Ids> getChildIdsByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType) {
        List<Ids> coll = new ArrayList<>();
        thisRepository.findAllChildIdsByParentOrgAndRelationshipType(parentOrg, relationshipType).forEach(coll::add);
        return coll;
    }
    
    /**
     * Returns Organisations and Core System for a parent Organisation via a relationship type ordered by Name and OrgCode
     * @param parentOrg
     * @param relationshipType
     */
    public List<OrgAndCountAndSolution> getOrganisationsCoreSystemByParentOrgAndRelationshipType(Organisation parentOrg, RelationshipType relationshipType,
    		Optional<String> optFilterByName, Optional<String> optFilterByCode, Optional<String> optFilterBySystem ) {
        List<OrgAndCountAndSolution> coll = new ArrayList<>();
        List<OrgAndCountAndSolution> collCCG = new ArrayList<>();
        thisRepository.findAllWithCoreSystemByParentOrgAndRelationshipType(parentOrg, relationshipType).forEach(coll::add);
        if (optFilterByName.isPresent()) {
        	coll.removeIf(e -> !GUtils.nullToString(e.organisationName).toUpperCase().contains(optFilterByName.get().toUpperCase()));
        }
        if (optFilterByCode.isPresent()) {
        	coll.removeIf(e -> !GUtils.nullToString(e.organisationCode).toUpperCase().contains(optFilterByCode.get().toUpperCase()));
        }
        if (optFilterBySystem.isPresent()) {
        	coll.removeIf(e -> !GUtils.nullToString(e.formatSolution()).toUpperCase().contains(optFilterBySystem.get().toUpperCase()));
        }
        coll.sort((object1, object2) -> (object1.organisationName+object1.organisationCode).compareToIgnoreCase(object2.organisationName+object2.organisationCode));
        
        thisRepository.findAllWithCoreSystemByOrg(parentOrg).forEach(collCCG::add);
        if (optFilterByName.isPresent()) {
        	collCCG.removeIf(e -> !GUtils.nullToString(e.organisationName).toUpperCase().contains(optFilterByName.get().toUpperCase()));
        }
        if (optFilterByCode.isPresent()) {
        	collCCG.removeIf(e -> !GUtils.nullToString(e.organisationCode).toUpperCase().contains(optFilterByCode.get().toUpperCase()));
        }
        if (optFilterBySystem.isPresent()) {
        	collCCG.removeIf(e -> !GUtils.nullToString(e.formatSolution()).toUpperCase().contains(optFilterBySystem.get().toUpperCase()));
        }
        
        collCCG.addAll(coll);
        return collCCG;
    }    

}
