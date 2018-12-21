package uk.nhs.gpitf.purchasing.models;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.Organisation;

@Data
public class LoginOrgSelectionModel {
	private Organisation[] organisations;
	
	private long selectedOrganisation;
}
