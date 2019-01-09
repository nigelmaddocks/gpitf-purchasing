package uk.nhs.gpitf.purchasing.models;

import java.util.Hashtable;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.Solutions;
import lombok.Data;

@Data
public class SearchSolutionByKeywordModel {
	
	@Size(min = 3, message="search term must be at least 3 characters")
	private String searchKeywords;
	
	List <Solutions> solutions = null;
	Hashtable <Solutions, List<Capabilities>> solutionCapabilities = null;
	
	private String selectedSolution;

}
