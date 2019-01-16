package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByCapabilityModel;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class SolutionByCapabilityController {
	
	@Autowired
	OnboardingService onboardingService;
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	ProcurementService procurementService;
    
    private static final Logger logger = LoggerFactory.getLogger(SolutionByCapabilityController.class);
/*	
	@GetMapping("/buyingprocess/solutionByCapability/{csvCapabilities}")
	public String solutionByCapability(@PathVariable String csvCapabiities, Model model, HttpServletRequest request) {

		model = setupSolutionByCapability(null, csvCapabiities, model);	
        return "buying-process/searchSolutionByCapability";
    }	
*/	
	@GetMapping(value = {"/buyingprocess/solutionByCapability/{csvCapabilities}", "/buyingprocess/{optProcurementId}/solutionByCapability/{csvCapabilities}"})
	public String solutionByCapability(@PathVariable Optional<Long> optProcurementId, @PathVariable String csvCapabilities, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("By capability", request);

		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		Long procurementId = null;
		
		if (optProcurementId.isPresent()) {
			procurementId = optProcurementId.get();
			if (procurementId != 0) {
				Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
				if (optProcurement.isPresent()) {
					Procurement procurement = optProcurement.get();
					
					// Check that the user is authorised to this procurement
					if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
					 && !secInfo.isAdministrator()) {
			        	String message = "view procurement " + procurementId;
			    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
			    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
			        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
					}
					try {
						procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), Optional.of(csvCapabilities));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}		

		model = setupSolutionByCapability(procurementId, csvCapabilities, model);	
        return "buying-process/searchSolutionByCapability";
    }	

	private Model setupSolutionByCapability(Long procurementId, String csvCapabilities, Model model) {
		SearchSolutionByCapabilityModel myModel = new SearchSolutionByCapabilityModel();
		myModel.setProcurementId(procurementId);
		myModel.setCsvCapabilities(csvCapabilities);
		myModel.setAllCapabilities(onboardingService.orderByCoreThenName(onboardingService.findCapabilities()));
		
		model.addAttribute("myModel", myModel);
		
		return model;
	}
	
}
