package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import java.time.LocalDateTime;

/*
 * Controller for stub/rudimentary screens.
 * Move their endpoints into better controllers once development of them has started
 * 
 */

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.CompetitionType;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.EvaluationService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class StubScreensController {
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	EvaluationService evaluationService;
	
    private static final Logger logger = LoggerFactory.getLogger(StubScreensController.class);

//	@GetMapping(value = {"/buyingprocess/solutionsReview/{procurementId}"})
//	public String solutionsReview(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
//		Breadcrumbs.register("Solutions Review", request);
//
//		String securityCheck = procurementSecurity(procurementId, true, attr, request);
//		if (StringUtils.isNotEmpty(securityCheck)) {
//			return securityCheck;
//		}
//
//		Procurement procurement = procurementRepository.findById(procurementId).get();
//
//		model.addAttribute("procurementId", procurementId);
//		model.addAttribute("procurementName", procurement.getName());
//		model.addAttribute("procurementSummaryAttributes", procurement.getSummaryAttributes());
//		return "buying-process/solutionsReview";
//
//	}
	

	@GetMapping(value = {"/buyingprocess/solutionsComparison/{procurementId}"})
	public String solutionsComparison(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("Solutions Comparison", request);
		
		String securityCheck = procurementSecurity(procurementId, false, attr, request);
		if (StringUtils.isNotEmpty(securityCheck)) {
			return securityCheck;
		}
		
		Procurement procurement = procurementRepository.findById(procurementId).get();

		if (procurement.getSingleSiteContinuity() && procurement.getStatus().getId() != ProcStatus.DRAFT && procurement.getStatus().getId() != ProcStatus.INITIATE
		 || !procurement.getSingleSiteContinuity() && procurement.getStatus().getId() != ProcStatus.INITIATE) {
        	String message = "procurement " + procurementId + " is at the wrong status. Its status is " + procurement.getStatus().getName() + ".";
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
		}
		
		model.addAttribute("procurementId", procurementId);
		model.addAttribute("procurementName", procurement.getName());
		model.addAttribute("procurementSummaryAttributes", procurement.getSummaryAttributes());
		model.addAttribute("singleSiteContinuity", procurement.getSingleSiteContinuity());
		model.addAttribute("evaluationPriceOnly", procurement.getEvaluationType() != null && procurement.getEvaluationType() == EvaluationTypeEnum.PRICE_ONLY);
		return "buying-process/solutionsComparison";
	}	
	

	@GetMapping(value = {"/buyingprocess/offCatalogueBidsAndEvaluation/{procurementId}"})
	public String offCatalogueBidsAndEvaluation(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("Off-Catalogue Process", request);
		
		String securityCheck = procurementSecurity(procurementId, true, attr, request);
		if (StringUtils.isNotEmpty(securityCheck)) {
			return securityCheck;
		}
		
		Procurement procurement = procurementRepository.findById(procurementId).get();
		if (procurement.getCompetitionType() == null || procurement.getCompetitionType().getId() != CompetitionType.OFF_CATALOGUE) {
			try {
				procurement.setCompetitionType((CompetitionType)GUtils.makeObjectForId(CompetitionType.class, CompetitionType.OFF_CATALOGUE));
				procurement.setLastUpdated(LocalDateTime.now());
				procurement = procurementRepository.save(procurement);
				return "redirect:/buyingprocess/offCatalogueBidsAndEvaluation/" + procurementId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		model.addAttribute("procurementId", procurementId);
		model.addAttribute("procurementName", procurement.getName());
		model.addAttribute("procurementSummaryAttributes", procurement.getSummaryAttributes());
		return "buying-process/offCatalogueBidsAndEvaluation";
	}	
	
	private String procurementSecurity(long procurementId, boolean statusCheck, RedirectAttributes attr, HttpServletRequest request) {
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		if (procurementId == 0) {
        	String message = "Unidentified route to Solutions review";
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
		}	
		
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
			
			if (statusCheck && procurement.getStatus().getId() != ProcStatus.INITIATE) {
	        	String message = "procurement " + procurementId + " is at the wrong status. Its status is " + procurement.getStatus().getName() + ".";
	    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", message);
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
			}
		} else {
        	String message = "procurement " + procurementId + " not found";
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
		}
		
		return null;
	}
	
/*
	@GetMapping(value = {"/buyingprocess/evaluationConversion/{procurementId}"})
	public String onToOffCatalogConversion(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		
		evaluationService.convertOnToOffCatalogCriteria(procurementId);
		
		return null;
	}	
*/
}
