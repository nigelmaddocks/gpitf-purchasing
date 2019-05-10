package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
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

import uk.nhs.gpitf.purchasing.entities.CompetitionType;
import uk.nhs.gpitf.purchasing.entities.EvaluationTypeEnum;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.SingleSiteContinuityEnum;
import uk.nhs.gpitf.purchasing.entities.SolutionSetEnum;
import uk.nhs.gpitf.purchasing.exception.ProcurementNotFoundException;
import uk.nhs.gpitf.purchasing.models.SearchSolutionByCapabilityModel;
import uk.nhs.gpitf.purchasing.models.TmpBuyingStartModel;
import uk.nhs.gpitf.purchasing.models.view.buyingprocess.ChoosePriceDeclarationView;
import uk.nhs.gpitf.purchasing.models.view.buyingprocess.ChooseSingleSiteContinuityView;
import uk.nhs.gpitf.purchasing.models.view.buyingprocess.ChooseSolutionSetView;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
public class Stage1ProcurementMenusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String ATTRIBUTE_PAGE_MODEL = "pageModel";

    protected static final String PAGE_SINGLE_SITE_CONTINUITY = "chooseSingleSiteContinuity";
    protected static final String PAGE_PRICE_DECLARATION = "choosePriceDeclaration";
    protected static final String PAGE_SOLUTION_SET = "chooseSolutionSet";

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

	@GetMapping("/buyingprocess/choose-price-declaration")
    public String choosePriceDeclaration(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Price or Price+Quality", request);
		
	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
	    HttpSession session = request.getSession();

	    Procurement.PrimitiveProcurement procurement = procurementService.getPrimitiveProcurement(session, secInfo);
	    Optional<EvaluationTypeEnum> evaluationType = Optional.ofNullable(procurement.getEvaluationType());

	    var pageModel = new ChoosePriceDeclarationView();
	    evaluationType.ifPresent(type -> pageModel.setSelectedOption(type.getId()));

	    model.addAttribute(ATTRIBUTE_PAGE_MODEL, pageModel);
	    model.addAttribute("evaluationTypes", Arrays.asList(EvaluationTypeEnum.values()));

        return BuyingProcessController.PAGE_PATH + PAGE_PRICE_DECLARATION;
    }

	@PostMapping("/buyingprocess/choose-price-declaration")
    public String choosePriceDeclarationPost(ChoosePriceDeclarationView pageModel, HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

	    EvaluationTypeEnum value = EvaluationTypeEnum.getById(pageModel.getSelectedOption());

  	    Procurement.PrimitiveProcurement procurement = procurementService.getPrimitiveProcurement(session, secInfo);
	    procurement.setEvaluationType(EvaluationTypeEnum.getById(pageModel.getSelectedOption()));
	    procurementService.setPrimitiveProcurement(session, procurement);

	    if (value == EvaluationTypeEnum.PRICE_ONLY) {
			Breadcrumbs.register("Price only", request);
	    } else {
			Breadcrumbs.register("Price+Quality", request);
	    }	    
	    
        return REDIRECT_URL_PREFIX
            + BuyingProcessController.URL_PATH
            + "/"
            + "choose-solution-set";
    }

	@GetMapping(value = {
	              "/buyingprocess/choose-solution-set",
	              "/buyingprocess/{procurementId}/choose-solution-set"
	          })
	public String chooseSolutionSet(@PathVariable Optional<Long> procurementId, Model model, HttpServletRequest request) throws ProcurementNotFoundException {
		Breadcrumbs.register("Foundation or Non-Foundation", request);

	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
	    HttpSession session = request.getSession();

	    Optional<SolutionSetEnum> solutionSet;
	    if (procurementId.isPresent()) {
	      Procurement procurement = procurementService.findById(procurementId.get());
	      solutionSet = Optional.ofNullable(foundationBooleanToSolutionSet(procurement.getFoundation()));
	    } else {
	      Procurement.PrimitiveProcurement procurement = procurementService.getPrimitiveProcurement(session, secInfo);
	      solutionSet = Optional.ofNullable(foundationBooleanToSolutionSet(procurement.getFoundation()));
	    }

	    var pageModel = new ChooseSolutionSetView();
	    procurementId.ifPresent(pageModel::setId);
	    solutionSet.ifPresent(solution -> pageModel.setSelectedOption(solution.getId()));

	    model.addAttribute(ATTRIBUTE_PAGE_MODEL, pageModel);
	    model.addAttribute("solutionSets", Arrays.asList(SolutionSetEnum.values()));

	    return BuyingProcessController.PAGE_PATH + PAGE_SOLUTION_SET;
	}

	@PostMapping("/buyingprocess/choose-solution-set")
    public String chooseSolutionSetPost(ChooseSolutionSetView pageModel, HttpServletRequest request) throws ProcurementNotFoundException {
  	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
  	    HttpSession session = request.getSession();

  	    Optional<Long> procurementId = Optional.ofNullable(pageModel.getId());
  	    SolutionSetEnum solution = SolutionSetEnum.getById(pageModel.getSelectedOption());
  	    
  	    Procurement procurement = null;
  	    
  	    try {
	  	    if (procurementId.isEmpty()) {
				Procurement.PrimitiveProcurement prim = procurementService.getPrimitiveProcurement(session, secInfo);
				prim.setFoundation(solutionSetToFoundationBoolean(solution));
				session.removeAttribute(Procurement.SESSION_ATTR_NAME);
	  	    	procurement = procurementService.saveCurrentPosition(prim);
	  	    } else {
				procurement = procurementService.findById(procurementId.get());
				procurement.setFoundation(solutionSetToFoundationBoolean(solution));
	        }
	  	    long iCompetitionType = CompetitionType.ON_CATALOGUE;
	        if (procurement.getSingleSiteContinuity() == false && procurement.getFoundation() == true) {
	        	iCompetitionType = CompetitionType.OFF_CATALOGUE;
	        }
	        procurement.setCompetitionType((CompetitionType) GUtils.makeObjectForId(CompetitionType.class, iCompetitionType));	  	  
	        
			procurement = procurementService.save(procurement);

  	    } catch (Exception e) {
  	    	e.printStackTrace();
  	    }

        Breadcrumbs.removeAfterTitle("Home", request);
        Breadcrumbs.register("Procurements", "/buyingprocess/listProcurements", request);

//        if (procurement.getSingleSiteContinuity() == true
//         || procurement.getEvaluationType() == EvaluationTypeEnum.PRICE_ONLY) {
        	return REDIRECT_URL_PREFIX
        		+ BuyingProcessController.URL_PATH
        		+ "/" + procurement.getId()
        		+ "/gotoProcurement";
//        } else {
//        	return REDIRECT_URL_PREFIX
//            	+ BuyingProcessController.URL_PATH
//            	+ "/evaluations/" + procurement.getId(); 	       	
//        }
	}

	@GetMapping(value = {
	              "/buyingprocess/choose-single-site-continuity",
	              "/buyingprocess/{procurementId}/choose-single-site-continuity"
	          })
	public String chooseSingleSiteContinuity(@PathVariable Optional<Long> procurementId, Model model, HttpServletRequest request) throws ProcurementNotFoundException {
		Breadcrumbs.register("Regular or Continuity", request);
		
	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
	    HttpSession session = request.getSession();

	    Optional<SingleSiteContinuityEnum> set;
	    if (procurementId.isPresent()) {
	      Procurement procurement = procurementService.findById(procurementId.get());
	      set = Optional.ofNullable(singleSiteContinuityBooleanToSingleSiteContinuity(procurement.getSingleSiteContinuity()));
	    } else {
	      Procurement.PrimitiveProcurement procurement = procurementService.getPrimitiveProcurement(session, secInfo);
	      set = Optional.ofNullable(singleSiteContinuityBooleanToSingleSiteContinuity(procurement.getSingleSiteContinuity()));
	    }

	    var pageModel = new ChooseSingleSiteContinuityView();
	    procurementId.ifPresent(pageModel::setId);
	    set.ifPresent(element -> pageModel.setSelectedOption(element.getId()));

	    model.addAttribute(ATTRIBUTE_PAGE_MODEL, pageModel);
	    model.addAttribute("values", Arrays.asList(SingleSiteContinuityEnum.values()));

	    return BuyingProcessController.PAGE_PATH + PAGE_SINGLE_SITE_CONTINUITY;
	}

	@PostMapping("/buyingprocess/choose-single-site-continuity")
    public String chooseSingleSiteContinuity(ChooseSingleSiteContinuityView pageModel, HttpServletRequest request) throws ProcurementNotFoundException {
  	    SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
  	    HttpSession session = request.getSession();

  	    Optional<Long> procurementId = Optional.ofNullable(pageModel.getId());
  	    SingleSiteContinuityEnum value = SingleSiteContinuityEnum.getById(pageModel.getSelectedOption());
        if (procurementId.isPresent()) {
          Procurement procurement = procurementService.findById(procurementId.get());
          procurement.setSingleSiteContinuity(singleSiteContinuityToSingleSiteContinuityBoolean(value));
          procurementService.save(procurement);
        } else {
          Procurement.PrimitiveProcurement procurement = procurementService.getPrimitiveProcurement(session, secInfo);
          procurement.setSingleSiteContinuity(singleSiteContinuityToSingleSiteContinuityBoolean(value));
          procurementService.setPrimitiveProcurement(session, procurement);
        }

        if (value == SingleSiteContinuityEnum.SINGLE_SITE_CONTINUITY) {
    		Breadcrumbs.register("Continuity", request);

            return REDIRECT_URL_PREFIX
                    + BuyingProcessController.URL_PATH
                    + "/"
                    + "choose-solution-set";
        }
        
		Breadcrumbs.register("Regular", request);
        return REDIRECT_URL_PREFIX
            + BuyingProcessController.URL_PATH
            + "/"
            + "choose-price-declaration";
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

		Procurement.PrimitiveProcurement prim = procurementService.getPrimitiveProcurement(session, secInfo);

		prim.setEvaluationType(tmpBuyingStartModel.getEvaluationType());
		prim.setFoundation(tmpBuyingStartModel.getFoundation() == 1L);
		prim.setSingleSiteContinuity(tmpBuyingStartModel.getSingleSiteContinuity() == 2L);

		procurementService.setPrimitiveProcurement(session, prim);

		return "redirect:/buyingprocess/solutionByCapability";
	}

    private static SolutionSetEnum foundationBooleanToSolutionSet(Boolean foundation) {
      if (foundation == null) {
        return null;
      } else if (foundation) {
        return SolutionSetEnum.FOUNDATION;
      } else {
        return SolutionSetEnum.NON_FOUNDATION;
      }
    }

    private static Boolean solutionSetToFoundationBoolean(SolutionSetEnum solution) {
      return solution == null ? null : solution == SolutionSetEnum.FOUNDATION;
    }

    private static SingleSiteContinuityEnum singleSiteContinuityBooleanToSingleSiteContinuity(Boolean value) {
      if (value == null) {
        return null;
      } else if (value) {
        return SingleSiteContinuityEnum.SINGLE_SITE_CONTINUITY;
      } else {
        return SingleSiteContinuityEnum.NOT_SINGLE_SITE_CONTINUITY;
      }
    }

    private static Boolean singleSiteContinuityToSingleSiteContinuityBoolean(SingleSiteContinuityEnum value) {
      return value == null ? null : value == SingleSiteContinuityEnum.SINGLE_SITE_CONTINUITY;
    }

}
