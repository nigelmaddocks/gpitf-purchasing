package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.client.model.Solutions;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByCapabilityModel;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.repositories.results.Ids;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class SolutionByCapabilityController {
	
	@Autowired
	OnboardingService onboardingService;
	
	@Autowired
	OrganisationRepository organisationRepository;
	
	@Autowired
	OrganisationService organisationService;
	
	@Autowired
	OrgRelationshipService orgRelationshipService;
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	ProcurementService procurementService;
    
	@Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;
	
    private static final Logger logger = LoggerFactory.getLogger(SolutionByCapabilityController.class);
/*	
	@GetMapping("/buyingprocess/solutionByCapability/{csvCapabilities}")
	public String solutionByCapability(@PathVariable String csvCapabiities, Model model, HttpServletRequest request) {

		model = setupSolutionByCapability(null, csvCapabiities, model);	
        return "buying-process/searchSolutionByCapability";
    }	
*/	
	@GetMapping(value = {"/buyingprocess/solutionByCapability/{optCsvCapabilities}", "/buyingprocess/{optProcurementId}/solutionByCapability/{optCsvCapabilities}", "/buyingprocess/solutionByCapability", "/buyingprocess/{optProcurementId}/solutionByCapability"})
	public String solutionByCapability(@PathVariable Optional<Long> optProcurementId, @PathVariable Optional<String> optCsvCapabilities, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("By capability", request);

		String csvCapabilities =  null;
		if (optCsvCapabilities.isPresent()) {
			csvCapabilities = optCsvCapabilities.get();
		}
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		Long procurementId = null;
		Procurement procurement = null;
		
		if (optProcurementId.isPresent()) {
			procurementId = optProcurementId.get();
			if (procurementId != 0) {
				Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
				if (optProcurement.isPresent()) {
					procurement = optProcurement.get();
					
					// If we've skipped the keyword search, then put it in the breadcrumb
					if (procurement.getSearchKeyword() != null && procurement.getSearchKeyword().trim().length() > 0) {
						Breadcrumbs.removeLast(request);
						Breadcrumbs.register("By keyword", "/buyingprocess/" + procurement.getId() + "/solutionByKeyword", request);
						Breadcrumbs.register("By capability", request);						
					}
					
					// Check that the user is authorised to this procurement
					if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
					 && !secInfo.isAdministrator()) {
			        	String message = "view procurement " + procurementId;
			    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
			    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
			        	return SecurityInfo.SECURITY_ERROR_REDIRECT;					
					}
					try {
						procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), csvCapabilities==null?Optional.empty():Optional.of(csvCapabilities), Optional.empty());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (csvCapabilities == null || csvCapabilities.trim().length() == 0) {
						csvCapabilities = procurement.getCsvCapabilities();
					}
				}
			}
		}

		model = setupSolutionByCapability(secInfo, procurement, csvCapabilities, model);	
        return "buying-process/searchSolutionByCapability";
    }	

	private Model setupSolutionByCapability(SecurityInfo secInfo, Procurement procurement, String csvCapabilities, Model model) {
		SearchSolutionByCapabilityModel myModel = new SearchSolutionByCapabilityModel();
		myModel.setSHORTLIST_MAX(SHORTLIST_MAX);
		myModel.setProcurement(procurement);
		myModel.setProcurementId(0L);
		if (procurement != null) {
			myModel.setProcurementId(procurement.getId());
			myModel.setCsvPractices("," + procurement.getCsvPractices() + ",");
			myModel.setPatientCount(organisationService.getPatientCountForOrganisationsInList(procurement.getCsvPractices()));
		}
		if (myModel.getCsvPractices() == null || myModel.getCsvPractices().equals(",null,") || myModel.getCsvPractices().length() == 0) {
			myModel.setCsvPractices(",");
		}
		myModel.setCsvCapabilities(csvCapabilities);
		myModel.setAllCapabilities(onboardingService.orderByCoreThenName(onboardingService.findCapabilities()));
		
		// Set up the user's CCGs
		if (secInfo.getOrganisationTypeId() == OrgType.CCG || secInfo.getOrganisationTypeId() == OrgType.CSU) {
			List<Organisation> myCCGs = new ArrayList<>();
			String myCsvCCGIDs = "";
			Organisation myOrganisation = organisationRepository.findById(secInfo.getOrganisationId()).get();
			if (secInfo.getOrganisationTypeId() == OrgType.CCG) {
				myCCGs.add(myOrganisation);
				myCsvCCGIDs = "" + myOrganisation.getId();
			}
			if (secInfo.getOrganisationTypeId() == OrgType.CSU) {
				try {
					RelationshipType relTypeCSUtoCCG = (RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG);
					myCCGs = orgRelationshipService.getOrganisationsByParentOrgAndRelationshipType(myOrganisation, relTypeCSUtoCCG);
					for (var org : myCCGs) {
						myCsvCCGIDs += "," + org.getId();
					}
					if (myCsvCCGIDs.length() > 0) {
						myCsvCCGIDs = myCsvCCGIDs.substring(1);
					}
				} catch (Exception e) {					
				}
			}
			myModel.setMyCCGs(myCCGs);
			myModel.setMyCsvCCGIDs(myCsvCCGIDs);
			
			// Set up the Practice IDs for each CCG
			for (Organisation ccg : myCCGs) {
				try {
					String csv = ",";
/* replaced by code below so that one-pass SQL is issued rather than loads of SELECTs for child orgs				
					List<Organisation> practices = orgRelationshipService.getOrganisationsByParentOrgAndRelationshipType(ccg, (RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_PRACTICE));
					for (Organisation practice : practices) {
						if (myModel.getCsvPractices().contains("," + practice.getId() + ",")) {
							csv += practice.getId() + ",";
						}
					}
*/					
					List<Ids> practiceIds = orgRelationshipService.getChildIdsByParentOrgAndRelationshipType(ccg, (RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_PRACTICE));
					for (Ids practiceId : practiceIds) {
						if (myModel.getCsvPractices().contains("," + practiceId.id + ",")) {
							csv += practiceId.id + ",";
						}
					}

					myModel.getSelectedCCGPracticeIds().put(ccg.getId(), csv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		
		model.addAttribute("myModel", myModel);
		
		return model;
		
	}

	
}
