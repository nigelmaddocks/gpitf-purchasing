package uk.nhs.gpitf.purchasing.models;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import lombok.Data;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;

@Data
public class OrganisationSearchModel {
	
	@Valid
	private Criteria criteria;
	
	private List<OrgRelationship> orgRels = null;
	
	@Data
	public static class Criteria implements Serializable {
		
		@Pattern(regexp="(^$|.{6,30})", message="Length should be between 6 and 30")
		private String gpCode;
		
		@Pattern(regexp="(^$|.{6,30})", message="Length should be between 6 and 30")
		private String gpName;
		
		@Pattern(regexp="(^$|.{3,30})", message="Length should be between 3 and 30")
		private String ccgCode;
		
		@Pattern(regexp="(^$|.{4,30})", message="Length should be between 4 and 30")
		private String ccgName;
		
	}
	
}
