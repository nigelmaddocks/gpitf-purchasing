package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
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

	@Value("${sysparam.foundationSearchAddCapabilities}")
    private String FOUNDATION_SEARCH_ADD_CAPABILITIES_STRING;

    private static final Logger logger = LoggerFactory.getLogger(SolutionByCapabilityController.class);
/*
	@GetMapping("/buyingprocess/solutionByCapability/{csvCapabilities}")
	public String solutionByCapability(@PathVariable String csvCapabiities, Model model, HttpServletRequest request) {

		model = setupSolutionByCapability(null, csvCapabiities, model);
        return "buying-process/searchSolutionByCapability";
    }
*/
	@GetMapping(value = {
			"/buyingprocess/solutionByCapability/{optCsvCapabilities}",
			"/buyingprocess/{optProcurementId}/solutionByCapability/{optCsvCapabilities}",
			"/buyingprocess/solutionByCapability",
			"/buyingprocess/{optProcurementId}/solutionByCapability"})
	public String solutionByCapability(
			@PathVariable Optional<Long> optProcurementId,
			@PathVariable Optional<String> optCsvCapabilities,
			@RequestParam(value = "mode", defaultValue = ""+SearchSolutionByCapabilityModel.MODE_ALL) String sMode,
			@RequestParam(value = "foundation", defaultValue = "") String sFoundationFromQuerystring,
			Model model, RedirectAttributes attr, HttpServletRequest request) {

		int mode = Integer.valueOf(sMode);
		boolean foundation = Boolean.valueOf(sFoundationFromQuerystring);

		Breadcrumbs.register(mode==SearchSolutionByCapabilityModel.MODE_SITES_ONLY ? "Select sites" : "By capability", request);

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
				}
			}
		} else {
		    HttpSession session = request.getSession();
			procurement = procurementService.restoreFromSession(session);
			if (procurement == null) {
				procurementService.setPrimitiveProcurement(
				    session,
				    procurementService.getPrimitiveProcurement(session, secInfo)
				);
				procurement = procurementService.restoreFromSession(session);
			}
		}

		if (procurement != null) {

			// Prioritise getting the "foundation" flag from querystring over that on the procurement record
			if (sFoundationFromQuerystring == null || sFoundationFromQuerystring.trim().length() == 0) {
				foundation = procurement.getFoundation()==null?false:procurement.getFoundation().booleanValue();
			}

			if (procurement.getStatus().getId() != ProcStatus.DRAFT) {
	        	String message = "procurement " + procurementId + " is at the wrong status. Its status is " + procurement.getStatus().getName() + ".";
	    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", message);
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
			}

			// If we've skipped the keyword search, then put it in the breadcrumb
			if (procurement.getSearchKeyword() != null && procurement.getSearchKeyword().trim().length() > 0) {
				Breadcrumbs.removeLast(request);
				Breadcrumbs.register("By keyword", "/buyingprocess/" + procurement.getId() + "/solutionByKeyword", request);
				Breadcrumbs.register("By capability", request);
			}

			// Check that the user is authorised to this procurement
			if (secInfo.isKnown()
			 && procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
			 && !secInfo.isAdministrator()) {
	        	String message = "view procurement " + procurementId;
	    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
			}
			if (secInfo.isKnown()) {
				try {
					procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), csvCapabilities==null?Optional.empty():Optional.of(csvCapabilities), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(foundation), Optional.empty());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (csvCapabilities == null || csvCapabilities.trim().length() == 0) {
				csvCapabilities = procurement.getCsvCapabilities();
			}
		}

		model = setupSolutionByCapability(secInfo, procurement, csvCapabilities, mode, foundation, model);
        return "buying-process/searchSolutionByCapability";
    }

	private Model setupSolutionByCapability(SecurityInfo secInfo, Procurement procurement, String csvCapabilities, int mode, boolean foundation, Model model) {
		SearchSolutionByCapabilityModel myModel = new SearchSolutionByCapabilityModel();
		myModel.setSHORTLIST_MAX(SHORTLIST_MAX);
		myModel.setFOUNDATION_SEARCH_ADD_CAPABILITIES(Boolean.valueOf(FOUNDATION_SEARCH_ADD_CAPABILITIES_STRING));
		myModel.setMode(mode);
		myModel.setUserIsKnown(secInfo.isKnown());
		myModel.setFoundation(foundation);
		myModel.setProcurement(procurement);
		myModel.setProcurementId(0L);
		if (procurement != null) {
			myModel.setProcurementId(procurement.getId());
			myModel.setCsvPractices("," + procurement.getCsvPractices() + ",");
			myModel.setPatientCount(organisationService.getPatientCountForOrganisationsInList(procurement.getCsvPractices()));
		}
		if (myModel.getCsvPractices() == null || myModel.getCsvPractices().equals(",,") || myModel.getCsvPractices().equals(",null,") || myModel.getCsvPractices().length() == 0) {
			myModel.setCsvPractices(",");
		}
		myModel.setCsvCapabilities(csvCapabilities);
		myModel.setCsvInteroperables(procurement.getCsvInteroperables());
		myModel.setAllCapabilities(onboardingService.orderByCoreThenName(onboardingService.findCapabilitiesFromCache()));

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

		// Setup the foundation systems (for use on additional interoperability filters)
		myModel.setFoundationSolutions(onboardingService.getFoundationSolutions());

		model.addAttribute("myModel", myModel);

		return model;

	}


}
