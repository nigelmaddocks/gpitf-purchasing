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
	
	@GetMapping("/solutionByCapability/{csvCapabiities}")
	public String solutionByCapability(@PathVariable String csvCapabiities, Model model) {
		model = setupSolutionByCapability(csvCapabiities, model);	
        return "buying-process/searchSolutionByCapability";
    }	

	private Model setupSolutionByCapability(String csvCapabilities, Model model) {
		SearchSolutionByCapabilityModel myModel = new SearchSolutionByCapabilityModel();
		myModel.setCsvCapabilities(csvCapabilities);
		myModel.setAllCapabilities(onboardingService.orderByCoreThenName(onboardingService.findCapabilities()));
		
		model.addAttribute("myModel", myModel);
		
		return model;
	}
	
}
