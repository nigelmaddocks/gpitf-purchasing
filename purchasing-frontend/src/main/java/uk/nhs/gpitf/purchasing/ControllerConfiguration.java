package uk.nhs.gpitf.purchasing;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.entities.OrgContact;
import uk.nhs.gpitf.purchasing.repositories.ContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRepository;
import uk.nhs.gpitf.purchasing.repositories.OrgContactRoleRepository;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Configuration
public class ControllerConfiguration implements WebMvcConfigurer {
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	OrgContactRepository orgContactRepository;
	
	@Autowired
	OrgContactRoleRepository orgContactRoleRepository;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ControllerInterceptor()).addPathPatterns("/**/*")
        .excludePathPatterns("/scss/**/*")
        .excludePathPatterns("/css/**/*")
        .excludePathPatterns("/js/**/*")
        .excludePathPatterns("/img/**/*")
        .excludePathPatterns("/loginOrgSelection")
        .excludePathPatterns("/purchasingLogout")
        ;
    }
    
    public class ControllerInterceptor implements HandlerInterceptor {
/*    	
        @Override
        public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
            System.out.println("afterCompletion");
        }
        @Override
        public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
            System.out.println("postHandle");
        }
*/        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
            //System.out.println("preHandle");
        	System.out.println("ControllerConfiguration.preHandle: " + request.getRequestURI());
    		SecurityInfo secinfo = (SecurityInfo) request.getSession().getAttribute("SecurityInfo");
        	if (secinfo == null) {
        		secinfo = new SecurityInfo();
        	}
    		
    		SecurityContext secCtx =  (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
    		
    		if (secCtx != null) {
	    		Authentication auth = secCtx.getAuthentication();
	    		secinfo.setAuthenticated(auth.isAuthenticated());
	    		Collection<SimpleGrantedAuthority> collAuthorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
	
	    		String sUserEmail = "";
	            if (auth instanceof OAuth2Authentication) {
	            	sUserEmail = (String) ((LinkedHashMap) ((OAuth2Authentication) auth).getUserAuthentication().getDetails()).get("email");
	            }
	            
	            // If the email address has changed, then lookup the name from Contacts table
	            if (!sUserEmail.equals(secinfo.getEmail())) {
                	secinfo.setOrganisationId(0);
                	secinfo.setOrganisationName("");
                	secinfo.setRoles(new long[] {});
                	secinfo.setRolesDescription("");
                	
	            	Optional<Contact> optContact = contactRepository.findByEmail(sUserEmail);
	            	if (optContact.isPresent()) {
	            		Contact contact = optContact.get();
	            		secinfo.setForename(contact.getForename());
	            		secinfo.setSurname(contact.getSurname());
	            		secinfo.setKnown(true);
	            		
	            		Iterable<OrgContact> itbOrgContact = orgContactRepository.findAllByContactAndDeleted(contact, false);
	            		ArrayList<OrgContact> arlOrgContact = new ArrayList<>();
	            		for (OrgContact orgContact : itbOrgContact) {
	            			arlOrgContact.add(orgContact);
	            		}
	            		if (arlOrgContact.size() == 1) {
	            			setupSecinfoFromOrgContact(secinfo, arlOrgContact.get(0));
	            		}
	            		if (arlOrgContact.size() > 1) {
	            			Long lngSelectionOrganisation = (Long)request.getSession().getAttribute("tmpSelectedOrganisation");
	            			if (lngSelectionOrganisation != null && lngSelectionOrganisation.longValue() != 0) {
	            				for (var oc : arlOrgContact) {
	            					if (oc.getOrganisation().getId() == lngSelectionOrganisation.longValue()) {
	            						setupSecinfoFromOrgContact(secinfo, oc);
	            						request.getSession().removeAttribute("tmpSelectedOrganisation");
	            					}
	            				}
	            			}
	            			
	            			if (secinfo.getOrganisationId() == 0) {
		            			request.getSession().setAttribute("SecurityInfo", secinfo);
		            			response.sendRedirect("/loginOrgSelection");
		            			return false;
	            			}
	            		}
	            	} else {
	            		secinfo.setForename(sUserEmail);
	            		secinfo.setSurname("unknown");
	            		secinfo.setKnown(false);
	            	}            	
	            }
	            secinfo.setEmail(sUserEmail);
    		}
    		
            if (!secinfo.isAuthenticated()) {
            	secinfo.setKnown(false);
            	secinfo.setEmail("");
            	secinfo.setForename("");
            	secinfo.setSurname("");
            	secinfo.setOrganisationId(0);
            	secinfo.setOrganisationName("");
            	secinfo.setOrganisationTypeId(0);
            	secinfo.setRoles(new long[] {});
            	secinfo.setRolesDescription("");
            }
		
    		//DefaultOAuth2ClientContext oAuth2CliCtx = (DefaultOAuth2ClientContext) request.getSession().getAttribute("scopedTarget.oauth2ClientContext");
    		
    		request.getSession().setAttribute("SecurityInfo", secinfo);
            
    		// General page authorisation
    		boolean authorised = isPageAuthorised(request, secinfo);
    		if (!authorised) {
    			response.sendRedirect(SecurityInfo.SECURITY_ERROR_TARGET);
    			return false;    			
    		}
    		
            return true;
        }
    }    
    
    private void setupSecinfoFromOrgContact(SecurityInfo secinfo, OrgContact orgContact) {
    	secinfo.setOrganisationId(orgContact.getOrganisation().getId());
    	secinfo.setOrganisationName(orgContact.getOrganisation().getNameProperCase());
    	secinfo.setOrganisationTypeId(orgContact.getOrganisation().getOrgType().getId());
    	secinfo.setRolesDescription(orgContact.getRolesAsString());
    	ArrayList<Long> arlRoleIds = new ArrayList<>();
    	for (var ocr : orgContact.getOrgContactRoles()) {
    		arlRoleIds.add(ocr.getRole().getId());
    	}
    	long[] arrRoleIds = new long[arlRoleIds.size()];
    	for (int idx=0; idx<arlRoleIds.size(); idx++) {
    		arrRoleIds[idx] = arlRoleIds.get(idx).intValue();
    	}
    	secinfo.setRoles(arrRoleIds);
    }
/*    
    private  String getClientId(HttpServletRequest request) {
        //final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        final String authorizationHeaderValue = request.getHeader("Authorization");
        final String base64AuthorizationHeader = Optional.ofNullable(authorizationHeaderValue)
                .map(headerValue->headerValue.substring("Basic ".length())).orElse("");

        if(StringUtils.isNotEmpty(base64AuthorizationHeader)){
            String decodedAuthorizationHeader = new String(Base64.getDecoder().decode(base64AuthorizationHeader), Charset.forName("UTF-8"));
            return decodedAuthorizationHeader.split(":")[0];
        }

        return "";
    }    
*/    
    /**
     * This is intended for simple authorisation of webpages, or a suite of webpages based on the url and the user's role[s]
     * @param secinfo
     * @return
     */
    private boolean isPageAuthorised(HttpServletRequest request, SecurityInfo secinfo) {
    	String uri = request.getRequestURI();
    	
    	// dataload must be administrator
    	if (uri.matches(".*\\/dataload\\/.*") && !secinfo.isAdministrator()) {
    		return false;
    	}
    	
    	return true;
    }
    
}
