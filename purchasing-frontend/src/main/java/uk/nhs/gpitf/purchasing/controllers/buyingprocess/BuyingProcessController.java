package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
@RequestMapping("/buyingprocess")
public class BuyingProcessController {

	protected static final String PATH = "buying-process/";
	protected static final String PAGE_INDEX = "index";
	protected static final String PAGE_SEARCH_SOLUTIONS_MENU = "searchSolutionMenu";
	protected static final String PAGE_LIST_PROCUREMENTS = "listProcurements";
	protected static final String PAGE_PROCUREMENT = "procurement";

    @Autowired
    private OrgContactRepository orgContactRepository;

    @Autowired
    private ProcurementService procurementService;

    @Autowired
    private ProcurementRepository procurementRepository;

    private static final Logger logger = LoggerFactory.getLogger(BuyingProcessController.class);

	@GetMapping()
	public String home(HttpServletRequest request) {
		Breadcrumbs.register("Buying Process", request);
		return PATH + PAGE_INDEX;
	}

	@GetMapping("/searchSolutionMenu")
	public String searchSolutionsMenu(HttpServletRequest request) {
		Breadcrumbs.register("Search menu", request);
		return PATH + PAGE_SEARCH_SOLUTIONS_MENU;
	}

	@GetMapping("/{procurementId}/gotoProcurement")
	public String gotoProcurement(@PathVariable Long procurementId, HttpServletRequest request, RedirectAttributes attr) {
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

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

				long procurementStatusId = procurement.getStatus().getId();
				
				if (procurementStatusId == ProcStatus.DRAFT) {
					if (procurement.getCsvCapabilities() != null && procurement.getCsvCapabilities().trim().length() > 0) {
						return "redirect:/buyingprocess/" + procurementId + "/solutionByCapability/" + procurement.getCsvCapabilities().trim();
					}
				} else 
				if (procurementStatusId == ProcStatus.SHORTLIST) {
					return "redirect:/buyingprocess/shortlist/" + procurementId;
				}
				
	        	String message = "Development is still in progress for procurements of status " + procurement.getStatus().getName();
	    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
	    		attr.addFlashAttribute("security_message", message);
	        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
			}
		}


		return "redirect:/buyingprocess/" + procurementId + "/solutionByKeyword";
	}

	@GetMapping(value = {"/listProcurements", "/listProcurements/{optionalOrgContactId}"})
	public String listProcurements(@PathVariable Optional<Long> optionalOrgContactId, HttpServletRequest request, Model model, RedirectAttributes attr) {
		Breadcrumbs.removeTitle("My Procurements", request);
		Breadcrumbs.register("Procurements", request);

		long orgContactId;
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		if (optionalOrgContactId.isEmpty()) {
			orgContactId = secInfo.getOrgContactId();
		} else {
			orgContactId = optionalOrgContactId.get();
		}

		// You can only see other people's procurements if they are at your organisation (unless you are an administrator)
		if (!secInfo.isAdministrator() && optionalOrgContactId.isPresent()) {
			Optional<OrgContact> paramOrgContact = orgContactRepository.findById(orgContactId);
			if (paramOrgContact.isEmpty()
			 || paramOrgContact.get().getOrganisation().getId() != secInfo.getOrganisationId()) {
		    	String message = "You cannot see procurements outside of your organisation";
				logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
				attr.addFlashAttribute("security_message", message);
		    	return SecurityInfo.SECURITY_ERROR_REDIRECT;
			}
		}

		ListProcurementsModel listProcurementsModel = new ListProcurementsModel();
		listProcurementsModel.setOrgContactId(orgContactId);
		listProcurementsModel.setOrgContact(orgContactRepository.findById(orgContactId).get());
		listProcurementsModel.setOpenProcurements(procurementService.getUncompletedByOrgContactOrderByLastUpdated(orgContactId));
		listProcurementsModel.setCompletedProcurements(procurementService.getAllByOrgContactAndStatusOrderByLastUpdatedDesc(orgContactId, ProcStatus.COMPLETED));

		model.addAttribute("listProcurementsModel", listProcurementsModel);

		return PATH + PAGE_LIST_PROCUREMENTS;
	}

	@GetMapping("/procurement")
	public String procurement(Model model, HttpServletRequest request) {
	  Breadcrumbs.register("My Procurements", request);

	  SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
	  List<Procurement> procurementList = procurementService.getUncompletedByOrgContactOrderByLastUpdated(secInfo.getOrgContactId());

	  model.addAttribute("procurements", procurementList);

	  return PATH + PAGE_PROCUREMENT;
	}

}
