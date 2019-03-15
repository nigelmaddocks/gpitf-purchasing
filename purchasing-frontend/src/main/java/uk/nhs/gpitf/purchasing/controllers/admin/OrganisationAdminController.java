package uk.nhs.gpitf.purchasing.controllers.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.models.OrganisationEditModel;
import uk.nhs.gpitf.purchasing.repositories.ContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgRelationshipRepository;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.services.OrgContactService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.SecurityService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Controller
public class OrganisationAdminController {

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrgRelationshipRepository orgRelationshipRepository;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private OrgRelationshipService orgRelationshipService;

    @Autowired
    private OrgContactRepository orgContactRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private OrgContactService orgContactService;

    @Autowired
    private SecurityService securityService;

    @Autowired
 	private Validator validator;

    private static final Logger logger = LoggerFactory.getLogger(OrganisationAdminController.class);

	@GetMapping("/admin")
	public String getAdminMenu(Model model, HttpServletRequest request, Principal principal) {
		Breadcrumbs.register("Admin", request);

        return "adminMenu";
    }

	@GetMapping("/organisationAdmin/{id}")
	public String getOrganisationById(@PathVariable Long id, Model model, HttpServletRequest request) {
		model = getOrganisationModel(request, id, model);
		long iOrgType = ((OrganisationEditModel) model.asMap().get("organisationEditModel")).getOrganisation().getOrgType().getId();
		String sShortOrgType = "";
		switch ((int)iOrgType) {
			case (int)OrgType.PRESCRIBING_PRACTICE: sShortOrgType = "Practice"; break;
			case (int)OrgType.CCG: sShortOrgType = "CCG"; break;
			case (int)OrgType.CSU: sShortOrgType = "CSU"; break;
		}
		Breadcrumbs.register("View " + sShortOrgType, request);
        return "admin/organisationView";
    }

	@GetMapping("/organisationAdmin/edit/{id}")
	public String getOrganisationForEditById(@PathVariable Long id, Model model, RedirectAttributes attr, HttpServletRequest request) {
        // Check the user is authorised to do this
        if (!securityService.canAdministerOrganisation(request, id)) {
        	String message = "create/update organisation " + id;
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
        }

		model = getOrganisationModel(request, id, model);
        return "admin/organisationEdit";
    }

