package uk.nhs.gpitf.purchasing.repositories.results;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Setter;
import uk.nhs.gpitf.purchasing.entities.LegacySolution;
import uk.nhs.gpitf.purchasing.utils.GUtils;

/** For results returned from OrgRelationshipRepository.findAllWithCoreSystemByParentOrgAndRelationshipType */
public class OrgAndCountAndSolutions {
	public long organisationId;
	public String organisationName;
	public String organisationNameProperCase;
	public String organisationCode;
	public Integer patientCount;
	public String htmlFormattedSolutions;
	
	@Setter(AccessLevel.PUBLIC)
	public SolutionDetail[] solutions = new SolutionDetail[] {};
	public void setSolutions(SolutionDetail[] value) {
		this.solutions = value;
		this.htmlFormattedSolutions = htmlFormatSolutions();
	}
	
	public String htmlFormatSolutions() {
		String s = "";
		for (var solution : solutions) {
			s += solution.formattedSolution + "<br />";
		}
		return s;
	}
	
	public OrgAndCountAndSolutions(long organisationId, String organisationName, String organisationCode, Integer patientCount) {
		//this.orgRel = orgRel;
		this.organisationId = organisationId;
		this.organisationName = organisationName;
		this.organisationNameProperCase = GUtils.getCapitalized(organisationName);
		this.organisationCode = organisationCode;
		this.patientCount = patientCount;
	}
	
	public static class SolutionDetail {
		public String solutionId;
		public LegacySolution legacySolution;
		public String solutionName;
		public String solutionSupplierName;
		public String formattedSolution;
		public LocalDate contractEndDate;
		
		public SolutionDetail(LegacySolution legacySolution, LocalDate contractEndDate) {
			this.legacySolution = legacySolution;
			this.solutionName = GUtils.getCapitalized(legacySolution.getName());
			this.solutionSupplierName = GUtils.getCapitalized(legacySolution.getSupplier().getName());
			this.contractEndDate = contractEndDate;
			formattedSolution = formatSolution();
		}
		
		public SolutionDetail(String solutionId, LocalDate contractEndDate) {
			this.solutionId = solutionId;
			formattedSolution = formatSolution();
		}
		
		public String formatSolution() {
			if (StringUtils.isNotEmpty(solutionId)) {
				return solutionId;
			}
			if (solutionName == null) {
				return "";
			}
			String s = solutionName + " (" + solutionSupplierName + ")"  +
					(contractEndDate == null ? "":" [" + contractEndDate + "]");
			return s;
		}
	}
}
