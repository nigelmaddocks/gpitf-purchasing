package uk.nhs.gpitf.purchasing.repositories.results;

import uk.nhs.gpitf.purchasing.entities.LegacySolution;
import uk.nhs.gpitf.purchasing.utils.GUtils;

/** For results returned from OrgRelationshipRepository.findAllWithCoreSystemByParentOrgAndRelationshipType */
public class OrgAndCountAndSolution {
	public String solutionId;
	public LegacySolution legacySolution;
	public long organisationId;
	public String organisationName;
	public String organisationNameProperCase;
	public String organisationCode;
	public Integer patientCount;
	public String solutionName;
	public String solutionSupplierName;
	public String formattedSolution;
	
	public OrgAndCountAndSolution(long organisationId, String organisationName, String organisationCode, Integer patientCount, String solutionId, LegacySolution legacySolution) {
		//this.orgRel = orgRel;
		this.organisationId = organisationId;
		this.organisationName = organisationName;
		this.organisationNameProperCase = GUtils.getCapitalized(organisationName);
		this.organisationCode = organisationCode;
		this.patientCount = patientCount;
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
