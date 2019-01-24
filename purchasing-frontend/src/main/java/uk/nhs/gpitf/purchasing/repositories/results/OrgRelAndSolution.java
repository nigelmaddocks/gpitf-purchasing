package uk.nhs.gpitf.purchasing.repositories.results;

import java.util.Optional;

import uk.nhs.gpitf.purchasing.entities.LegacySolution;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.utils.GUtils;

/** For results returned from OrgRelationshipRepository.findAllWithCoreSystemByParentOrgAndRelationshipType */
public class OrgRelAndSolution {
	public OrgRelationship orgRel;
	public String solutionId;
	public LegacySolution legacySolution;
	public long organisationId;
	public String organisationName;
	public String organisationNameProperCase;
	public String organisationCode;
	public long patientCount;
	public String solutionName;
	public String solutionSupplierName;
	public String formattedSolution;
	
	public OrgRelAndSolution(OrgRelationship orgRel) {
		this.orgRel = orgRel;
		organisationId = this.orgRel.getChildOrg().getId();
		organisationName = this.orgRel.getChildOrg().getName();
		organisationNameProperCase = this.orgRel.getChildOrg().getNameProperCase();
		organisationCode = this.orgRel.getChildOrg().getOrgCode();
		patientCount = this.orgRel.getChildOrg().getPatientCount();
		formattedSolution = formatSolution();
	}
	
	public OrgRelAndSolution(OrgRelationship orgRel, String solutionId, LegacySolution legacySolution) {
		this.orgRel = orgRel;
		organisationId = this.orgRel.getChildOrg().getId();
		organisationName = this.orgRel.getChildOrg().getName();
		organisationNameProperCase = this.orgRel.getChildOrg().getNameProperCase();
		organisationCode = this.orgRel.getChildOrg().getOrgCode();
		patientCount = this.orgRel.getChildOrg().getPatientCount();
		this.solutionId = solutionId;
		this.legacySolution = legacySolution;
		if (this.legacySolution != null) {
			solutionName = GUtils.getCapitalized(this.legacySolution.getName());
			solutionSupplierName = GUtils.getCapitalized(this.legacySolution.getSupplier().getName());
		}
		formattedSolution = formatSolution();
	}
	
	public String formatSolution() {
		if (solutionName == null) {
			return "";
		}
		return solutionName + " (" + solutionSupplierName + ")";
	}
}
