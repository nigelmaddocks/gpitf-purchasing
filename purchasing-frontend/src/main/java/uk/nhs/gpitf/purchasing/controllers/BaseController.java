package uk.nhs.gpitf.purchasing.controllers;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.nhs.gpitf.purchasing.utils.Breadcrumbs;

@Controller
public class BaseController {
	
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

}
