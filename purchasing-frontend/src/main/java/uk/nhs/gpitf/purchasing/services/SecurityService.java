package uk.nhs.gpitf.purchasing.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.*;
import uk.nhs.gpitf.purchasing.repositories.*;
import uk.nhs.gpitf.purchasing.utils.GUtils;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Service
public class SecurityService {
	
	@Autowired
	OrganisationRepository organisationRepository;
	
	@Autowired
	OrgRelationshipRepository orgRelationshipRepository;

	@Autowired
	OrgContactRepository orgContactRepository;

	/** Determine whether the currently logged-on user can purchase for a particular GP Practice */
	public boolean canPurchaseForGPPractice(HttpServletRequest request, long gpOrganisationId) {
		SecurityInfo secinfo = SecurityInfo.getSecurityInfo(request);
		if (secinfo.isAdministrator()) {
			return true;
		}
		
		if (secinfo.isPurchaser()) {		
			if (isDescendant(secinfo.getOrganisationId(), secinfo.getOrganisationTypeId(), gpOrganisationId, Optional.of(OrgType.PRESCRIBING_PRACTICE))) {
				return true;
			}
		}
		return false;
	}

	/** Determine whether the currently logged-on user can administer an organisation */
	public boolean canAdministerOrganisation(HttpServletRequest request, long targetOrganisationId) {
		SecurityInfo secinfo = SecurityInfo.getSecurityInfo(request);
		if (secinfo.isAdministrator()) {
			return true;
		}
		
		if (secinfo.isLocalAdmin()) {	
			if (targetOrganisationId == 0) {
				return false;
			}
			if (secinfo.getOrganisationId() == targetOrganisationId) {
				return true;
			}
			
			if (isDescendant(secinfo.getOrganisationId(), secinfo.getOrganisationTypeId(), targetOrganisationId, Optional.empty())) {
				return true;
			}
		}
		return false;
	}

	/** Determine whether the currently logged-on user can access procurements */
	public static boolean accessIsDeniedToProcurements(HttpServletRequest request, Optional<Long> optionalOrgContactId, OrgContactRepository orgContactRepo){
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		long orgContactId = getOrgContactId(optionalOrgContactId, secInfo);
		boolean accessDenied = !(isAdmin(secInfo, optionalOrgContactId) || isMemberOfOrg(secInfo, orgContactId, orgContactRepo));
		return accessDenied;
	}

