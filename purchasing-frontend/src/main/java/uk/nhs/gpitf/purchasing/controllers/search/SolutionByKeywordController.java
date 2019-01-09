package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.client.model.Capabilities;
import io.swagger.client.model.Solutions;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByKeywordModel;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller 
public class SolutionByKeywordController {
	
	@Autowired
	OnboardingService onboardingService;
	
	@GetMapping("/buyingprocess/solutionByKeyword")
	public String solutionByCapability(Model model, HttpServletRequest request) {
		Breadcrumbs.register("By keyword", request);
		
		SearchSolutionByKeywordModel searchModel = new SearchSolutionByKeywordModel();
		setupModel(null, searchModel);	
		
		model.addAttribute("searchSolutionByKeywordModel", searchModel);

        return "buying-process/searchSolutionByKeyword";
    }	
	
	@PostMapping("/buyingprocess/solutionByKeyword")
	public String solutionByCapabilityPost(@Valid SearchSolutionByKeywordModel searchModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {
		searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
		
		setupModel(bindingResult, searchModel);	
				
        return "buying-process/searchSolutionByKeyword";
    }	

	private void setupModel(BindingResult bindingResult, SearchSolutionByKeywordModel searchModel) {
		if (searchModel.getSolutions() != null) {
			searchModel.setSolutionCapabilities(new Hashtable<>());
			for (Solutions solution : searchModel.getSolutions()) {
				searchModel.getSolutionCapabilities().put(solution, onboardingService.findCapabilitiesBySolutionId(solution.getId()));
			}
		}
	}
	
}
