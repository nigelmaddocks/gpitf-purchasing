package uk.nhs.gpitf.purchasing.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	/** Determine whether the currently logged-on user can purchase for a particular GP Practice */
	public boolean canPurchaseForGPPractice(HttpServletRequest request, long gpOrganisationId) {
		SecurityInfo secinfo = SecurityInfo.getSecurityInfo(request);
		if (secinfo.isAdministrator()) {
			return true;
		}
		
		if (secinfo.isPurchaser()) {		
			if (isDescendant(secinfo.getOrganisationId(), secinfo.getOrganisationTypeId(), gpOrganisationId, Optional.of(OrgType.GPPRACTICE))) {
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
			if (secinfo.getOrganisationId() == targetOrganisationId) {
				return true;
			}
			
			if (isDescendant(secinfo.getOrganisationId(), secinfo.getOrganisationTypeId(), targetOrganisationId, Optional.empty())) {
				return true;
			}
		}
		return false;
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
						(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_GPPRACTICE));
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
							(RelationshipType) GUtils.makeObjectForId(RelationshipType.class, RelationshipType.CCG_TO_GPPRACTICE));
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