	/** Send security warning for use when logged-on user cannot access procurements */
	public static String sendSecurityWarning(HttpServletRequest request, RedirectAttributes attr, Logger LOGGER) {
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);
		String message = "You cannot see procurements outside of your organisation";
		LOGGER.warn(secInfo.loggerSecurityMessage(message));
		attr.addFlashAttribute("security_message", message);
		return SecurityInfo.SECURITY_ERROR_REDIRECT;
	}

	/** Get Organisation's contact id */
	public static long getOrgContactId(Optional<Long> optionalOrgContactId, SecurityInfo secInfo) {
		long orgContactId;

		if (optionalOrgContactId.isEmpty()) {
			orgContactId = secInfo.getOrgContactId();
		} else {
			orgContactId = optionalOrgContactId.get();
		}
		return orgContactId;
	}

	private static boolean isMemberOfOrg(SecurityInfo secInfo, long orgContactId, OrgContactRepository orgContactRepository) {
		Optional<OrgContact> paramOrgContact = orgContactRepository.findById(orgContactId);
		return paramOrgContact.isPresent() || paramOrgContact.get().getOrganisation().getId() == secInfo.getOrganisationId();
	}

	private static boolean isAdmin(SecurityInfo secInfo, Optional<Long> optionalOrgContactId) {
		return secInfo.isAdministrator() && optionalOrgContactId.isPresent();
	}

	/**
	 * Checks whether a organisation is a descendant (child, grandchild etc) or another organisation.
	 * 
	 * If you know the Organisation type of the parentOrgId, please use {@link #isDescendant(long, long, long)}
	 * 
	 * @see #isDescendant(long, long, long) 
	 * 
	 * @param parentOrgId
	 * @param descendantOrgId
	 * @return descendant (true) or not (false)
	 */
	@SuppressWarnings("unused")
	private boolean isDescendant(long parentOrgId, long descendantOrgId, Optional<Long>descendantOrgTypeId) {
		Optional<Organisation> optOrg = organisationRepository.findById(parentOrgId);
		if (optOrg.isEmpty()) {
			return false;
		}
		
		return isDescendant(parentOrgId, optOrg.get().getOrgType().getId(), descendantOrgId, descendantOrgTypeId);
	}
	
	/**
	 * Checks whether a organisation is a descendant (child, grandchild etc) or another organisation.
	 * Currently works for CCG --> GP or CSU --> via CCG --> GP
	 * @param parentOrgId
	 * @param startingOrgTypeId
	 * @param descendantOrgId
	 * @return descendant (true) or not (false)
	 */
	private boolean isDescendant(long parentOrgId, long startingOrgTypeId, long descendantOrgId, Optional<Long>descendantOrgTypeId) {
		
		// For CCGs, check that the CCG has a relationship with the descendant organisation
		if (startingOrgTypeId == OrgType.CCG) {
			try {
				Iterable<OrgRelationship> iterOrgRelGP =
					orgRelationshipRepository.findAllByParentOrgAndChildOrgAndRelationshipType(
						(Organisation) GUtils.makeObjectForId(Organisation.class, parentOrgId), 
						(Organisation) GUtils.makeObjectForId(Organisation.class, descendantOrgId), 
						(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_PRACTICE));
				if (iterOrgRelGP.iterator().hasNext()) {
					if (descendantOrgTypeId.isEmpty()) {
						return true;
					} else {
						return iterOrgRelGP.iterator().next().getChildOrg().getOrgType().getId() == descendantOrgTypeId.get();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else 
		
		// For CSUs, check that for each of their CCGs, one CCG has a relationship with the descendant organisation
		if (startingOrgTypeId == OrgType.CSU) {
			if (descendantOrgTypeId.isEmpty()) {
				try {
					Iterable<OrgRelationship> iterOrgRelCCG =
						orgRelationshipRepository.findAllByParentOrgAndRelationshipType(
							(Organisation) GUtils.makeObjectForId(Organisation.class, parentOrgId), 
							(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG));
					for (OrgRelationship orgRelToCCG : iterOrgRelCCG) {
						Organisation childOrg = orgRelToCCG.getChildOrg();
						if (childOrg.getId() == descendantOrgId) {
							if (descendantOrgTypeId.isEmpty()) {
								return true;
							} else {
								return childOrg.getOrgType().getId() == descendantOrgTypeId.get();
							}							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
			
			
			try {
				Iterable<OrgRelationship> iterOrgRelCCG =
					orgRelationshipRepository.findAllByParentOrgAndRelationshipType(
						(Organisation) GUtils.makeObjectForId(Organisation.class, parentOrgId), 
						(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CSU_TO_CCG));
				for (OrgRelationship orgRelToCCG : iterOrgRelCCG) {
					Iterable<OrgRelationship> iterOrgRelGP =
						orgRelationshipRepository.findAllByParentOrgAndChildOrgAndRelationshipType(
							orgRelToCCG.getChildOrg(), 
							(Organisation) GUtils.makeObjectForId(Organisation.class, descendantOrgId), 
							(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_PRACTICE));
					if (iterOrgRelGP.iterator().hasNext()) {
						if (descendantOrgTypeId.isEmpty()) {
							return true;
						} else {
							return iterOrgRelGP.iterator().next().getChildOrg().getOrgType().getId() == descendantOrgTypeId.get();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}				
		}
		
		return false;		
	}
	
}