	@PostMapping("/organisationAdmin/{id}")
	public String updateOrganisation(@Valid
			OrganisationEditModel orgEditModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {

        Organisation org = orgEditModel.getOrganisation();

		logger.debug("create/update organisation " + (org == null? 0L : org.getId()));

        // Check the user is authorised to do this
        if (!securityService.canAdministerOrganisation(request, org == null? 0L : org.getId())) {
        	String message = "create/update organisation " + (org == null? 0L : org.getId());
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", "You attempted to " + message + " but you are not authorised");
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
        }

        if (!bindingResult.hasErrors() && orgEditModel.getNewContact().hasAnyPropertySet()) {
        	Errors errors = new BeanPropertyBindingResult(orgEditModel.getNewContact(), "");
        	validator.validate(orgEditModel.getNewContact(), errors);
        	for (var fe : errors.getFieldErrors()) {
        		FieldError fe2 = new FieldError("", "newContact." + fe.getField(), fe.getRejectedValue(), false, null, null, fe.getDefaultMessage());
        		bindingResult.addError(fe2);
        	}
        }

        if (!bindingResult.hasErrors()) {

        	// Delete the parent relationships the user flagged
        	if (orgEditModel.getDeleteOrgRelationships() != null) {
		        for (String deleteFlag : orgEditModel.getDeleteOrgRelationships()) {
		        	long iOrgRelationship = Long.parseLong(deleteFlag.split("-")[0]);
		        	Optional<OrgRelationship> optRelationship = orgRelationshipRepository.findById(iOrgRelationship);
		        	if (optRelationship.isEmpty()) {
		                bindingResult.addError(new ObjectError("Parent relationship " + iOrgRelationship,  " was not found"));
		        	} else {
		        		OrgRelationship orgRelationship = optRelationship.get();
		        		if (orgRelationship.getChildOrg().getOrgCode().equals(deleteFlag.split("-")[1])
		        		 && orgRelationship.getParentOrg().getOrgCode().equals(deleteFlag.split("-")[2])) {
		        			try {
		        				orgRelationshipRepository.delete(orgRelationship);
		        	        } catch (DataAccessException dae) {
		    		            bindingResult.addError(new ObjectError("Error on delete of parent relationship",  dae.getMessage()));
		        			}
		        		} else {
				            bindingResult.addError(new ObjectError("Data Integrity",  "Could not identify correct parent organisation relationship to delete"));
		        		}
		        	}
		        }
        	}

        	// Delete the contacts the user flagged
        	if (orgEditModel.getDeleteContacts() != null) {
		        for (String deleteFlag : orgEditModel.getDeleteContacts()) {
		        	long iOrgContact = Long.parseLong(deleteFlag.split("-")[0]);
		        	Optional<OrgContact> optOrgContact = orgContactRepository.findById(iOrgContact);
		        	if (optOrgContact.isEmpty()) {
		                bindingResult.addError(new ObjectError("OrgContact " + iOrgContact,  " was not found"));
		        	} else {
		        		OrgContact orgContact = optOrgContact.get();
		        		if (orgContact.getContact().getEmail().equalsIgnoreCase(deleteFlag.split("-")[1])) {
		        			try {
		        				orgContact.setDeleted(true);
		        				orgContactRepository.save(orgContact);
		        	        } catch (DataAccessException dae) {
		    		            bindingResult.addError(new ObjectError("Error on update of OrgContact",  dae.getMessage()));
		        			}
		        		} else {
				            bindingResult.addError(new ObjectError("Data Integrity",  "Could not identify correct OrgContact to mark as deleted"));
		        		}
		        	}
		        }
        	}
        }


        if (!bindingResult.hasErrors()) {
	        RelationshipType parentRelationshipType = new RelationshipType();

	        if (org.getOrgType().getId() == OrgType.PRESCRIBING_PRACTICE) {
	        	parentRelationshipType.setId(RelationshipType.CCG_TO_PRACTICE);
	        } else if (org.getOrgType().getId() == OrgType.CCG) {
	        	parentRelationshipType.setId(RelationshipType.CSU_TO_CCG);
	        }

	        if (orgEditModel.getNewParentOrgId() != 0 && parentRelationshipType != null) {
	        	OrgRelationship orgRelationship = new OrgRelationship();
	        	orgRelationship.setChildOrg(org);
	        	orgRelationship.setParentOrg(organisationRepository.findById(orgEditModel.getNewParentOrgId()).get());
	        	orgRelationship.setRelationshipType(parentRelationshipType);
		        try {
		        	orgRelationshipRepository.save(orgRelationship);
		        } catch (DataAccessException dae) {
		            bindingResult.addError(new ObjectError("Error on save of new parent relationship",  dae.getMessage()));
		        }
	        }
        }

        if (!bindingResult.hasErrors()) {
	        try {
	        	organisationRepository.save(orgEditModel.getOrganisation());
	        } catch (DataAccessException dae) {
	            bindingResult.addError(new ObjectError("Error on save Organisation",  dae.getMessage()));
	        }
        }

        // Add new OrgContact
        if (!bindingResult.hasErrors() && orgEditModel.getNewContact().hasAnyPropertySet()) {
        	Optional<Contact> optContact = contactRepository.findByEmail(orgEditModel.getNewContact().getEmail().toLowerCase());
        	Contact contact = null;
        	if (optContact.isEmpty()) {
        		contact = orgEditModel.getNewContact();
    	        try {
    	        	contactRepository.save(contact);
    	        } catch (DataAccessException dae) {
    	            bindingResult.addError(new ObjectError("Error on save Contact",  dae.getMessage()));
    	        }
        	} else {
        		contact = optContact.get();
        		if (contact.isDeleted()) {
        			contact.setDeleted(false);
        	        try {
        	        	contactRepository.save(contact);
        	        } catch (DataAccessException dae) {
        	            bindingResult.addError(new ObjectError("Error on save Contact",  dae.getMessage()));
        	        }
           		}
        	}

        	if (!bindingResult.hasErrors()) {
	        	Optional<OrgContact> optOrgContact = orgContactRepository.findByOrganisationAndContact(orgEditModel.getOrganisation(), contact);
	        	OrgContact orgContact = null;
	        	if (optOrgContact.isPresent()) {
	        		orgContact = optOrgContact.get();
	        		if (orgContact.isDeleted()) {
	        			orgContact.setDeleted(false);
	        	        try {
	        	        	orgContactRepository.save(orgContact);
	        	        } catch (DataAccessException dae) {
	        	            bindingResult.addError(new ObjectError("Error on save OrgContact",  dae.getMessage()));
	        	        }
	        		}
	        	} else {
	        		orgContact = new OrgContact();
	        		orgContact.setContact(contact);
	        		orgContact.setOrganisation(orgEditModel.getOrganisation());
	    	        try {
	    	        	orgContactRepository.save(orgContact);
	    	        } catch (DataAccessException dae) {
	    	            bindingResult.addError(new ObjectError("Error on save OrgContact",  dae.getMessage()));
	    	        }
	        	}
        	}
        }

		if (bindingResult.hasErrors()) {
			// Add the parentOrgs collection of the BindingResult version of the model, as it's lost it
			OrganisationEditModel myModel = (OrganisationEditModel) bindingResult.getModel().get("organisationEditModel");
			setupOrganisationModelCollections(org, myModel);

			return "admin/organisationEdit";
		}

		return "redirect:/organisationAdmin/" + org.getId();
    }

	@GetMapping("/admin/loggingLevel")
	public String getAdminLoggingLevel(Model model, RedirectAttributes attr, HttpServletRequest request) {
        // Check the user is authorised to do this
		SecurityInfo secInfo = SecurityInfo.getSecurityInfo(request);

        if (!secInfo.isAdministrator()) {
        	String message = "You are not authorised to this page";
    		logger.warn(SecurityInfo.getSecurityInfo(request).loggerSecurityMessage(message));
    		attr.addFlashAttribute("security_message", message);
        	return SecurityInfo.SECURITY_ERROR_REDIRECT;
        }
		return "admin/loggingLevel";
	}

	private Model getOrganisationModel(HttpServletRequest request, long id, Model model) {
		Organisation org = organisationRepository.findById(id).get();
		OrganisationEditModel orgEditModel = new OrganisationEditModel();
		orgEditModel.setOrganisation(org);
		orgEditModel.setNewContact(new Contact());
		orgEditModel.setCanAdminister(securityService.canAdministerOrganisation(request, id));

		setupOrganisationModelCollections(org, orgEditModel);

        model.addAttribute("organisationEditModel", orgEditModel);

        return model;
    }

	private void setupOrganisationModelCollections(Organisation org, OrganisationEditModel orgEditModel) {
        RelationshipType parentRelationshipType = new RelationshipType();

        if (org.getOrgType().getId() == OrgType.PRESCRIBING_PRACTICE) {
        	parentRelationshipType.setId(RelationshipType.CCG_TO_PRACTICE);
        } else if (org.getOrgType().getId() == OrgType.CCG) {
        	parentRelationshipType.setId(RelationshipType.CSU_TO_CCG);
        }

        List<Organisation> potentialParentOrgs = getParentOrgCandidates(org.getOrgType().getId());

        List<OrgContact> orgContacts = orgContactService.getAllByOrganisation(org);

        List<OrgRelationship> parentOrgRelationships = new ArrayList<>();

        if (parentRelationshipType.getId() != 0) {
        	parentOrgRelationships = orgRelationshipService.getAllByChildOrgAndRelationshipType(org, parentRelationshipType);
        }

        for (OrgRelationship orgRelationship : parentOrgRelationships) {
        	potentialParentOrgs.remove(orgRelationship.getParentOrg());
        }

        orgEditModel.setParentOrgRelationships(parentOrgRelationships);
        orgEditModel.setPotentialParentOrgs(potentialParentOrgs);
        orgEditModel.setOrgContacts(orgContacts);
    }

	private List<Organisation> getParentOrgCandidates(long iOrgType) {
		List<Organisation> parentOrgs = new ArrayList<>();
        if (iOrgType == OrgType.PRESCRIBING_PRACTICE) {
            parentOrgs = organisationService.getAllByOrgType(OrgType.CCG);
        } else if (iOrgType == OrgType.CCG) {
        	parentOrgs = organisationService.getAllByOrgType(OrgType.CSU);
        }
		return parentOrgs;
	}

}
