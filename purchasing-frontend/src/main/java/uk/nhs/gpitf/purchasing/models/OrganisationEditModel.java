package uk.nhs.gpitf.purchasing.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.Role;

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
/*	
	public String rolesToString(OrgContact orgContact) {
		ArrayList<Role> arlRoles = new ArrayList<>();
		for (var orc : orgContact.getOrgContactRoles()) {
			if (!orc.isDeleted()) {
				arlRoles.add(orc.getRole());
			}
		}
		String sRoles = "";
		for (var role : arlRoles) {
			sRoles += ", " + role.getName();
		}
		if (sRoles.length() > 2) {
			sRoles = sRoles.substring(2);
		} else {
			sRoles = "none";
		}
		return sRoles;
	}
*/	
}
