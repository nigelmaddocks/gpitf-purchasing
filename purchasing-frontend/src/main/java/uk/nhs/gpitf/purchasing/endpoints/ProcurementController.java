package uk.nhs.gpitf.purchasing.endpoints;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uk.nhs.gpitf.purchasing.entities.Procurement;
import uk.nhs.gpitf.purchasing.repositories.ProcurementRepository;
import uk.nhs.gpitf.purchasing.services.ProcurementService;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
public class ProcurementController {
	
    public static final String ENDPOINT_UPDATE_PROCUREMENT_WITH_CAPABILITIES 	= "/buyingprocess/updateProcurementWithCapabilities";
    public static final String ENDPOINT_UPDATE_PROCUREMENT_WITH_INTEROPERABLES 	= "/buyingprocess/updateProcurementWithInteroperables";
    public static final String ENDPOINT_UPDATE_PROCUREMENT_WITH_FOUNDATION 		= "/buyingprocess/updateProcurementWithFoundation";
    public static final String ENDPOINT_UPDATE_PROCUREMENT_WITH_PRACTICES 		= "/buyingprocess/updateProcurementWithPractices";
    public static final String ENDPOINT_CREATE_PROCUREMENT 						= "/buyingprocess/createProcurement";
	
	@Autowired
	ProcurementRepository procurementRepository;
	
	@Autowired
	ProcurementService procurementService;
    
    private static final Logger logger = LoggerFactory.getLogger(ProcurementController.class);

    @PostMapping(value = ENDPOINT_UPDATE_PROCUREMENT_WITH_CAPABILITIES, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Procurement updateProcurementWithCapabilitiesPost ( 
    		@RequestBody Map<String,Object> body,
    		HttpServletRequest request
    		) {
    	
    	String sProcurementId = body.get("procurementId").toString();
    	String csvCapabilities = body.get("csvCapabilities").toString();
    	Long procurementId = Long.parseLong(sProcurementId);
    	
    	SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
    	
		if (procurementId != null && procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				
				// Check that the user is authorised to this procurement
				if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
				 && !secInfo.isAdministrator()) {
		        	String message = "view procurement " + procurementId;
		    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
				}
				try {
					procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), Optional.of(csvCapabilities), Optional.empty(), Optional.empty(), Optional.empty());
					return procurement;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
		} else {
			Procurement.PrimitiveProcurement prim = (Procurement.PrimitiveProcurement) request.getSession().getAttribute(Procurement.SESSION_ATTR_NAME);
			if (prim == null) {
				prim = procurementService.createNewPrimitiveProcurement(secInfo);
			}
			prim.setCsvCapabilities(csvCapabilities);
			request.getSession().setAttribute(Procurement.SESSION_ATTR_NAME, prim);
			return new Procurement();
		}
		
    	return null;
    }

    @PostMapping(value = ENDPOINT_UPDATE_PROCUREMENT_WITH_INTEROPERABLES, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Procurement updateProcurementWithInteroperablesPost ( 
    		@RequestBody Map<String,Object> body,
    		HttpServletRequest request
    		) {
    	
    	String sProcurementId = body.get("procurementId").toString();
    	String csvInteroperables = body.get("csvInteroperables").toString();
    	Long procurementId = Long.parseLong(sProcurementId);
    	
    	SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
    	
		if (procurementId != null && procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				
				// Check that the user is authorised to this procurement
				if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
				 && !secInfo.isAdministrator()) {
		        	String message = "view procurement " + procurementId;
		    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
				}
				try {
					procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.of(csvInteroperables), Optional.empty(), Optional.empty());
					return procurement;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
		} else {
			Procurement.PrimitiveProcurement prim = (Procurement.PrimitiveProcurement) request.getSession().getAttribute(Procurement.SESSION_ATTR_NAME);
			if (prim == null) {
				prim = procurementService.createNewPrimitiveProcurement(secInfo);
			}
			prim.setCsvCapabilities(csvInteroperables);
			request.getSession().setAttribute(Procurement.SESSION_ATTR_NAME, prim);			
			return new Procurement();
		}
    	return null;
    }

    @PostMapping(value = ENDPOINT_UPDATE_PROCUREMENT_WITH_FOUNDATION, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Procurement updateProcurementWithFoundationPost ( 
    		@RequestBody Map<String,Object> body,
    		HttpServletRequest request
    		) {
    	
    	String sProcurementId = body.get("procurementId").toString();
    	String sFoundation = body.get("foundation").toString();
    	Long procurementId = Long.parseLong(sProcurementId);
    	
    	SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
    	
		if (procurementId != null && procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				
				// Check that the user is authorised to this procurement
				if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
				 && !secInfo.isAdministrator()) {
		        	String message = "view procurement " + procurementId;
		    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
				}
				try {
					procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Boolean.valueOf(sFoundation)), Optional.empty());
					return procurement;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
		} else {
			Procurement.PrimitiveProcurement prim = (Procurement.PrimitiveProcurement) request.getSession().getAttribute(Procurement.SESSION_ATTR_NAME);
			if (prim == null) {
				prim = procurementService.createNewPrimitiveProcurement(secInfo);
			}
			prim.setFoundation(Boolean.valueOf(sFoundation));
			request.getSession().setAttribute(Procurement.SESSION_ATTR_NAME, prim);
		}
		
    	return null;
    }
    

    @PostMapping(value = ENDPOINT_UPDATE_PROCUREMENT_WITH_PRACTICES, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Procurement updateProcurementWithPracticesPost ( 
    		@RequestBody Map<String,Object> body,
    		HttpServletRequest request
    		) {
    	
    	String sProcurementId = body.get("procurementId").toString();
    	String csvPractices = body.get("csvPractices").toString();
    	Long procurementId = Long.parseLong(sProcurementId);
    	
    	SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
    	
		if (procurementId != null && procurementId != 0) {
			Optional<Procurement> optProcurement = procurementRepository.findById(procurementId);
			if (optProcurement.isPresent()) {
				Procurement procurement = optProcurement.get();
				
				// Check that the user is authorised to this procurement
				if (procurement.getOrgContact().getOrganisation().getId() != secInfo.getOrganisationId()
				 && !secInfo.isAdministrator()) {
		        	String message = "view procurement " + procurementId;
		    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
				}
				try {
					procurement = procurementService.saveCurrentPosition(procurementId, secInfo.getOrgContactId(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(csvPractices));
					return procurement;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
		} else {
			Procurement.PrimitiveProcurement prim = (Procurement.PrimitiveProcurement) request.getSession().getAttribute(Procurement.SESSION_ATTR_NAME);
			if (prim == null) {
				prim = procurementService.createNewPrimitiveProcurement(secInfo);
			}
			prim.setCsvPractices(GUtils.trimCommas(csvPractices));
			request.getSession().setAttribute(Procurement.SESSION_ATTR_NAME, prim);
		}
		
    	return null;
    }
    

    @PostMapping(value = ENDPOINT_CREATE_PROCUREMENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Procurement createProcurement ( 
    		@RequestBody Map<String,Object> body,
    		HttpServletRequest request
    		) {
    	
    	SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		Procurement.PrimitiveProcurement prim = (Procurement.PrimitiveProcurement) request.getSession().getAttribute(Procurement.SESSION_ATTR_NAME);
		if (prim == null) {
			prim = procurementService.createNewPrimitiveProcurement(secInfo);
		}
		try {
			Procurement procurement = procurementService.saveCurrentPosition(prim);
			request.getSession().removeAttribute(Procurement.SESSION_ATTR_NAME);
			return procurement;
		} catch (Exception e) {
			e.printStackTrace();
	    	return null;
		}
    }
}
