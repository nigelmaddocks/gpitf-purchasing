package uk.nhs.gpitf.purchasing;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import uk.nhs.gpitf.purchasing.entities.Contact;
import uk.nhs.gpitf.purchasing.repositories.ContactRepository;
import uk.nhs.gpitf.purchasing.utils.SecurityInfo;

@Configuration
public class ControllerConfiguration implements WebMvcConfigurer {
	
	@Autowired
	ContactRepository contactRepository;
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ControllerInterceptor()).addPathPatterns("/**/*")
        .excludePathPatterns("/scss/**/*")
        .excludePathPatterns("/css/**/*")
        .excludePathPatterns("/js/**/*")
        .excludePathPatterns("/img/**/*")
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
	            	Optional<Contact> optContact = contactRepository.findByEmail(sUserEmail);
	            	if (optContact.isPresent()) {
	            		Contact contact = optContact.get();
	            		secinfo.setForename(contact.getForename());
	            		secinfo.setSurname(contact.getSurname());
	            		secinfo.setKnown(true);
	            	} else {
	            	
//	            	boolean bFoundContact = false;
//	            	if (!bFoundContact) {
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
            }
		
    		//DefaultOAuth2ClientContext oAuth2CliCtx = (DefaultOAuth2ClientContext) request.getSession().getAttribute("scopedTarget.oauth2ClientContext");
    		
    		request.getSession().setAttribute("SecurityInfo", secinfo);
            
            return true;
        }
    }    
}
