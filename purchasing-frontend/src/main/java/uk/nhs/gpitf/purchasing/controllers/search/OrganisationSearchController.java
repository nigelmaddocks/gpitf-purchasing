package uk.nhs.gpitf.purchasing.controllers.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.OrgRelationship;
import uk.nhs.gpitf.purchasing.entities.RelationshipType;
import uk.nhs.gpitf.purchasing.models.OrganisationSearchModel;
import uk.nhs.gpitf.purchasing.services.OrganisationService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller 
public class OrganisationSearchController {	

    @Autowired
    private OrganisationService organisationService;
	
	@GetMapping("/organisationSearch")
	public String organisationSearch(Model model, HttpServletRequest request) {
		Breadcrumbs.register("Search Orgs", request);
		OrganisationSearchModel.Criteria criteria = (OrganisationSearchModel.Criteria) request.getSession().getAttribute("OrganisationSearchModel.Criteria");
		
		OrganisationSearchModel orgSearchModel = new OrganisationSearchModel();
		
		if (criteria != null) {			
			orgSearchModel.setCriteria(criteria);
			setupOrgSearchModel(null, orgSearchModel);
		}
		//request.getSession().removeAttribute("OrganisationSearchModel.Criteria");
		
		model.addAttribute("organisationSearchModel", orgSearchModel);
        return "admin/organisationSearch";
    }	
	
	@PostMapping("/organisationSearch")
	public String organisationSearchPost(
			@Valid OrganisationSearchModel orgSearchModel, BindingResult bindingResult, RedirectAttributes attr, HttpServletRequest request) {

		setupOrgSearchModel(bindingResult, orgSearchModel);
		
    	request.getSession().setAttribute("OrganisationSearchModel.Criteria", orgSearchModel.getCriteria());

    	return "admin/organisationSearch";
    }	
	
	private void setupOrgSearchModel(BindingResult bindingResult, OrganisationSearchModel orgSearchModel) {

        if (bindingResult != null && !bindingResult.hasErrors()) { 	
        	String sGPName = orgSearchModel.getCriteria().getGpName().trim().toUpperCase();
        	if (sGPName.equals("SURGERY")
        	 || sGPName.equals("PRACTICE")
        	 || sGPName.equals("CENTRE")
        	 || sGPName.equals("CLINIC")
        	 || sGPName.equals("MEDICAL")) {
        		bindingResult.addError(new FieldError("organisationSearchModel", "gpName", sGPName, true, null, null, "Choose a more targeted term"));
        		//bindingResult.addError(new ObjectError("GP Name", "Choose a more targeted term"));
        	}
        }		

        if (bindingResult != null && !bindingResult.hasErrors()) { 	
        	
        	int iCriteria = 0;
        	if (orgSearchModel.getCriteria().getGpCode().trim().length() > 0) iCriteria++;
        	if (orgSearchModel.getCriteria().getGpName().trim().length() > 0) iCriteria++;
        	if (orgSearchModel.getCriteria().getCcgCode().trim().length() > 0) iCriteria++;
        	if (orgSearchModel.getCriteria().getCcgName().trim().length() > 0) iCriteria++;
        	if (iCriteria == 0 || iCriteria > 1) {
        		bindingResult.addError(new ObjectError("Search Criteria", "Please supply one search term"));
        	}
        }		

        if (bindingResult != null && !bindingResult.hasErrors() || bindingResult == null) {
        	if (orgSearchModel.getCriteria().getGpName().trim().length() > 0) {
        		List<OrgRelationship> orgRels = organisationService.getOrgsAndParentsByRelTypeAndChildNameOrderByChildName(RelationshipType.CCG_TO_PRACTICE, orgSearchModel.getCriteria().getGpName().trim().toUpperCase());
        		orgSearchModel.setOrgRels(orgRels);
        	}
        	
        	if (orgSearchModel.getCriteria().getGpCode().trim().length() > 0) {
        		List<OrgRelationship> orgRels = organisationService.getOrgsAndParentsByRelTypeAndChildOrgTypeOrderByChildName(RelationshipType.CCG_TO_PRACTICE, orgSearchModel.getCriteria().getGpCode().trim().toUpperCase());
        		orgSearchModel.setOrgRels(orgRels);
        	}
        	
        	if (orgSearchModel.getCriteria().getCcgName().trim().length() > 0) {
        		List<OrgRelationship> orgRels = organisationService.getOrgsAndParentsByRelTypeAndParentNameOrderByChildName(RelationshipType.CCG_TO_PRACTICE, orgSearchModel.getCriteria().getCcgName().trim().toUpperCase());
        		orgSearchModel.setOrgRels(orgRels);
        	}
        	
        	if (orgSearchModel.getCriteria().getCcgCode().trim().length() > 0) {
        		List<OrgRelationship> orgRels = organisationService.getOrgsAndParentsByRelTypeAndParentOrgTypeOrderByChildName(RelationshipType.CCG_TO_PRACTICE, orgSearchModel.getCriteria().getCcgCode().trim().toUpperCase());
        		orgSearchModel.setOrgRels(orgRels);
        	}
        }
		
	}
	
}
