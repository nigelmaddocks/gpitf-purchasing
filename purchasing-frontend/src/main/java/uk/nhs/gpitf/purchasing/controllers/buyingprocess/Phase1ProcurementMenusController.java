package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByCapabilityModel;
import uk.nhs.gpitf.purchasing.models.TmpBuyingStartModel;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
public class Phase1ProcurementMenusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String PAGE_PRICE_DECLARATION = "priceDeclaration";

	@Autowired
	ProcurementService procurementService;

	@GetMapping(value = {"/buyingprocess/capabilitiesOrSitesMenu"})
	public String capabilitiesOrSites(
		@RequestParam(value = "option", defaultValue = "0") String sOption,
		Model model, RedirectAttributes attr, HttpServletRequest request) {

		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		if (sOption != null && sOption.trim().length() > 0) {
			int iOption = Integer.valueOf(sOption);

			// Option 2: Select Sites
			if (iOption == 2) {
				long procurementId = 0;
				try {
					Procurement procurement = procurementService.saveCurrentPosition(0L, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
					procurementId = procurement.getId();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "redirect:/buyingprocess/" + procurementId + "/solutionByCapability?mode=" + SearchSolutionByCapabilityModel.MODE_SITES_ONLY;
			}
		}

        return "buying-process/capabilitiesOrSitesMenu";
	}

	@GetMapping(value = {"/buyingprocess/foundationChoiceMenu", "/buyingprocess/foundationChoiceMenu/{optProcurementId}"})
	public String foundationChoice(
			@PathVariable Optional<Long> optProcurementId,
			@RequestParam(value = "option", defaultValue = "0") String sOption,
			Model model, RedirectAttributes attr, HttpServletRequest request) throws ProcurementNotFoundException {

		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		if (optProcurementId.isPresent()) {
			model.addAttribute("procurementId", optProcurementId.get());
		}

		if (sOption != null && sOption.trim().length() > 0) {
			int iOption = Integer.valueOf(sOption);
			long procurementId = 0;
			Procurement procurement = null;
			if (optProcurementId.isEmpty()) {
				try {
					procurement = procurementService.saveCurrentPosition(0L, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
					procurementId = procurement.getId();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				procurementId = optProcurementId.get();
				procurement = procurementService.findById(procurementId);
			}

			procurement.setFoundation(iOption==1);
			procurementService.save(procurement);

			// Option 1: Foundation Systems
			if (iOption == 1) {
				return "redirect:/buyingprocess/" + procurementId + "/solutionByCapability";
			}

			// Option 2: Non-Foundation Systems
			if (iOption == 2) {
				return "redirect:/buyingprocess/capabilitiesOrKeywordsMenu/" + procurementId;
			}
		}

        return "buying-process/foundationChoiceMenu";
	}

	@GetMapping(value = {"/buyingprocess/capabilitiesOrKeywordsMenu", "/buyingprocess/capabilitiesOrKeywordsMenu/{optProcurementId}"})
	public String capabilitiesOrKeywords(
		@PathVariable Optional<Long> optProcurementId,
		@RequestParam(value = "option", defaultValue = "0") String sOption,
		Model model, RedirectAttributes attr, HttpServletRequest request) {

		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		if (optProcurementId.isPresent()) {
			model.addAttribute("procurementId", optProcurementId.get());
		}

		if (sOption != null && sOption.trim().length() > 0) {
			int iOption = Integer.valueOf(sOption);


			// Option 1: Capability search
			if (iOption == 1) {
				long procurementId = 0;
				if (optProcurementId.isEmpty()) {
					try {
						Procurement procurement = procurementService.saveCurrentPosition(0L, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
						procurementId = procurement.getId();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					procurementId = optProcurementId.get();
				}
				return "redirect:/buyingprocess/" + procurementId + "/solutionByCapability";
			}

			// Option 2: Keyword search
			if (iOption == 2) {
				if (optProcurementId.isEmpty()) {
					return "redirect:/buyingprocess/solutionByKeyword";
				} else {
					return "redirect:/buyingprocess/" + optProcurementId.get() + "/solutionByKeyword";
				}
			}
		}

        return "buying-process/capabilitiesOrKeywordsMenu";
	}

	@GetMapping("/buyingprocess/price-declaration")
    public String priceDeclaration(Model model, HttpServletRequest request) {
	    HttpSession session = request.getSession();

	    Procurement.PrimitiveProcurement procurement = (Procurement.PrimitiveProcurement) session.getAttribute(Procurement.SESSION_ATTR_NAME);
	    if (procurement == null) {
	      procurement = new Procurement.PrimitiveProcurement();
	    }
	    model.addAttribute("procurement", procurement);

        return BuyingProcessController.PATH + PAGE_PRICE_DECLARATION;
    }

	@PostMapping("/buyingprocess/price-declaration")
	public String priceDeclarationPost(@RequestParam Long evaluationType, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

	    Procurement.PrimitiveProcurement prim = procurementService.createAndPersistNewPrimitiveProcurement(session, secInfo);
	    prim.setEvaluationType(EvaluationTypeEnum.getById(evaluationType));
	    session.setAttribute(Procurement.SESSION_ATTR_NAME, prim);

	    // TODO change to correct view when known
        return BuyingProcessController.PATH + BuyingProcessController.PAGE_PROCUREMENT;
    }

	// Temporary page to kick off a procurement
	@GetMapping(value = {"/buyingprocess/tmpBuyingStart"})
	public String tmpBuyingStart(Model model, RedirectAttributes attr, HttpServletRequest request) {
		return "buying-process/tmpBuyingStart";
	}

	// Temporary page to kick off a procurement
	@PostMapping(value = {"/buyingprocess/tmpBuyingStart"})
	public String tmpBuyingStartPOST(Model model, TmpBuyingStartModel tmpBuyingStartModel, RedirectAttributes attr, HttpServletRequest request) {
		HttpSession session = request.getSession();
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		Procurement.PrimitiveProcurement prim = procurementService.createAndPersistNewPrimitiveProcurement(session, secInfo);

		prim.setEvaluationType(tmpBuyingStartModel.getEvaluationType());
		prim.setFoundation(tmpBuyingStartModel.getFoundation() == 1L);
		prim.setSingleSiteContinuity(tmpBuyingStartModel.getSingleSiteContinuity() == 2L);

		session.setAttribute(Procurement.SESSION_ATTR_NAME, prim);

		return "redirect:/buyingprocess/solutionByCapability";
	}
}
