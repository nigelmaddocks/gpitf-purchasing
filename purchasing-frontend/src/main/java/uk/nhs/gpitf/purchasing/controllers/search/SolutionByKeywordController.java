package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.Hashtable;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.client.model.Solutions;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByKeywordModel;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class SolutionByKeywordController {
	
	@Autowired
	OnboardingService onboardingService;
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	ProcurementService procurementService;
    
    private static final Logger logger = LoggerFactory.getLogger(SolutionByKeywordController.class);
	
	/**
	 * GET: A keyword search doesn't persist to a procurement
	 */
	@GetMapping("/buyingprocess/solutionByKeyword/{searchKeyword}")
	public String solutionByCapability(@PathVariable String searchKeyword, Model model, HttpServletRequest request) {
		Breadcrumbs.register("By keyword", request);
		
		SearchSolutionByKeywordModel searchModel = new SearchSolutionByKeywordModel();
		if (searchKeyword != null && searchKeyword.trim().length() > 0) {
			searchModel.setSearchKeywords(searchKeyword);
			searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
		}
		
		setupModel(null, searchModel);	
		
		model.addAttribute("searchSolutionByKeywordModel", searchModel);

        return "buying-process/searchSolutionByKeyword";
    }	
	
	/**
	 * GET: A keyword search that persists to a procurement
	 */
	@GetMapping("/buyingprocess/{procurementId}/solutionByKeyword")
	public String solutionByCapability(@PathVariable Long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("By keyword", request);
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		SearchSolutionByKeywordModel searchModel = new SearchSolutionByKeywordModel();
		searchModel.setProcurementId(procurementId);
		
		if (procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				searchModel.setProcurement(procurement);
				
				// Check that the user is authorised to this procurement
				if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
				 && !secInfo.isAdministrator()) {
		        	String message = "view procurement " + procurementId;
		    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
		    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
		        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
				}
				
				// Perform search based on procurement's stored keyword search
				if (procurement.getSearchKeyword() != null && procurement.getSearchKeyword().trim().length() > 0) {
					searchModel.setSearchKeywords(procurement.getSearchKeyword());
					searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
					try {
						procurement = procurementService.saveCurrentPosition(procurement.getId(), secInfo.getOrgContactId(), Optional.of(searchModel.getSearchKeywords()), Optional.of(""), Optional.empty(), Optional.empty(), Optional.empty());
					} catch (Exception e) {
			        	String message = "Could not save current position of procurement " + procurementId;
			    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
					}
				}
			}
		}
		
		setupModel(null, searchModel);	
		
		model.addAttribute("searchSolutionByKeywordModel", searchModel);

        return "buying-process/searchSolutionByKeyword";
    }	
	
	@PostMapping(value = {"/buyingprocess/solutionByKeyword", "/buyingprocess/{optProcurementId}/solutionByKeyword"})
	public String solutionByCapabilityPost(@PathVariable Optional<Long> optProcurementId, @Valid SearchSolutionByKeywordModel searchModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {
		if (optProcurementId.isPresent()) {
			searchModel.setProcurementId(optProcurementId.get());
		}
		searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		if (optProcurementId.isPresent()) {
			long procurementId = optProcurementId.get();
			try {
				Procurement procurement = null;
				Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
				if (optProcurement.isPresent()) {
					procurement = optProcurement.get();				

					// Check that the user is authorised to this procurement
					if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
					 && !secInfo.isAdministrator()) {
			        	String message = "view procurement " + procurementId;
			    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
			    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
			        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
					}

					procurement = procurementService.saveCurrentPosition(optProcurementId.get(), secInfo.getOrgContactId(), Optional.of(searchModel.getSearchKeywords()), Optional.of(""), Optional.empty(), Optional.empty(), Optional.empty());
						//procurementService.saveCurrentPosition(optProcurementId.get(), secInfo.getOrgContactId(), Optional.of(searchModel.getSearchKeywords()), Optional.empty());
			
				} else {
					procurement = procurementService.saveCurrentPosition(0, secInfo.getOrgContactId(), Optional.of(searchModel.getSearchKeywords()), Optional.of(""), Optional.empty(), Optional.empty(), Optional.empty());
				}
				searchModel.setProcurement(procurement);
				searchModel.setProcurementId(procurement.getId());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
