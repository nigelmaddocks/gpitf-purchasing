package uk.nhs.gpitf.purchasing.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContactRole;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.Role;

@Data
public class OrgContactModel {
	
	private long id;
	
	@Valid
	private Contact contact;
	
	private Organisation organisation;
	
	private List<OrgContactRole> orgContactRoles = new ArrayList<>();
	
	private List<Role> potentialRoles = new ArrayList<>();
	
	private String[] deleteOrgContactRoles;
	
	private long newRoleId;
}
