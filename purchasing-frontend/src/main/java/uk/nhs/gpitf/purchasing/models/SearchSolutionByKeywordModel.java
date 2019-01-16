package uk.nhs.gpitf.purchasing.models;

import java.util.Hashtable;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.Solutions;
import lombok.Data;

@Data
public class SearchSolutionByKeywordModel {
	
	private Long procurementId;
	
	@Size(min = 3, message="search term must be at least 3 characters")
	private String searchKeywords;
	
	List <Solutions> solutions = null;
	Hashtable <Solutions, List<Capabilities>> solutionCapabilities = null;
	
	private String selectedSolution;

	/** 
	 * Returns the capability ids as csv for each solution
	 */
	public Hashtable<Solutions, String> getSolutionCapabilitiesCsv() {
		Hashtable<Solutions, String> hshRtn = new Hashtable<>();
		if (solutionCapabilities == null) {
			return hshRtn;
		}
		
		for (Solutions sol : solutionCapabilities.keySet()) {
			var capabilities = solutionCapabilities.get(sol);
			String csv = "";
			for (Capabilities cap : capabilities) {
				csv += "," + cap.getId();
			}
			if (csv.length() > 0) {
				csv = csv.substring(1);
			}
			hshRtn.put(sol, csv);
		}
		return hshRtn;
	}
}
