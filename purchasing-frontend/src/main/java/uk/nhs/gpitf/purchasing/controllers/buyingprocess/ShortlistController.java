package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.ProcShortlist;
import uk.nhs.gpitf.purchasing.entities.ProcShortlistRemovalReason;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.models.OrgContactModel;
import uk.nhs.gpitf.purchasing.models.ShortlistModel;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcShortlistRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.ProcShortlistRemovalReasonService;
import uk.nhs.gpitf.purchasing.services.OnboardingService.RankedSolution;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class ShortlistController {
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	OrganisationRepository organisationRepository;
	
	@Autowired
	OrganisationService organisationService;
    
	@Autowired
	OnboardingService onboardingService;
	
	@Autowired
	OrgRelationshipService orgRelationshipService;
    
	@Autowired
	ProcShortlistRepository procShortlistRepository;
    
	@Autowired
	ProcShortlistRemovalReasonService procShortlistRemovalReasonService;
	
	@Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;
	
	@Value("${sysparam.directaward.maxvalue}")
    private String DIRECTAWARD_MAXVALUE;
	
    private static final Logger logger = LoggerFactory.getLogger(ShortlistController.class);

	@GetMapping(value = {"/buyingprocess/directShortlistInitialise/{procurementId}"})
	public String directShortlistInitialise(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.removeTitle("By capability", request);
		Breadcrumbs.removeTitle("By keyword", request);
		Breadcrumbs.register("Shortlist", request);
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		if (procurementId == 0) {
        	String message = "Unidentified route to shortlist";
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
			
			if (procurement.getStatus().getId() != ProcStatus.DRAFT) {
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

		// Perform a capability search and verify that the number of results is still below the threshold
		List<RankedSolution> rankedSolutions = onboardingService.findRankedSolutionsHavingCapabilitiesInList(procurement.getCsvCapabilities());
		if (rankedSolutions.size() > Integer.valueOf(SHORTLIST_MAX)) {
        	String message = "Shortlist size for procurement " + procurementId + " is " + rankedSolutions.size() +
        			" but the maximum allowed size is " + SHORTLIST_MAX + " in order to proceed to shortlisting. " +
        			"Please re-visit your procurement to reduce the solution results or go through the long-listing process.";
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;								
		}
		
		// Save the shortlisted solution ids and calculated patient count to the the procurement
		// and set its status to Shortlist
		String csvSolutionIds = "";
		for (var rankedSolution : rankedSolutions) {
			csvSolutionIds += "," + rankedSolution.solution.getId();
		}
		if (csvSolutionIds.length() > 0) {
			csvSolutionIds = csvSolutionIds.substring(1);
		}
		
		// . . Set up the user's CCGs
		ArrayList<Organisation> arlCCGs = new ArrayList<>();
		if (secInfo.getOrganisationTypeId() == OrgType.CCG || secInfo.getOrganisationTypeId() == OrgType.CSU) {
			Organisation myOrganisation = organisationRepository.findById(secInfo.getOrganisationId()).get();
			if (secInfo.getOrganisationTypeId() == OrgType.CCG) {
				arlCCGs.add(myOrganisation);
			}
			if (secInfo.getOrganisationTypeId() == OrgType.CSU) {
				try {
					RelationshipType relTypeCSUtoCCG = (RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG);
					List<Organisation> myCCGs = orgRelationshipService.getOrganisationsByParentOrgAndRelationshipType(myOrganisation, relTypeCSUtoCCG);
					for (var org : myCCGs) {
						arlCCGs.add(org);
					}
				} catch (Exception e) {	
					e.printStackTrace();
				}
			}
		}
		
		// . . Get patient count info for each practice in each CCG and sum the patient count for selected practices
		long initialPatientCount = organisationService.getPatientCountForOrganisationsInList(procurement.getCsvPractices());
		// . . Update the procurement	
		try {
			ProcStatus statusShortlist = (ProcStatus) GUtils.makeObjectForId(ProcStatus.class, ProcStatus.SHORTLIST);
//			procurement.setCsvShortlistSolutions(csvSolutionIds);
			procurement.setInitialPatientCount((int)initialPatientCount);
			procurement.setStatus(statusShortlist);
			procurement.setStatusLastChangedDate(LocalDateTime.now());
			procurement.setLastUpdated(LocalDateTime.now());
			procurementRepository.save(procurement);
			
			String[] arrSolutionIds = csvSolutionIds.split(",");
			for (String solutionId : arrSolutionIds) {
				solutionId = solutionId.trim();
				if (solutionId.length() > 0) {
					ProcShortlist shortlistItem = new ProcShortlist();
					shortlistItem.setProcurement(procurement);
					shortlistItem.setSolutionId(solutionId);
					procShortlistRepository.save(shortlistItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ShortlistModel shortlistModel = new ShortlistModel();
		
		setupModel(secInfo, procurement, model);
		
		return "buying-process/shortlist";

	}
	

	@GetMapping(value = {"/buyingprocess/shortlist/{procurementId}"})
	public String directShortlist(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("Shortlist", request);
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		Object rtnObject = procurementSecurityValidation(secInfo, procurementId, attr);
		if (rtnObject instanceof String) {
			return (String) rtnObject;
		}
		Procurement procurement = (Procurement)rtnObject;
		
		setupModel(secInfo, procurement, model);
		
		return "buying-process/shortlist";
	}
	
	@PostMapping("/buyingprocess/shortlist")
	public String directShortlistRecalculate(
			@Valid ShortlistModel shortlistModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		
		long procurementId = shortlistModel.getProcurementId();
		Object rtnObject = procurementSecurityValidation(secInfo, procurementId, attr);
		if (rtnObject instanceof String) {
			return (String) rtnObject;
		}
		Procurement procurement = (Procurement)rtnObject;
		
		shortlistModel.DIRECTAWARD_MAXVALUE = Integer.valueOf(DIRECTAWARD_MAXVALUE);
		setupModelCollections(shortlistModel, procurement);;
		
		// Validate that if a solution is being removed, a reason is given
		if (!bindingResult.hasErrors()) {			
			if (shortlistModel.removeSolutionId != null && shortlistModel.removeSolutionId.trim().length() > 0) {
				if (shortlistModel.removalReasonId == 0) {
					bindingResult.addError(new ObjectError("removalReasonId", "Please specify a reason for removing a solution from the shortlist"));
				} else 
				if (shortlistModel.removalReasonId == ProcShortlistRemovalReason.OTHER
				 && (shortlistModel.removalReasonText == null || shortlistModel.removalReasonText.trim().length() == 0)) {
					bindingResult.addError(new ObjectError("removalReasonText", "When specifying \"Other\" as the reason for removal, please supply additional text"));
				}
			}
		}
		
		// Validate that if a direct award is being made for a solution, that the value is less than the threshold
		if (!bindingResult.hasErrors()) {	
			if (!bindingResult.hasErrors() && (shortlistModel.contractMonthsYears == null || shortlistModel.contractMonthsMonths == null)) {
				bindingResult.addError(new ObjectError("directAward", "Please select the contract length"));
			}
			
			if (!bindingResult.hasErrors() && shortlistModel.directAwardSolutionId != null && shortlistModel.directAwardSolutionId.trim().length() > 0) {
				if (shortlistModel.getPrice(shortlistModel.directAwardSolutionId).compareTo(new BigDecimal(Integer.valueOf(DIRECTAWARD_MAXVALUE))) > 0) {
					bindingResult.addError(new ObjectError("directAward", "Cannot directly award if the value is £" + DIRECTAWARD_MAXVALUE + " or greater"));
				}
			}
		}
		
		// Remove a solution if the model attribute non-blank
		if (!bindingResult.hasErrors()) {
			if (shortlistModel.removeSolutionId != null && shortlistModel.removeSolutionId.trim().length() > 0) {
				
				String removeSolutionId = shortlistModel.removeSolutionId.trim();
				for (ProcShortlist shortlistItem : procurement.getShortlistItems()) {
					if (shortlistItem.getSolutionId().equals(removeSolutionId)) {
						shortlistItem.setRemoved(true);
						if (shortlistModel.removalReasonId != 0) {
							try {
								ProcShortlistRemovalReason removalReason = (ProcShortlistRemovalReason) GUtils.makeObjectForId(ProcShortlistRemovalReason.class, shortlistModel.removalReasonId);
								shortlistItem.setRemovalReason(removalReason);
								if (shortlistModel.removalReasonId == ProcShortlistRemovalReason.OTHER) {
									shortlistItem.setRemovalReasonText(shortlistModel.removalReasonText);
								} else {
									shortlistItem.setRemovalReasonText(null);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						procShortlistRepository.save(shortlistItem);
						break;
					}
				}
			}
		}
		
		// Reset any fields that lead to actions
		if (!bindingResult.hasErrors()) {
			shortlistModel.setRemoveSolutionId("");
			shortlistModel.setRemovalReasonId(null);
			shortlistModel.setRemovalReasonText("");
			shortlistModel.setDirectAwardSolutionId("");
		}		
		
		return "buying-process/shortlist";	
	}
	
	private Model setupModel(SecurityInfo secInfo, Procurement procurement, Model model) {
		
		ShortlistModel shortlistModel = new ShortlistModel();
		shortlistModel.DIRECTAWARD_MAXVALUE = Integer.valueOf(DIRECTAWARD_MAXVALUE);
		
		shortlistModel.setProcurementId(procurement.getId());
		
		setupModelCollections(shortlistModel, procurement);
		
		shortlistModel.setNumberOfPractices(procurement.getCsvPractices().split(",").length);
		shortlistModel.setNumberOfPatients(procurement.getInitialPatientCount());
		
		model.addAttribute("shortlistModel", shortlistModel);
		
		return model;
	}
	
	private void setupModelCollections(ShortlistModel shortlistModel, Procurement procurement) {		
//		String[] arrSolutionIds = procurement.getCsvShortlistSolutions().split(",");
		for (ProcShortlist shortlistItem : procurement.getShortlistItems()) {
			String solutionId = shortlistItem.getSolutionId();
			if (solutionId.trim().length() > 0 && shortlistItem.isRemoved() == false) {
				shortlistModel.getSolutions().add(onboardingService.getSolutionEx2ById(solutionId));
			}
		}
		
		shortlistModel.setRemovalReasons(procShortlistRemovalReasonService.getAll());
	}
	
	private Object procurementSecurityValidation(SecurityInfo secInfo, long procurementId, RedirectAttributes attr) {
		if (procurementId == 0) {
        	String message = "Unidentified route to shortlist";
    		logger.warn(secInfo.loggerSecurityMessage(message));
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
	    		logger.warn(secInfo.loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
			}
			
			if (procurement.getStatus().getId() != ProcStatus.SHORTLIST) {
	        	String message = "procurement " + procurementId + " is at the wrong status. Its status is " + procurement.getStatus().getName() + ".";
	    		logger.warn(secInfo.loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", message);
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
			}
		} else {
        	String message = "procurement " + procurementId + " not found";
    		logger.warn(secInfo.loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
		}
		
		return procurement;
		
	}
}
