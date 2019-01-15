package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;

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
	public String solutionByCapability(@PathVariable Long procurementId, Model model, HttpServletRequest request) {
		Breadcrumbs.register("By keyword", request);
		
		SearchSolutionByKeywordModel searchModel = new SearchSolutionByKeywordModel();
		searchModel.setProcurementId(procurementId);
		
		if (procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				if (procurement.getSearchKeyword() != null && procurement.getSearchKeyword().trim().length() > 0) {
					searchModel.setSearchKeywords(procurement.getSearchKeyword());
					searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
				}
			}
		}
		
		setupModel(null, searchModel);	
		
		model.addAttribute("searchSolutionByKeywordModel", searchModel);

        return "buying-process/searchSolutionByKeyword";
    }	
	
	@PostMapping(value = {"/buyingprocess/solutionByKeyword", "/buyingprocess/{procurementId}/solutionByKeyword"})
	public String solutionByCapabilityPost(@PathVariable Optional<Long> optProcurementId, @Valid SearchSolutionByKeywordModel searchModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {
		if (optProcurementId.isPresent()) {
			searchModel.setProcurementId(optProcurementId.get());
		}
		searchModel.setSolutions(onboardingService.findSolutionsHavingKeywords(searchModel.getSearchKeywords()));
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		if (optProcurementId.isPresent()) {
			try {
				Procurement procurement =
						procurementService.saveSearchKeyword(optProcurementId.get(), secInfo.getOrgContactId(), searchModel.getSearchKeywords());
			
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
