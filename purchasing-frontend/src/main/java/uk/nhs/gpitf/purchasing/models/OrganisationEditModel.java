package uk.nhs.gpitf.purchasing.models;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.Organisation;

@Data
public class OrganisationEditModel {
	
	@Valid
	private Organisation organisation;
	
	private List<OrgRelationship> parentOrgRelationships;
	
	private List<Organisation> potentialParentOrgs;
	
	private List<OrgContact> orgContacts;
	
	private long newParentOrgId;
	
	private Contact newContact;
	
	private String[] deleteOrgRelationships;
	
	private String[] deleteContacts;
	
	private boolean canAdminister;
}
