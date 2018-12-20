package uk.nhs.gpitf.purchasing.controllers.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uk.nhs.gpitf.purchasing.models.SearchSolutionByCapabilityModel;
import uk.nhs.gpitf.purchasing.services.OnboardingService;

@Controller 
public class SolutionByCapabilityController {
	
	@Autowired
	OnboardingService onboardingService;
	
	@GetMapping("/solutionByCapability/{search}")
	public String solutionByCapability(@PathVariable String search, Model model) {
		model = setupSolutionByCapability(search, model);	
        return "searchSolutionByCapability";
    }	

	private Model setupSolutionByCapability(String search, Model model) {
		SearchSolutionByCapabilityModel myModel = new SearchSolutionByCapabilityModel();
		myModel.setAllCapabilities(onboardingService.orderByCoreThenName(onboardingService.findCapabilities()));
		
		model.addAttribute("myModel", myModel);
		
		return model;
	}
	
}
