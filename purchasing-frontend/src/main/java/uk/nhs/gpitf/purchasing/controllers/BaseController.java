package uk.nhs.gpitf.purchasing.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.entities.Organisation;
import uk.nhs.gpitf.purchasing.models.LoginOrgSelectionModel;
import uk.nhs.gpitf.purchasing.models.OrgContactModel;
import uk.nhs.gpitf.purchasing.repositories.ContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.services.SecurityService;
import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller
public class BaseController {

	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	OrgContactRepository orgContactRepository;
	
	@Autowired
	SecurityService securityService;
	
	@GetMapping("/purchasingLogin")
	public String purchasingLogin(Model model, HttpServletRequest request, Principal principal) {
		Breadcrumbs.reset(request);
        //return "mainMenu";
        return "redirect:/mainMenu";
    }	
	
	@GetMapping("/purchasingLogout")
	public String purchasingLogout(Model model, HttpServletRequest request, Principal principal) {
		//request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
		//new SecurityContextLogoutHandler().logout(request, null, null);
		
		HttpSession session= request.getSession(false);
	    SecurityContextHolder.clearContext();
	    session= request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        for(Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }

		
		Breadcrumbs.reset(request);
        return "redirect:/mainMenu";
    }	
	
	@GetMapping("/")
	public String root(Model model, HttpServletRequest request, Principal principal) {
		Breadcrumbs.reset(request);
        return "mainMenu";
    }	
	@GetMapping("/mainMenu")
	public String mainMenu(Model model, HttpServletRequest request, Principal principal) {
		Breadcrumbs.reset(request);
        return "mainMenu";
    }	
	
	//@GetMapping("/error")
	public String error(Model model, HttpServletRequest request, Principal principal) {
		Breadcrumbs.reset(request);
        return "error";
    }	
	
	@GetMapping("/loginOrgSelection")
	public String loginOrgSelection(Model model, HttpServletRequest request, Principal principal) {
		SecurityContext secCtx =  (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		
		if (secCtx != null) {
    		Authentication auth = secCtx.getAuthentication();
 
    		String sUserEmail = "";
            if (auth instanceof OAuth2Authentication) {
            	sUserEmail = (String) ((LinkedHashMap) ((OAuth2Authentication) auth).getUserAuthentication().getDetails()).get("email");
            }
        	Optional<Contact> optContact = contactRepository.findByEmail(sUserEmail);
        	if (optContact.isPresent()) {
        		Contact contact = optContact.get();
        		Iterable<OrgContact> itbOrgContact = orgContactRepository.findAllByContactAndDeleted(contact, false);
        		ArrayList<Organisation> arlOrganisation = new ArrayList<>();
        		for (var oc : itbOrgContact) {
        			arlOrganisation.add(oc.getOrganisation());
        		}
        		
        		LoginOrgSelectionModel myModel = new LoginOrgSelectionModel();
        		myModel.setOrganisations(arlOrganisation.toArray(new Organisation[] {}));
        		model.addAttribute("LoginOrgSelectionModel", myModel);
        		
        		return "loginOrgSelection";
        	}
		}
		return "";
    }	
	
	@PostMapping("/loginOrgSelection")
	public String loginOrgSelectionPost(@Valid
			LoginOrgSelectionModel loginOrgSelectionModel, BindingResult bindingResult, RedirectAttributes attr,
			HttpServletRequest request) {
		
		request.getSession().setAttribute("tmpSelectedOrganisation", Long.valueOf(loginOrgSelectionModel.getSelectedOrganisation()));
		
		return "redirect:/mainMenu";
    }	

}
