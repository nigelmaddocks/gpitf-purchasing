package uk.nhs.gpitf.purchasing.endpoints;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.repositories.results.OrgAndCountAndSolution;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrgTypeService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.RelationshipTypeService;

@RestController
public class OrganisationDataController {
	
    @Autowired
    private OrganisationService organisationService;
    
    @Autowired
    private OrgTypeService orgTypeService;
    
    @Autowired
    private OrgRelationshipService orgRelationshipService;
    
    @Autowired
    private RelationshipTypeService relationshipTypeService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/organisationData/all")
    public List<Organisation> getAllOrganisations() {
    	List<Organisation> list;
    	list = organisationService.getAll();
    	return list;
    }

    @GetMapping(value = "/organisationData/{parentOrg}/{relationshipType}")
    public List<Organisation> getOrganisationsByParentOrgAndRelationshipType(
    		@PathVariable("parentOrg") long parentOrgId,
    		@PathVariable("relationshipType") long relationshipTypeId
    		) {
    	
    	objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    	
    	List<Organisation> list;
    	Organisation parentOrg = new Organisation();
    	parentOrg.setId(parentOrgId);
    	RelationshipType relationshipType = new RelationshipType();
    	relationshipType.setId(relationshipTypeId);
    	list = orgRelationshipService.getOrganisationsByParentOrgAndRelationshipType(parentOrg, relationshipType);
    	return list;
    }

    @GetMapping(value = "/organisationCoreSystemData/{parentOrg}/{relationshipType}")
    public List<OrgAndCountAndSolution> getOrganisationsAndCoreSystemByParentOrgAndRelationshipType(
    		@PathVariable("parentOrg") long parentOrgId,
    		@PathVariable("relationshipType") long relationshipTypeId,
    		@RequestParam(value = "filterByName", defaultValue = "") String filterByName,
    		@RequestParam(value = "filterByCode", defaultValue = "") String filterByCode,
    		@RequestParam(value = "filterBySystem", defaultValue = "") String filterBySystem
    		) {
    	
    	objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    	
    	List<OrgAndCountAndSolution> list;
    	Organisation parentOrg = new Organisation();
    	parentOrg.setId(parentOrgId);
    	RelationshipType relationshipType = new RelationshipType();
    	relationshipType.setId(relationshipTypeId);
    	list = orgRelationshipService.getOrganisationsCoreSystemByParentOrgAndRelationshipType(parentOrg, relationshipType,
    			(filterByName.length()>0?Optional.of(filterByName):Optional.empty()),
    			(filterByCode.length()>0?Optional.of(filterByCode):Optional.empty()),
    			(filterBySystem.length()>0?Optional.of(filterBySystem):Optional.empty())
    	);
    	return list;
    }
    
    @GetMapping(value = "/orgType/all")
    public List<OrgType> getAllOrgTypes() {
    	List<OrgType> list;
    	list = orgTypeService.getAll();
    	return list;
    }

    @GetMapping(value = "/orgRelationship/all")
    public List<OrgRelationship> getAllOrgRelationships() {
    	List<OrgRelationship> list;
    	list = orgRelationshipService.getAll();
    	return list;
    }    

    @GetMapping(value = "/orgRelationship/{parentOrg}/{relationshipType}")
    public List<OrgRelationship> getOrgRelationshipsByParentOrgAndRelationshipType(
    		@PathVariable("parentOrg") long parentOrgId,
    		@PathVariable("relationshipType") long relationshipTypeId
    		) {
    	List<OrgRelationship> list;
    	Organisation parentOrg = new Organisation();
    	parentOrg.setId(parentOrgId);
    	RelationshipType relationshipType = new RelationshipType();
    	relationshipType.setId(relationshipTypeId);
    	list = orgRelationshipService.getAllByParentOrgAndRelationshipType(parentOrg, relationshipType);
    	return list;
    }    

    @GetMapping(value = "/relationshipType/all")
    public List<RelationshipType> getAllRelationshipTypes() {
    	List<RelationshipType> list;
    	list = relationshipTypeService.getAll();
    	return list;
    }

}
