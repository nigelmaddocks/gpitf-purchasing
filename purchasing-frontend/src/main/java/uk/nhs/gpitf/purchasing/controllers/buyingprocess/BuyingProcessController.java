package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.models.ListProcurementsModel;
import uk.nhs.gpitf.purchasing.models.view.buyingprocess.ProcurementDeleteView;
import uk.nhs.gpitf.purchasing.models.view.buyingprocess.ProcurementEditNameView;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
@RequestMapping("/buyingprocess")
public class BuyingProcessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyingProcessController.class);

    protected static final String ATTRIBUTE_PROCUREMENT = "procurement";

    protected static final String URL_PATH = "/buyingprocess";
    protected static final String PAGE_PATH = "buying-process/";
	protected static final String PAGE_INDEX = "index";
	protected static final String PAGE_SEARCH_SOLUTIONS_MENU = "searchSolutionMenu";
	protected static final String PAGE_LIST_PROCUREMENTS = "listProcurements";
	protected static final String PAGE_PROCUREMENT = "procurement";
	protected static final String PAGE_RENAME_PROCUREMENT = "procurementRename";
	protected static final String PAGE_DELETE_PROCUREMENT = "procurementDelete";

    @Autowired
    private OrgContactRepository orgContactRepository;

    @Autowired
    private ProcurementService procurementService;

    @Autowired
    private Validator validator;

	@GetMapping()
	public String home(HttpServletRequest request) {
		//Breadcrumbs.register("Buying Process", request);
		return PAGE_PATH + PAGE_INDEX;
	}

	@GetMapping("/searchSolutionMenu")
	public String searchSolutionsMenu(HttpServletRequest request) {
		//Breadcrumbs.register("Search menu", request);
		return PAGE_PATH + PAGE_SEARCH_SOLUTIONS_MENU;
	}

	@GetMapping("/{procurementId}/gotoProcurement")
	public String gotoProcurement(@PathVariable Long procurementId, HttpServletRequest request, RedirectAttributes attr) throws ProcurementNotFoundException {
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

        Procurement procurement = procurementService.findById(procurementId);

		// Check that the user is authorised to this procurement
		if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
		 && !secInfo.isAdministrator()) {
        	String message = "view procurement " + procurementId;
    		LOGGER.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
		}

		// Check that the user is authorised to this procurement
		if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
		 && !secInfo.isAdministrator()) {
        	String message = "view procurement " + procurementId;
        	LOGGER.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
		}

		long procurementStatusId = procurement.getStatus().getId();

		if (procurementStatusId == ProcStatus.DRAFT) {
			//if (procurement.getCsvCapabilities() != null && procurement.getCsvCapabilities().trim().length() > 0) {
				return "redirect:/buyingprocess/" + procurementId + "/solutionByCapability/" + GUtils.nullToString(procurement.getCsvCapabilities()).trim();
			//}
		} else
		if (procurementStatusId == ProcStatus.INITIATE) {
			return "redirect:/buyingprocess/initiate/" + procurementId;
		}

    	String message = "Development is still in progress for procurements of status " + procurement.getStatus().getName();
    	LOGGER.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
		attr.addFlashAttribute("security_message", message);
    	return SecurityInfo.SECURITY_ERROR_REDIRECT;

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
				LOGGER.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
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

		return PAGE_PATH + PAGE_LIST_PROCUREMENTS;
	}

	@GetMapping("/procurement")
	public String procurement(Model model, HttpServletRequest request) {
	  //Breadcrumbs.register("My Procurements", request);

	  SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
	  List<Procurement> procurementList = procurementService.getUncompletedByOrgContactOrderByLastUpdated(secInfo.getOrgContactId());

	  model.addAttribute("procurements", procurementList);

	  return PAGE_PATH + PAGE_PROCUREMENT;
	}

	@GetMapping("/procurement/{procurementId}/edit-name")
	public String editProcurementName(@PathVariable Long procurementId, Model model, HttpServletRequest request) throws ProcurementNotFoundException {
 	  Breadcrumbs.register("Rename Procurement", request);

 	  Optional<Procurement> procurement = Optional.ofNullable(procurementService.findById(procurementId));

 	  var pageModel = new ProcurementEditNameView();
 	  procurement.ifPresent(obj -> {
 	    pageModel.setId(obj.getId());
 	    pageModel.setName(obj.getName());
 	  });
 	  model.addAttribute(ATTRIBUTE_PROCUREMENT, pageModel);
	  return PAGE_PATH + PAGE_RENAME_PROCUREMENT;
	}

	@PostMapping("/procurement/edit-name")
    public String editProcurementNamePost(@ModelAttribute(ATTRIBUTE_PROCUREMENT) ProcurementEditNameView pageModel, BindingResult bindingResult) throws ProcurementNotFoundException {

	  Procurement validatedProcurement = procurementService.findById(pageModel.getId());
	  validatedProcurement.setName(pageModel.getName());

	  validator.validate(validatedProcurement, bindingResult);
	  if (bindingResult.hasErrors()) {
	    return PAGE_PATH + PAGE_RENAME_PROCUREMENT;
	  }

	  procurementService.save(validatedProcurement);

      return REDIRECT_URL_PREFIX + "/buyingprocess/listProcurements";
    }

	@GetMapping("/procurement/{procurementId}/delete")
    public String deleteProcurement(@PathVariable Long procurementId, Model model, HttpServletRequest request) throws ProcurementNotFoundException {
      Breadcrumbs.register("Delete Procurement", request);

      Optional<Procurement> procurement = Optional.ofNullable(procurementService.findById(procurementId));

      var pageModel = new ProcurementDeleteView();
      procurement.ifPresent(obj -> {
        pageModel.setId(obj.getId());
        pageModel.setName(obj.getName());
        pageModel.setStartedDate(obj.getStartedDate());
        pageModel.setLastUpdated(obj.getLastUpdated());
      });

      model.addAttribute(ATTRIBUTE_PROCUREMENT, pageModel);
      return PAGE_PATH + PAGE_DELETE_PROCUREMENT;
    }

	@PostMapping("/procurement/delete")
	public String deleteProcurement(ProcurementDeleteView pageModel) throws Exception {

	  procurementService.delete(pageModel.getId());

	  return REDIRECT_URL_PREFIX + "/buyingprocess/listProcurements";
	}
}
