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
import uk.nhs.gpitf.purchasing.entities.ProcBundleSrService;
import uk.nhs.gpitf.purchasing.entities.ProcShortlist;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundle;
import uk.nhs.gpitf.purchasing.entities.ProcSolutionBundleItem;
import uk.nhs.gpitf.purchasing.entities.ProcSrvRecipient;
import uk.nhs.gpitf.purchasing.entities.ProcStatus;
import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.entities.ServiceType;
import uk.nhs.gpitf.purchasing.entities.TmpAdditionalService;
import uk.nhs.gpitf.purchasing.entities.TmpAssociatedService;
import uk.nhs.gpitf.purchasing.entities.TmpPriceBasis;
import uk.nhs.gpitf.purchasing.entities.TmpUnitType;
import uk.nhs.gpitf.purchasing.models.InitiateModel;
import uk.nhs.gpitf.purchasing.models.InitiateModel.RowDetail;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcBundleSrServiceRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcShortlistRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSolutionBundleItemRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSolutionBundleRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcSrvRecipientRepository;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.FrameworkService;
import uk.nhs.gpitf.purchasing.services.OnboardingService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.ProcBundleSrServiceService;
import uk.nhs.gpitf.purchasing.services.ProcShortlistRemovalReasonService;
import uk.nhs.gpitf.purchasing.services.ProcSolutionBundleService;
import uk.nhs.gpitf.purchasing.services.ProcSrvRecipientService;
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

	@Autowired
	ProcBundleSrServiceRepository procBundleSrServiceRepository;
	
	@Autowired
	ProcBundleSrServiceService procBundleSrServiceService;
	
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
		
		setupModel(secInfo, procurement, model, true);
		
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
		
		setupModel(secInfo, procurement, model, true);
		
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
		setupModelCollections(initiateModel, procurement, false);
		
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
		
		// Store Associated & Additional Services
		List<ProcSolutionBundle> bundles = procurement.getBundles();
		List<ProcSrvRecipient> srvRecipients = procSrvRecipientService.getAllByProcurementOrderByOrganisationName(procurement);
		int idxBundle = 0;
		for (var bundle : bundles) {
			List<ProcBundleSrService> allBundleServices = procBundleSrServiceService.getAllForBundleOrderById(bundle);

			int idxSR = 0;
			for (var sr : srvRecipients) {
				InitiateModel.RowDetail rowDetail = setupRowDetails(bundle, sr, null, srvRecipients.size(), initiateModel, ServiceType.BASE_SOLUTION, allBundleServices).get(0);
				InitiateModel.RowDetail[] rowDetails = setupRowDetails(bundle, sr, null, srvRecipients.size(), initiateModel, ServiceType.ASSOCIATED_SERVICE, allBundleServices)
						.toArray(new InitiateModel.RowDetail[] {});
				
				// Store Associated Services
				List<ProcBundleSrService> bundleServices = getBundleServicesForServiceType(bundle, sr, ServiceType.ASSOCIATED_SERVICE, allBundleServices);
				List<ProcBundleSrService> toRemoveBundleServices = new ArrayList<>();
				// . add/amend
				for (int idxPostedAssociatedService=0; idxPostedAssociatedService<initiateModel.assocSrv[idxBundle][idxSR].length; idxPostedAssociatedService++) {
					if (StringUtils.isNotEmpty(initiateModel.assocSrv[idxBundle][idxSR][idxPostedAssociatedService])) {
						try {
							String postedAssociatedService = initiateModel.assocSrv[idxBundle][idxSR][idxPostedAssociatedService];
							Optional<ProcBundleSrService> optPbss = bundleServices.stream().filter(s -> s.getAssociatedService().equals(postedAssociatedService)).findFirst();
							ProcBundleSrService pbss = null;
							if (optPbss.isEmpty()) {
								pbss = new ProcBundleSrService();
								pbss.setBundle(bundle);
								pbss.setServiceRecipient(sr);
								pbss.setServiceType((ServiceType) GUtils.makeObjectForId(ServiceType.class, ServiceType.ASSOCIATED_SERVICE));
								pbss.setAssociatedService(initiateModel.assocSrv[idxBundle][idxSR][idxPostedAssociatedService]);
							} else {
								pbss = optPbss.get();
							}
							Integer postedUnits = initiateModel.assocSrvUnits[idxBundle][idxSR][idxPostedAssociatedService];
							if (postedUnits == null && pbss.getNumberOfUnits() != null || postedUnits != null && pbss.getNumberOfUnits() == null
							 || (postedUnits != null && pbss.getNumberOfUnits() != null && postedUnits.intValue() != pbss.getNumberOfUnits().intValue())
							 || optPbss.isEmpty()) {
								pbss.setNumberOfUnits(initiateModel.assocSrvUnits[idxBundle][idxSR][idxPostedAssociatedService]);
								procBundleSrServiceRepository.save(pbss);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// . delete (for each database record, if it's not in the posted set then delete it)
				for (ProcBundleSrService pbss : bundleServices) {
					boolean exists = false;
					for (String assocService : initiateModel.assocSrv[idxBundle][idxSR]) {
						if (assocService.equals(pbss.getAssociatedService())) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						toRemoveBundleServices.add(pbss);
					}
				}
				procBundleSrServiceRepository.deleteAll(toRemoveBundleServices);
				
				
				idxSR++;
			}
			idxBundle++;
		}
		
		// Reset any fields that lead to actions
		if (!bindingResult.hasErrors()) {
			initiateModel.setRemoveSolutionId("");
			initiateModel.setRemovalReasonId(null);
			initiateModel.setRemovalReasonText("");
			initiateModel.setDirectAwardBundleId(null);
		}		

		setupModelCollections(initiateModel, procurement, true); // again
		
		return "buying-process/initiate";	
	}
	
	private Model setupModel(SecurityInfo secInfo, Procurement procurement, Model model, boolean bIncludePostableData) {
		
		InitiateModel initiateModel = new InitiateModel();
		initiateModel.setTmpSolutionPriceBandService(this.tmpSolutionPriceBandService);

		initiateModel.DIRECTAWARD_MAXVALUE = Integer.valueOf(DIRECTAWARD_MAXVALUE);
		
		initiateModel.setProcurementId(procurement.getId());
		
		setupModelCollections(initiateModel, procurement, bIncludePostableData);
		
		initiateModel.setNumberOfPractices(procurement.getCsvPractices().split(",").length);
		initiateModel.setNumberOfPatients(procurement.getPatientCount() == null ? procurement.getInitialPatientCount() : procurement.getPatientCount());
		initiateModel.setPlannedContractStart(procurement.getPlannedContractStart());
		
		model.addAttribute("initiateModel", initiateModel);
		
		return model;
	}
	
	private void setupModelCollections(InitiateModel initiateModel, Procurement procurement, boolean bIncludePostableData) {		
		List<ProcSolutionBundle> bundles = procurement.getBundles();
		// The bundles should be in bundle.id sequence as specified by the @OrderBy directive on the Procurement entity
		initiateModel.getDbBundles().clear();
		initiateModel.getDbBundles().addAll(bundles);
		
		List<ProcSrvRecipient> srvRecipients = procSrvRecipientService.getAllByProcurementOrderByOrganisationName(procurement);
		initiateModel.getSrvRecipients().clear();
		initiateModel.getSrvRecipients().addAll(srvRecipients);
		
		initiateModel.setRowDetailForBaseSystemPerBundleAndSR(	new InitiateModel.RowDetail[bundles.size()][srvRecipients.size()]);
		initiateModel.setRowDetailForAssocSrvPerBundleAndSR(  	new InitiateModel.RowDetail[bundles.size()][srvRecipients.size()][0]);
		initiateModel.setRowDetailForAdditSrvPerBundleAndSR(  	new InitiateModel.RowDetail[bundles.size()][srvRecipients.size()][0]);
		
		if (bIncludePostableData) {
			initiateModel.setAssocSrv(  						  	new String[bundles.size()][srvRecipients.size()][0]);
			initiateModel.setAssocSrvUnits(						  	new Integer[bundles.size()][srvRecipients.size()][0]);
			initiateModel.setAdditSrv(  						  	new String[bundles.size()][srvRecipients.size()][0]);
			initiateModel.setAdditSrvUnits(						  	new Integer[bundles.size()][srvRecipients.size()][0]);
			initiateModel.setAdditAssocSrv(  					  	new String[bundles.size()][srvRecipients.size()][0][0]);
			initiateModel.setAdditAssocSrvUnits(					new Integer[bundles.size()][srvRecipients.size()][0][0]);
			int idxBundle = 0;
			for (var bundle : bundles) {
				List<ProcBundleSrService> bundleServices = procBundleSrServiceService.getAllForBundleOrderById(bundle);
	
				int idxSR = 0;
				for (var sr : srvRecipients) {
					InitiateModel.RowDetail rowDetail = setupRowDetails(bundle, sr, null, srvRecipients.size(), initiateModel, ServiceType.BASE_SOLUTION, bundleServices).get(0);				
					initiateModel.getRowDetailForBaseSystemPerBundleAndSR()[idxBundle][idxSR] = rowDetail;
					
					InitiateModel.RowDetail[] rowDetails = setupRowDetails(bundle, sr, null, srvRecipients.size(), initiateModel, ServiceType.ASSOCIATED_SERVICE, bundleServices)
							.toArray(new InitiateModel.RowDetail[] {});
					initiateModel.getRowDetailForAssocSrvPerBundleAndSR()[idxBundle][idxSR] = rowDetails;
					List<String> lstAssocSrv = new ArrayList<>();
					List<Integer> lstAssocSrvUnits = new ArrayList<>();
					for (var rd : rowDetails) {
						lstAssocSrv.add(rd.associatedService);
						lstAssocSrvUnits.add(rd.priceUnits);
					}
					initiateModel.getAssocSrv()[idxBundle][idxSR] = lstAssocSrv.toArray(new String[] {});
					initiateModel.getAssocSrvUnits()[idxBundle][idxSR] = lstAssocSrvUnits.toArray(new Integer[] {});
	
					rowDetails = setupRowDetails(bundle, sr, null, srvRecipients.size(), initiateModel, ServiceType.ADDITIONAL_SERVICE, bundleServices)
							.toArray(new InitiateModel.RowDetail[] {});
					initiateModel.getRowDetailForAdditSrvPerBundleAndSR()[idxBundle][idxSR] = rowDetails;
					List<String> lstAdditSrv = new ArrayList<>();
					List<Integer> lstAdditSrvUnits = new ArrayList<>();
					for (var rd : rowDetails) {
						lstAdditSrv.add(rd.additionalService);
						lstAdditSrvUnits.add(rd.priceUnits);
					}
					initiateModel.getAdditSrv()[idxBundle][idxSR] = lstAdditSrv.toArray(new String[] {});
					initiateModel.getAdditSrvUnits()[idxBundle][idxSR] = lstAdditSrvUnits.toArray(new Integer[] {});
				
					initiateModel.getAdditAssocSrv()[idxBundle][idxSR] = new String[lstAdditSrv.size()][0];				
					initiateModel.getAdditAssocSrvUnits()[idxBundle][idxSR] = new Integer[lstAdditSrvUnits.size()][0];				
					for (int idxAdditSvc=0; idxAdditSvc<lstAdditSrv.size(); idxAdditSvc++) {
						lstAssocSrv = new ArrayList<>();
						lstAssocSrvUnits = new ArrayList<>();
						for (var rd : initiateModel.getRowDetailForAdditSrvPerBundleAndSR()[idxBundle][idxSR][idxAdditSvc].additAssociatedServices) {
							lstAssocSrv.add(rd.associatedService);
							lstAssocSrvUnits.add(rd.priceUnits);
						}
						initiateModel.getAdditAssocSrv()[idxBundle][idxSR][idxAdditSvc] = lstAssocSrv.toArray(new String[] {});
						initiateModel.getAdditAssocSrvUnits()[idxBundle][idxSR][idxAdditSvc] = lstAssocSrvUnits.toArray(new Integer[] {});
					}
					
					idxSR++;
				}
				idxBundle++;
			}
		}
		
		
		
		Framework framework = frameworkService.getDefaultFramework();
		initiateModel.setPossibleContractTermMonths(IntStream.rangeClosed(1, framework.getMaxTermMonths()).toArray());
		if (initiateModel.getContractTermMonths().length == 0) {
			initiateModel.setContractTermMonths(new Integer[srvRecipients.size()]);
		}
		if (initiateModel.getPatientCount().length == 0) {
			initiateModel.setPatientCount(new Integer[srvRecipients.size()]);
		}
		if (bIncludePostableData) {
			int idx = 0;
			for (ProcSrvRecipient sr : srvRecipients) {
				initiateModel.getContractTermMonths()[idx] = sr.getTerm();
				initiateModel.getPatientCount()[idx] = sr.getPatientCount();
				idx++;
			}
		}
		
		// Add each bundle's possible Associated Services into their key value
		initiateModel.getPossibleBundleAssociatedServices().clear();
		for (ProcSolutionBundle bundle : bundles) {
			List<TmpAssociatedService> bundleAssociatedServices = procSolutionBundleService.getAssociatedServicesForBundle(bundle);
			initiateModel.getPossibleBundleAssociatedServices().put(bundle.getId(), bundleAssociatedServices);
		}
		
		// Add each bundle's possible Additional Services into their key value
		initiateModel.getPossibleBundleAdditionalServices().clear();
		for (ProcSolutionBundle bundle : bundles) {
			List<TmpAdditionalService> bundleAdditionalServices = procSolutionBundleService.getAdditionalServicesForBundle(bundle);
			initiateModel.getPossibleBundleAdditionalServices().put(bundle.getId(), bundleAdditionalServices);
		}
		
		// Add each Additional Service's possible Additional Services into their key value
		initiateModel.getPossibleAdditAssociatedServices().clear();
		for (List<TmpAdditionalService> lstAddSvc : initiateModel.getPossibleBundleAdditionalServices().values()) {
			for (TmpAdditionalService addSvc : lstAddSvc) {
				if (!initiateModel.getPossibleAdditAssociatedServices().containsKey(addSvc.getAdditionalServiceId())) {
					List<TmpAssociatedService> assocSvc = procSolutionBundleService.getAssociatedServicesForAdditionalService(addSvc.getAdditionalServiceId());
					initiateModel.getPossibleAdditAssociatedServices().put(addSvc.getAdditionalServiceId(), assocSvc);
				}
			}
		}
		
		if (bIncludePostableData) {
			addEmptyRowToCollections(initiateModel);
		}
	}
	
	private void addEmptyRowToCollections(InitiateModel initiateModel) {
		RowDetail[][][] assocSrvs = initiateModel.getRowDetailForAssocSrvPerBundleAndSR();
		String[][][] assocSrv = initiateModel.getAssocSrv() ;
		Integer[][][] assocSrvUnits = initiateModel.getAssocSrvUnits() ;
		RowDetail[][][] additSrvs = initiateModel.getRowDetailForAdditSrvPerBundleAndSR();
		String[][][] additSrv = initiateModel.getAdditSrv() ;
		Integer[][][] additSrvUnits = initiateModel.getAdditSrvUnits() ;
		String[][][][] additAssocSrv = initiateModel.getAdditAssocSrv() ;
		Integer[][][][] additAssocSrvUnits = initiateModel.getAdditAssocSrvUnits() ;

		for (int idxBundle=0; idxBundle<assocSrvs.length; idxBundle++) {
			for (int idxSR=0; idxSR<assocSrvs[idxBundle].length; idxSR++) {
				// Associated Services
				List<RowDetail> list = new ArrayList(List.of(assocSrvs[idxBundle][idxSR]));
				RowDetail rdElement = new RowDetail();
				rdElement.bundleId = initiateModel.getDbBundles().get(idxBundle).getId();
				rdElement.readonly = true;
				list.add(rdElement);
				assocSrvs[idxBundle][idxSR] = list.toArray(new RowDetail[] {});
				
				List<String> lstAssocSrv = new ArrayList(List.of(assocSrv[idxBundle][idxSR]));
				lstAssocSrv.add("");
				assocSrv[idxBundle][idxSR] = lstAssocSrv.toArray(new String[] {});
				
				List<Integer> lstAssocSrvUnits = new ArrayList(List.of(assocSrvUnits[idxBundle][idxSR]));
				lstAssocSrvUnits.add(null);
				assocSrvUnits[idxBundle][idxSR] = lstAssocSrvUnits.toArray(new Integer[] {});
				
			}
		}
		for (int idxBundle=0; idxBundle<additSrvs.length; idxBundle++) {
			for (int idxSR=0; idxSR<additSrvs[idxBundle].length; idxSR++) {
				// Additional Service
				List<RowDetail> list = new ArrayList(List.of(additSrvs[idxBundle][idxSR]));
				RowDetail rdElement = new RowDetail();
				rdElement.bundleId = initiateModel.getDbBundles().get(idxBundle).getId();
				rdElement.readonly = true;
				list.add(rdElement);
				additSrvs[idxBundle][idxSR] = list.toArray(new RowDetail[] {});
				
				List<String> lstAdditSrv = new ArrayList(List.of(additSrv[idxBundle][idxSR]));
				lstAdditSrv.add("");
				additSrv[idxBundle][idxSR] = lstAdditSrv.toArray(new String[] {});
				
				List<Integer> lstAdditSrvUnits = new ArrayList(List.of(additSrvUnits[idxBundle][idxSR]));
				lstAdditSrvUnits.add(null);
				additSrvUnits[idxBundle][idxSR] = lstAdditSrvUnits.toArray(new Integer[] {});
				
				// Additional Service's Associated Services
				for (int idxAddit=0; idxAddit<additAssocSrv[idxBundle][idxSR].length; idxAddit++) {
					List<String> lstAssocSrv = new ArrayList(List.of(additAssocSrv[idxBundle][idxSR][idxAddit]));
					lstAssocSrv.add("");
					additAssocSrv[idxBundle][idxSR][idxAddit] = lstAssocSrv.toArray(new String[] {});

					List<Integer> lstAssocSrvUnits = new ArrayList(List.of(additAssocSrvUnits[idxBundle][idxSR][idxAddit]));
					lstAssocSrvUnits.add(null);
					additAssocSrvUnits[idxBundle][idxSR][idxAddit] = lstAssocSrvUnits.toArray(new Integer[] {});
					
					if (lstAssocSrv.size() > 1) {
						list = new ArrayList(List.of(initiateModel.getRowDetailForAdditSrvPerBundleAndSR()[idxBundle][idxSR][idxAddit].additAssociatedServices));
						rdElement = new RowDetail();
						rdElement.bundleId = initiateModel.getDbBundles().get(idxBundle).getId();
						rdElement.readonly = true;
						list.add(rdElement);
						initiateModel.getRowDetailForAdditSrvPerBundleAndSR()[idxBundle][idxSR][idxAddit].additAssociatedServices = list.toArray(new RowDetail[] {});
					}
				}
			}
		}
		
		// The multi-dimensional arrays need to be set up to be able to accommodate the on-screen postback data. 
		// On postback, the aaarrayDimensions is posted first (because of the name I assume) and so it is able
		// to initialise the multi-dimensional arrays before their values are posted back.
		int max2ndDim = 0;
		int max3rdDim = 0;
		for (int idxBundle=0; idxBundle<assocSrvs.length; idxBundle++) {
			for (int idxSR=0; idxSR<assocSrvs[idxBundle].length; idxSR++) {
				if (assocSrvs[idxBundle][idxSR].length > max2ndDim) {
					max2ndDim = assocSrvs[idxBundle][idxSR].length;
				}
				if (additSrvs[idxBundle][idxSR].length > max2ndDim) {
					max2ndDim = additSrvs[idxBundle][idxSR].length;
				}
				for (int idxAddit=0; idxAddit<additAssocSrv[idxBundle][idxSR].length; idxAddit++) {
					if (additAssocSrv[idxBundle][idxSR][idxAddit].length > max3rdDim) {
						max3rdDim = additAssocSrv[idxBundle][idxSR][idxAddit].length;
					}
				}
			}
		}
		String arrayDimensions = initiateModel.getDbBundles().size() + "," + initiateModel.getSrvRecipients().size() + ","+
				max2ndDim + "," + max3rdDim;
		
		initiateModel.setAaarrayDimensions(arrayDimensions);
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
	
	private List<InitiateModel.RowDetail> setupRowDetails(ProcSolutionBundle bundle, ProcSrvRecipient sr, String additionalService, int serviceRecipientCount, InitiateModel initiateModel, 
			long iServiceType, List<ProcBundleSrService> allBundleServices) {

		List<InitiateModel.RowDetail> rowDetails = new ArrayList<>();
		
		List<ProcBundleSrService> bundleServices = null;
		if (iServiceType != ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE) {
			bundleServices = getBundleServicesForServiceType(bundle, sr, iServiceType, allBundleServices);
		} else {
			bundleServices = getBundleAssocatedServicesForAdditionalService(bundle, sr, additionalService, allBundleServices);
		}
		
		if (bundleServices.size() == 0 && iServiceType == ServiceType.BASE_SOLUTION) {
			bundleServices.add(new ProcBundleSrService());
		}
		
		for (var bundleService : bundleServices) {
			TmpUnitType bandingUnit = null;
			InitiateModel.RowDetail rowDetail = new InitiateModel.RowDetail();
			rowDetail.bundleId = bundle.getId();
			if (iServiceType == ServiceType.BASE_SOLUTION) {
				rowDetail.name = bundle.getName();
				rowDetail.solutionId = bundle.getSolutionId();
			}
			rowDetail.priceUnits = 0;
			if (bundleService.getId() != 0) {
				rowDetail.priceUnits = bundleService.getNumberOfUnits()==null?0:bundleService.getNumberOfUnits();
			}
			
			TmpPriceBasis priceBasis = null;
			if (iServiceType == ServiceType.BASE_SOLUTION) {
				rowDetail.unitTypeName = tmpSolutionPriceBandService.getUnitTextForSolution(bundle.getSolutionId());
				priceBasis = tmpSolutionPriceBandService.getPriceBasisForSolution(bundle.getSolutionId());
				bandingUnit = tmpSolutionPriceBandService.getBandingUnitForSolution(bundle.getSolutionId());
			} else 
			if ((iServiceType == ServiceType.ASSOCIATED_SERVICE || iServiceType == ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE) && bundleService.getId() != 0) {
				rowDetail.unitTypeName = tmpSolutionPriceBandService.getUnitTextForAssociatedService(bundleService.getAssociatedService());
				priceBasis = tmpSolutionPriceBandService.getPriceBasisForAssociatedService(bundleService.getAssociatedService());
				bandingUnit = tmpSolutionPriceBandService.getBandingUnitForAssociatedService(bundleService.getAssociatedService());
			} else 
			if (iServiceType == ServiceType.ADDITIONAL_SERVICE && bundleService.getId() != 0) {
				rowDetail.unitTypeName = tmpSolutionPriceBandService.getUnitTextForAdditionalService(bundleService.getAdditionalService());
				priceBasis = tmpSolutionPriceBandService.getPriceBasisForAdditionalService(bundleService.getAdditionalService());
				bandingUnit = tmpSolutionPriceBandService.getBandingUnitForAdditionalService(bundleService.getAdditionalService());
			}
				
			if (priceBasis != null) {
				TmpUnitType priceBasisUnit1 = priceBasis.getUnit1();
				TmpUnitType priceBasisUnit2 = priceBasis.getUnit2();
				long unit1Id = 0;
	    		if (priceBasisUnit1 != null) {
	    			unit1Id = priceBasisUnit1.getId();
	    		}
				rowDetail.readonly = priceBasisUnit2 == null 
					&& (unit1Id == 0 || unit1Id == TmpUnitType.PATIENT || unit1Id == TmpUnitType.SERVICE_RECIPIENT);
				if (unit1Id == TmpUnitType.PATIENT) {
					rowDetail.priceUnits = sr.getPatientCount();
				} else
				if (unit1Id == TmpUnitType.SERVICE_RECIPIENT) {
					rowDetail.priceUnits = serviceRecipientCount;
				}
				if (priceBasis.isFixedPrice()) {
					rowDetail.priceUnits = 1;
				}
			} else {
				rowDetail.unitTypeName = "";
				rowDetail.readonly = false;
			}
			
			
			if (bandingUnit != null) {
				long iBandingUnitType = bandingUnit.getId();
				if (iBandingUnitType == TmpUnitType.PATIENT) {
					rowDetail.bandingUnits = sr.getPatientCount();
				} else
				if (iBandingUnitType == TmpUnitType.SERVICE_RECIPIENT) {
					rowDetail.bandingUnits = serviceRecipientCount;
				}
			}
			
			if (iServiceType == ServiceType.BASE_SOLUTION) {
				rowDetail.unitPrice = initiateModel.getUnitPriceForBundle(rowDetail.bundleId, rowDetail.bandingUnits==null?0:rowDetail.bandingUnits);
				rowDetail.price = initiateModel.getPriceForBundle(rowDetail.bundleId, 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits);
				rowDetail.priceOverTerm = initiateModel.getPriceOverTermForBundle(rowDetail.bundleId, 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits, 
						sr.getTerm()==null?12:sr.getTerm());
			} else 
			if ((iServiceType == ServiceType.ASSOCIATED_SERVICE || iServiceType == ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE) && bundleService.getId() != 0) {
				rowDetail.unitPrice = tmpSolutionPriceBandService.getUnitPriceForAssociatedService(bundleService.getAssociatedService(), rowDetail.bandingUnits==null?0:rowDetail.bandingUnits);
				rowDetail.price = tmpSolutionPriceBandService.getPriceForAssociatedService(bundleService.getAssociatedService(), 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits);
				rowDetail.priceOverTerm = tmpSolutionPriceBandService.getPriceOverTermForAssociatedService(bundleService.getAssociatedService(), 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits, 
						sr.getTerm()==null?12:sr.getTerm());
				rowDetail.associatedService = bundleService.getAssociatedService();
				if (iServiceType == ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE) {
					rowDetail.additionalService = bundleService.getAdditionalService();
				}
			} else 
			if (iServiceType == ServiceType.ADDITIONAL_SERVICE && bundleService.getId() != 0) {
				rowDetail.unitPrice = tmpSolutionPriceBandService.getUnitPriceForAdditionalService(bundleService.getAdditionalService(), rowDetail.bandingUnits==null?0:rowDetail.bandingUnits);
				rowDetail.price = tmpSolutionPriceBandService.getPriceForAdditionalService(bundleService.getAdditionalService(), 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits);
				rowDetail.priceOverTerm = tmpSolutionPriceBandService.getPriceOverTermForAdditionalService(bundleService.getAdditionalService(), 
						rowDetail.bandingUnits==null?0:rowDetail.bandingUnits, 
						rowDetail.priceUnits==null?0:rowDetail.priceUnits, 
						sr.getTerm()==null?12:sr.getTerm());
				rowDetail.additionalService = bundleService.getAdditionalService();
			}
			
			if (iServiceType == ServiceType.ADDITIONAL_SERVICE) {
				InitiateModel.RowDetail[] rowDetailsOfAdditAssociatedServices = setupRowDetails(bundle, sr, rowDetail.additionalService, serviceRecipientCount, initiateModel, ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE, allBundleServices)
						.toArray(new InitiateModel.RowDetail[] {});
				rowDetail.additAssociatedServices = rowDetailsOfAdditAssociatedServices;
			}
			
			rowDetails.add(rowDetail);
		}
			
		return rowDetails;
	}
/*	
	private ProcBundleSrService getBundleServiceForServiceType(ProcSrvRecipient sr, long iServiceType, List<ProcBundleSrService> bundleServices) {
		for (var bundleService : bundleServices) {
			if (bundleService.getServiceType().getId() == iServiceType 
			 && bundleService.getServiceRecipient().getId() == sr.getId()) {
				return bundleService;
			}
		}
		return null;
	}
*/	
	private List<ProcBundleSrService> getBundleServicesForServiceType(ProcSolutionBundle bundle, ProcSrvRecipient sr, long iServiceType, List<ProcBundleSrService> bundleServices) {
		List <ProcBundleSrService> list = new ArrayList<>();
		for (var bundleService : bundleServices) {
			if (bundleService.getServiceType().getId() == iServiceType 
			 && bundleService.getBundle().getId() == bundle.getId()
			 && bundleService.getServiceRecipient().getId() == sr.getId()) {
				list.add(bundleService);
			}
		}
/*		
		if (iServiceType == ServiceType.ASSOCIATED_SERVICE) {
			list.sort((object1, object2) -> (object1.getAssociatedService()).compareToIgnoreCase(object2.getAssociatedService()));
		} else
		if (iServiceType == ServiceType.ADDITIONAL_SERVICE) {
			list.sort((object1, object2) -> (object1.getAdditionalService()).compareToIgnoreCase(object2.getAdditionalService()));
		}
*/		
		return list;
	}
	
	private List<ProcBundleSrService> getBundleAssocatedServicesForAdditionalService(ProcSolutionBundle bundle, ProcSrvRecipient sr, String additionalService, List<ProcBundleSrService> bundleServices) {
		List <ProcBundleSrService> list = new ArrayList<>();
		for (var bundleService : bundleServices) {
			if (bundleService.getServiceType().getId() == ServiceType.ASSOCIATED_SERVICE_OF_ADDITIONAL_SERVICE 
			 && bundleService.getBundle().getId() == bundle.getId()
			 && bundleService.getServiceRecipient().getId() == sr.getId()
			 && bundleService.getAdditionalService().equals(additionalService)) {
				list.add(bundleService);
			}
		}
/*		
		list.sort((object1, object2) -> (object1.getAssociatedService()).compareToIgnoreCase(object2.getAssociatedService()));
*/		
		return list;
	}
}
