package uk.nhs.gpitf.purchasing.controllers.buyingprocess;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.core.instrument.util.StringUtils;
import uk.nhs.gpitf.purchasing.entities.Framework;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.ProcShortlist;
import uk.nhs.gpitf.purchasing.entities.ProcShortlistRemovalReason;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundleItem;
import uk.nhs.gpitf.purchasing.entities.ProcSrvRecipient;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.TmpAssociatedService;
import uk.nhs.gpitf.purchasing.entities.TmpPriceBasis;
import uk.nhs.gpitf.purchasing.entities.TmpUnitType;
import uk.nhs.gpitf.purchasing.models.InitiateModel;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcShortlistRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSolutionBundleItemRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSolutionBundleRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSrvRecipientRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.FrameworkService;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.ProcShortlistRemovalReasonService;
import uk.nhs.gpitf.purchasing.services.ProcSolutionBundleService;
import uk.nhs.gpitf.purchasing.services.ProcSrvRecipientService;
import uk.nhs.gpitf.purchasing.services.TmpAssociatedServiceService;
import uk.nhs.gpitf.purchasing.services.TmpSolutionPriceBandService;
import uk.nhs.gpitf.purchasing.services.OnboardingService.RankedBundle;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller 
public class InitiateController {
	
	@Autowired
	FrameworkService frameworkService;
	
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
	ProcSrvRecipientRepository procSrvRecipientRepository;
    
	@Autowired
	ProcSrvRecipientService procSrvRecipientService;
	
	@Autowired
	ProcSolutionBundleRepository procSolutionBundleRepository;
	
	@Autowired
	ProcSolutionBundleService procSolutionBundleService;
    
	@Autowired
	ProcSolutionBundleItemRepository procSolutionBundleItemRepository;
    
	@Autowired
	ProcShortlistRemovalReasonService procShortlistRemovalReasonService;
	
	@Autowired
	TmpSolutionPriceBandService tmpSolutionPriceBandService;

	
	@Value("${sysparam.shortlist.max}")
    private String SHORTLIST_MAX;
	
	@Value("${sysparam.directaward.maxvalue}")
    private String DIRECTAWARD_MAXVALUE;
	
    private static final Logger logger = LoggerFactory.getLogger(InitiateController.class);

