package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository thisRepository;

    public List<Organisation> getAll() {
        List<Organisation> coll = new ArrayList<>();
        thisRepository.findAll().forEach(coll::add);
        return coll;
    }    

    public List<Organisation> getAllByOrgType(long iOrgType) {
        OrgType orgType = new OrgType();
        orgType.setId(iOrgType);
        
        List<Organisation> coll = new ArrayList<>();
        thisRepository.findAllByOrgTypeOrderByName(orgType).forEach(coll::add);
        return coll;
    }    
/*
    public List<Organisation> getAllByTypeAndName(long iOrgType, String sOrgNamePattern) {
        OrgType orgType = new OrgType();
        orgType.setId(iOrgType);
        
        //List<Organisation> coll = new ArrayList<>();
        //thisRepository.findAllByOrgTypeAndOrgNameOrderByName(orgType, sOrgNamePattern).forEach(coll::add);
        List<Organisation> coll = thisRepository.findAllByOrgTypeAndOrgNameOrderByName(orgType, sOrgNamePattern);
        return coll;
    }    
*/    
    

    public List<OrgRelationship> getOrgsAndParentsByRelTypeAndChildNameOrderByChildName(long iRelType, String sOrgNamePattern) {
        RelationshipType relType = new RelationshipType();
        relType.setId(iRelType);
        
    	List<OrgRelationship> coll = thisRepository.findOrgAndParentsByRelTypeAndChildNameOrderByChildName(relType, sOrgNamePattern);
        return coll;
    }    

    public List<OrgRelationship> getOrgsAndParentsByRelTypeAndChildOrgTypeOrderByChildName(long iRelType, String sOrgCode) {
        RelationshipType relType = new RelationshipType();
        relType.setId(iRelType);
        
    	List<OrgRelationship> coll = thisRepository.findOrgAndParentsByRelTypeAndChildOrgCodeOrderByChildName(relType, sOrgCode);
        return coll;
    }    

    public List<OrgRelationship> getOrgsAndParentsByRelTypeAndParentNameOrderByChildName(long iRelType, String sOrgNamePattern) {
        RelationshipType relType = new RelationshipType();
        relType.setId(iRelType);
        
    	List<OrgRelationship> coll = thisRepository.findOrgAndParentsByRelTypeAndParentNameOrderByChildName(relType, sOrgNamePattern);
        return coll;
    }    

    public List<OrgRelationship> getOrgsAndParentsByRelTypeAndParentOrgTypeOrderByChildName(long iRelType, String sOrgCode) {
        RelationshipType relType = new RelationshipType();
        relType.setId(iRelType);
        
    	List<OrgRelationship> coll = thisRepository.findOrgAndParentsByRelTypeAndParentOrgCodeOrderByChildName(relType, sOrgCode);
        return coll;
    }    


/*
    public List<Organisation> findAllByOrgTypeAndOrgNameLikeOrderByName(long iOrgType, ) {
        OrgType orgType = new OrgType();
        orgType.setId(iOrgType);
        
        List<Organisation> coll = new ArrayList<>();
        thisRepository.findAllByOrgTypeOrderByName(orgType).forEach(coll::add);
        return coll;
    }    
*/
    public long getPatientCountForOrganisationsInList(String csvOrgIds) {
    	if (csvOrgIds == null || csvOrgIds.trim().length() == 0) {
    		return 0L;
    	}
    	String[] arrStringCsvOrgId = csvOrgIds.split(",");
    	ArrayList<Long> arlOrgIds = new ArrayList<>();
    	for (String sOrgId : arrStringCsvOrgId) {
    		arlOrgIds.add(Long.valueOf(sOrgId));
    	}
    	Object object = thisRepository.getPatientCountForIds(arlOrgIds);
    	
    	return (long)object;
    }
}
