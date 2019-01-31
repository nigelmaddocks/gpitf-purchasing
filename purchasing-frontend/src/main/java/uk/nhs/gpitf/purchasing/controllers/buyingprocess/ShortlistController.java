package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.models.OrgContactModel;
import uk.nhs.gpitf.purchasing.models.ShortlistModel;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
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
    
	@Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;
	
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
			procurement.setCsvShortlistSolutions(csvSolutionIds);
			procurement.setInitialPatientCount((int)initialPatientCount);
			procurement.setStatus(statusShortlist);
			procurement.setStatusLastChangedDate(LocalDateTime.now());
			procurement.setLastUpdated(LocalDateTime.now());
			procurementRepository.save(procurement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ShortlistModel shortListModel = new ShortlistModel();
		model.addAttribute("shortListModel", shortListModel);
		
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
		
		setupModelCollections(shortlistModel, procurement);;
		
		return "buying-process/shortlist";	
	}
	
	private Model setupModel(SecurityInfo secInfo, Procurement procurement, Model model) {
		
		ShortlistModel shortlistModel = new ShortlistModel();
		
		shortlistModel.setProcurementId(procurement.getId());
		
		setupModelCollections(shortlistModel, procurement);
		
		shortlistModel.setNumberOfPractices(procurement.getCsvPractices().split(",").length);
		shortlistModel.setNumberOfPatients(procurement.getInitialPatientCount());
		
		model.addAttribute("shortlistModel", shortlistModel);
		
		return model;
	}
	
	private void setupModelCollections(ShortlistModel shortlistModel, Procurement procurement) {		
		String[] arrSolutionIds = procurement.getCsvShortlistSolutions().split(",");
		for (String solutionId : arrSolutionIds) {
			if (solutionId.trim().length() > 0) {
				shortlistModel.getSolutions().add(onboardingService.getSolutionEx2ById(solutionId));
			}
		}			
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
