package uk.nhs.gpitf.purchasing.controllers.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
//import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.OrgContactRole;
import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.OrgType;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.entities.Role;
import uk.nhs.gpitf.purchasing.models.OrgContactModel;
import uk.nhs.gpitf.purchasing.models.OrganisationEditModel;
import uk.nhs.gpitf.purchasing.repositories.ContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRoleRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgRelationshipRepository;
import uk.nhs.gpitf.purchasing.repositories.OrganisationRepository;
import uk.nhs.gpitf.purchasing.repositories.RoleRepository;
import uk.nhs.gpitf.purchasing.services.OrgContactService;
import uk.nhs.gpitf.purchasing.services.OrgRelationshipService;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.services.RoleService;
import uk.nhs.gpitf.purchasing.services.SecurityService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller
public class ContactAdminController {
	
	@Autowired
	OrgContactRepository orgContactRepository;
	
	@Autowired
	OrgContactRoleRepository orgContactRoleRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	SecurityService securityService;
	
	@GetMapping("/orgContactAdmin/{id}")
	public String getOrgContactById(@PathVariable Long id, Model model, HttpServletRequest request) {
		model = getOrgContactModel(request, id, model);	
		Breadcrumbs.register("Contact", request);
        return "admin/orgContactView";
    }	
	
	@GetMapping("/orgContactAdmin/edit/{id}")
	public String getOrgContactEditById(@PathVariable Long id, Model model, HttpServletRequest request) {
		model = getOrgContactModel(request, id, model);	
        return "admin/orgContactEdit";
    }	

	
	@PostMapping("/orgContactAdmin")
	public String updateOrganisation(@Valid
			OrgContactModel orgContactModel, BindingResult bindingResult, RedirectAttributes attr) {
 
		// Save contact attributes
		if (!bindingResult.hasErrors()) {
			try {
				contactRepository.save(orgContactModel.getContact());
	        } catch (DataAccessException dae) {
	            bindingResult.addError(new ObjectError("Error on update of Contact",  dae.getMessage()));
			}
		}
		
		// Remove tagged Roles
		if (!bindingResult.hasErrors()) {
			if (orgContactModel.getDeleteOrgContactRoles() != null) {
		        for (String deleteFlag : orgContactModel.getDeleteOrgContactRoles()) {
		        	long iDeleteOrgContactRole = Long.parseLong(deleteFlag);
		        	
		        	Optional<OrgContactRole> optOrgContactRole = orgContactRoleRepository.findById(iDeleteOrgContactRole);
		        	if (optOrgContactRole.isEmpty()) {
		                bindingResult.addError(new ObjectError("OrgContact Role " + iDeleteOrgContactRole,  " was not found"));
		        	} else {
		        		OrgContactRole orgContactRole = optOrgContactRole.get();
	        			try {
	        				orgContactRoleRepository.delete(orgContactRole);
	        	        } catch (DataAccessException dae) {
	    		            bindingResult.addError(new ObjectError("Error on delete of OrgContact Role",  dae.getMessage()));
	        			}
		        	}
		        }
			}
		}
		
		// Add new Role
		if (!bindingResult.hasErrors() && orgContactModel.getNewRoleId() != 0) {
			OrgContact orgContact = null;
			Role role = null;
			try {
				 orgContact = orgContactRepository.findById(orgContactModel.getId()).get();
	        } catch (Exception e) {
	            bindingResult.addError(new ObjectError("Error on getting of OrgContact",  e.getMessage()));
			}
			if (!bindingResult.hasErrors()) {
				try {
					role = roleRepository.findById(orgContactModel.getNewRoleId()).get();
		        } catch (Exception e) {
		            bindingResult.addError(new ObjectError("Error on getting of new Role",  e.getMessage()));
				}
			}
		
			OrgContactRole newOrgContactRole = new OrgContactRole();
			newOrgContactRole.setOrgContact(orgContact);
			newOrgContactRole.setRole(role);
			
			try {				
				orgContactRoleRepository.save(newOrgContactRole);
	        } catch (DataAccessException dae) {
	            bindingResult.addError(new ObjectError("Error on update of OrgContact Role",  dae.getMessage()));
			}
		}		
		
		
		if (bindingResult.hasErrors()) {
			// Add the parentOrgs collection of the BindingResult version of the model, as it's lost it			
			OrgContactModel myModel = (OrgContactModel) bindingResult.getModel().get("orgContactModel");
			setupObjects(myModel);
			
			return "/admin/orgContactEdit";
		}
		
		return "redirect:/orgContactAdmin/" + orgContactModel.getId();
	}	
	
	private void setupCollections(OrgContactModel orgContactModel, OrgContact orgContact) {
		orgContactModel.setOrganisation(orgContact.getOrganisation());
		orgContactModel.setOrgContactRoles(orgContact.getOrgContactRoles());
		
		// Setup potential roles as those not already held
		orgContactModel.setPotentialRoles(roleService.getAll());
		for (var ocr : orgContact.getOrgContactRoles()) {
			orgContactModel.getPotentialRoles().remove(ocr.getRole());
		}				
	}
	
	private void setupObjects(OrgContactModel orgContactModel) {
		OrgContact orgContact = orgContactRepository.findById(orgContactModel.getId()).get();
		setupCollections(orgContactModel, orgContact);
	}
	
	private Model getOrgContactModel(HttpServletRequest request, long id, Model model) {
		OrgContact orgContact = orgContactRepository.findById(id).get();
		OrgContactModel orgContactModel = new OrgContactModel();
		orgContactModel.setId(id);
		orgContactModel.setContact(orgContact.getContact());
		orgContactModel.setCanAdminister(securityService.canAdministerOrganisation(request, orgContact.getOrganisation().getId()));

		
		setupCollections(orgContactModel, orgContact);
		
        model.addAttribute("orgContactModel", orgContactModel);
        
        return model;
    }	
	
}