	@GetMapping(value = {"/buyingprocess/directInitiateInitialise/{procurementId}"})
	@Transactional
	public String directInitiateInitialise(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.removeTitle("By capability", request);
		Breadcrumbs.removeTitle("By keyword", request);
		Breadcrumbs.register("Initiate", request);
		
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

		// Re-perform the capability search
		List<RankedBundle> rankedBundles = onboardingService.findRankedSolutionsHavingCapabilitiesInList(procurement.getCsvCapabilities(), procurement.getCsvInteroperables(), procurement.getFoundation().booleanValue());
		
		// Save the shortlisted solution ids and calculated patient count to the the procurement
		// and set its status to Shortlist
		String csvSolutionIds = "";
		for (var rankedBundle : rankedBundles) {
			//csvSolutionIds += "," + rankedSolution.solution.getId();
			
			// .. Short-term fix on the Drop 1 assumption that there is one solution in each bundle. Need to re-write this section to take account of bundle items correctly.
			for (var item : rankedBundle.bundle.getBundleItems()) {
				if (GUtils.nullToString(item.getSolutionId()).length() > 0) {
					csvSolutionIds += "," + item.getSolutionId();
				}
			}
		}
		if (csvSolutionIds.length() > 0) {
			csvSolutionIds = csvSolutionIds.substring(1);
		}
		
		// . . Get patient count info for each practice in each CCG and sum the patient count for selected practices
		long initialPatientCount = organisationService.getPatientCountForOrganisationsInList(procurement.getCsvPractices());
		// . . Update the procurement	
		try {
			ProcStatus statusInitiate = (ProcStatus) GUtils.makeObjectForId(ProcStatus.class, ProcStatus.INITIATE);
//			procurement.setCsvShortlistSolutions(csvSolutionIds);
			procurement.setInitialPatientCount((int)initialPatientCount);
			procurement.setStatus(statusInitiate);
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
			
			// Create the bundle and bundle items
			for (var rankedBundle : rankedBundles) {
				rankedBundle.bundle.setProcurement(procurement);
				
				ProcSolutionBundleItem[] arrBundleItems = rankedBundle.bundle.getBundleItems().toArray(new ProcSolutionBundleItem[] {});
				rankedBundle.bundle.getBundleItems().clear();
				
				ProcSolutionBundle savedBundle = procSolutionBundleRepository.save(rankedBundle.bundle);
				
				for (var bundleItem : arrBundleItems) {
					bundleItem.setBundle(savedBundle);
					procSolutionBundleItemRepository.save(bundleItem);
				}
			}
			
			// Create the Procurement Service Recipients
			List <ProcSrvRecipient> lstSrvRecipient = new ArrayList<>();
			String[] arrSRIDs = procurement.getCsvPractices().split(",");
			for (String sSRID : arrSRIDs) {
				if (StringUtils.isNotEmpty(sSRID)) {
					long iSRID = Long.valueOf(sSRID.trim());
					Optional<Organisation> optSR = organisationRepository.findById(iSRID);
					if (optSR.isPresent()) {
						Organisation orgSR = optSR.get();
						ProcSrvRecipient procSrvRecipient = new ProcSrvRecipient();
						procSrvRecipient.setProcurement(procurement);
						procSrvRecipient.setOrganisation(orgSR);
						procSrvRecipient.setPatientCount(orgSR.getPatientCount());
						procSrvRecipient.setTerm(null);
						lstSrvRecipient.add(procSrvRecipient);
					}
				}
			}
			procSrvRecipientRepository.saveAll(lstSrvRecipient);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setupModel(secInfo, procurement, model, HttpMethod.GET);
		
		return "buying-process/initiate";

	}
	

	@GetMapping(value = {"/buyingprocess/initiate/{procurementId}"})
	public String initiate(@PathVariable long procurementId, Model model, RedirectAttributes attr, HttpServletRequest request) {
		Breadcrumbs.register("Shortlist", request);
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

		Object rtnObject = procurementSecurityValidation(secInfo, procurementId, attr);
		if (rtnObject instanceof String) {
			return (String) rtnObject;
		}
		Procurement procurement = (Procurement)rtnObject;
		
		setupModel(secInfo, procurement, model, HttpMethod.GET);
		
		return "buying-process/initiate";
	}
	
	@PostMapping("/buyingprocess/initiate")
	public String initiateRecalculate(
			@Valid InitiateModel initiateModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {
		initiateModel.setTmpSolutionPriceBandService(this.tmpSolutionPriceBandService);
		
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		
		long procurementId = initiateModel.getProcurementId();
		Object rtnObject = procurementSecurityValidation(secInfo, procurementId, attr);
		if (rtnObject instanceof String) {
			return (String) rtnObject;
		}
		Procurement procurement = (Procurement)rtnObject;
		
		initiateModel.DIRECTAWARD_MAXVALUE = Integer.valueOf(DIRECTAWARD_MAXVALUE);
		setupModelCollections(initiateModel, procurement, HttpMethod.POST);
		
		// Validate that if a direct award is being made for a solution, that the value is less than the threshold
		if (!bindingResult.hasErrors()) {	
			if (!bindingResult.hasErrors() && initiateModel.contractTermMonths == null) {
				bindingResult.addError(new ObjectError("directAward", "Please select the contract length"));
			}
			
			if (!bindingResult.hasErrors() && initiateModel.directAwardBundleId != null && initiateModel.directAwardBundleId.trim().length() > 0) {
				if (initiateModel.getPriceOverTermForBundle(Long.valueOf(initiateModel.directAwardBundleId), 0, 1000, 12).compareTo(new BigDecimal(Integer.valueOf(DIRECTAWARD_MAXVALUE))) > 0) {
					bindingResult.addError(new ObjectError("directAward", "Cannot directly award if the value is £" + DIRECTAWARD_MAXVALUE + " or greater"));
				}
			}
		}
		
		// Store any new procurement attributes
		if (!bindingResult.hasErrors()) {
			procurement.setPlannedContractStart(initiateModel.getPlannedContractStart());
			//procurement.setContractMonths(initiateModel.getContractTermMonths());
			procurement.setPatientCount(initiateModel.getNumberOfPatients());
			procurementRepository.save(procurement);
		}
		
		// Store the Term and Patient Count against the Service Recipients
		if (!bindingResult.hasErrors()) {
			int idx = 0;
			for (ProcSrvRecipient sr : initiateModel.getSrvRecipients()) {
				Integer iNewTerm = initiateModel.getContractTermMonths()[idx];
				Integer iNewPatientCount = initiateModel.getPatientCount()[idx];
				if (!iNewTerm.equals(sr.getTerm()) || !iNewPatientCount.equals(sr.getPatientCount())) {
					sr.setTerm(iNewTerm);
					sr.setPatientCount(iNewPatientCount);
					sr = procSrvRecipientRepository.save(sr);
				}
				idx++;
			}
		}
		
		// Reset any fields that lead to actions
		if (!bindingResult.hasErrors()) {
			initiateModel.setRemoveSolutionId("");
			initiateModel.setRemovalReasonId(null);
			initiateModel.setRemovalReasonText("");
			initiateModel.setDirectAwardBundleId(null);
		}		

		setupModelCollections(initiateModel, procurement, HttpMethod.POST); // again
		
		return "buying-process/initiate";	
	}
	
	private Model setupModel(SecurityInfo secInfo, Procurement procurement, Model model, HttpMethod method) {
		
		InitiateModel initiateModel = new InitiateModel();
		initiateModel.setTmpSolutionPriceBandService(this.tmpSolutionPriceBandService);

		initiateModel.DIRECTAWARD_MAXVALUE = Integer.valueOf(DIRECTAWARD_MAXVALUE);
		
		initiateModel.setProcurementId(procurement.getId());
		
		setupModelCollections(initiateModel, procurement, method);
		
		initiateModel.setNumberOfPractices(procurement.getCsvPractices().split(",").length);
		initiateModel.setNumberOfPatients(procurement.getPatientCount() == null ? procurement.getInitialPatientCount() : procurement.getPatientCount());
		initiateModel.setPlannedContractStart(procurement.getPlannedContractStart());
		
		model.addAttribute("initiateModel", initiateModel);
		
		return model;
	}
	
	private void setupModelCollections(InitiateModel initiateModel, Procurement procurement, HttpMethod method) {		
		List<ProcSolutionBundle> bundles = procurement.getBundles();
		// The bundles should be in bundle.id sequence as specified by the @OrderBy directive on the Procurement entity
		initiateModel.getDbBundles().clear();
		initiateModel.getDbBundles().addAll(bundles);
		
		List<ProcSrvRecipient> srvRecipients = procSrvRecipientService.getAllByProcurementOrderByOrganisationName(procurement);
		initiateModel.getSrvRecipients().clear();
		initiateModel.getSrvRecipients().addAll(srvRecipients);
		
		initiateModel.setBundleInfoForBaseSystemPerBundleAndSR(new InitiateModel.BundleInfo[bundles.size()][srvRecipients.size()]);
		int idxBundle = 0;
		for (var bundle : bundles) {
			int idxSR = 0;
			for (var sr : srvRecipients) {
				TmpUnitType bandingUnit = tmpSolutionPriceBandService.getBandingUnitForSolution(bundle.getSolutionId());
				long iBandingUnitType = 0;
				if (bandingUnit != null) {
					iBandingUnitType = bandingUnit.getId();
				}
				InitiateModel.BundleInfo bundleInfo = new InitiateModel.BundleInfo();
				bundleInfo.bundleId = bundle.getId();
				bundleInfo.solutionId = bundle.getSolutionId();
				bundleInfo.name = bundle.getName();
				bundleInfo.priceUnits = bundle.getNumberOfUnits();
				bundleInfo.unitTypeName = tmpSolutionPriceBandService.getUnitTextForSolution(bundle.getSolutionId());
				TmpPriceBasis priceBasis = tmpSolutionPriceBandService.getPriceBasisForSolution(bundle.getSolutionId());
				if (priceBasis != null) {
					TmpUnitType priceBasisUnit1 = priceBasis.getUnit1();
					TmpUnitType priceBasisUnit2 = priceBasis.getUnit2();
					bundleInfo.readonly = priceBasisUnit2 == null 
						&& (priceBasisUnit1.getId() == TmpUnitType.PATIENT || priceBasisUnit1.getId() == TmpUnitType.SERVICE_RECIPIENT);
					if (priceBasisUnit1.getId() == TmpUnitType.PATIENT) {
						bundleInfo.priceUnits = sr.getPatientCount();
					} else
					if (priceBasisUnit1.getId() == TmpUnitType.SERVICE_RECIPIENT) {
						bundleInfo.priceUnits = srvRecipients.size();
					}
				} else {
					bundleInfo.unitTypeName = "";
					bundleInfo.readonly = false;
				}
				
				if (bandingUnit != null) {
					if (iBandingUnitType == TmpUnitType.PATIENT) {
						bundleInfo.bandingUnits = sr.getPatientCount();
					} else
					if (iBandingUnitType == TmpUnitType.SERVICE_RECIPIENT) {
						bundleInfo.bandingUnits = srvRecipients.size();
					}
				}
				
				bundleInfo.unitPrice = initiateModel.getUnitPriceForBundle(bundleInfo.bundleId, bundleInfo.bandingUnits==null?0:bundleInfo.bandingUnits);
				bundleInfo.price = initiateModel.getPriceForBundle(bundleInfo.bundleId, 
						bundleInfo.bandingUnits==null?0:bundleInfo.bandingUnits, 
						bundleInfo.priceUnits==null?0:bundleInfo.priceUnits);
				bundleInfo.priceOverTerm = initiateModel.getPriceOverTermForBundle(bundleInfo.bundleId, 
						bundleInfo.bandingUnits==null?0:bundleInfo.bandingUnits, 
						bundleInfo.priceUnits==null?0:bundleInfo.priceUnits, 
						sr.getTerm()==null?12:sr.getTerm());
				initiateModel.getBundleInfoForBaseSystemPerBundleAndSR()[idxBundle][idxSR] = bundleInfo;
				idxSR++;
			}
			idxBundle++;
		}
		
		
		
		Framework framework = frameworkService.getDefaultFramework();
		initiateModel.setPossibleContractTermMonths(IntStream.rangeClosed(1, framework.getMaxTermMonths()).toArray());
		if (initiateModel.getContractTermMonths().length == 0) {
			initiateModel.setContractTermMonths(new Integer[srvRecipients.size()]);
		}
		if (initiateModel.getPatientCount().length == 0) {
			initiateModel.setPatientCount(new Integer[srvRecipients.size()]);
		}
		if (method.equals(HttpMethod.GET)) {
			int idx = 0;
			for (ProcSrvRecipient sr : srvRecipients) {
				initiateModel.getContractTermMonths()[idx] = sr.getTerm();
				initiateModel.getPatientCount()[idx] = sr.getPatientCount();
				idx++;
			}
		}
		
		// Add each bundle's possible Associated Services into their key value
		for (ProcSolutionBundle bundle : bundles) {
			List<TmpAssociatedService> bundleAssociatedServices = procSolutionBundleService.getAssociatedServicesForBundle(bundle);
			initiateModel.getPossibleBundleAssociatedServices().put(bundle.getId(), bundleAssociatedServices);
		}
	}
	
	private Object procurementSecurityValidation(SecurityInfo secInfo, long procurementId, RedirectAttributes attr) {
		if (procurementId == 0) {
        	String message = "Unidentified route to Initiate";
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
			
			if (procurement.getStatus().getId() != ProcStatus.INITIATE) {
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
