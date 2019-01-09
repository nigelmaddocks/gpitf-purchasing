package uk.nhs.gpitf.purchasing;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.boot.autoconfigure.security.oauth2.client.SsoSecurityConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity

@Configuration
@EnableOAuth2Sso
//@Order(101)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final ApplicationContext applicationContext;

	public WebSecurityConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

//	public static final String ADMIN = "admin";
//	public static final String GUEST = "guest";
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
        //.csrf().disable()   // With it disabled, runs better in Chrome DevTools - better enabled though
        .authorizeRequests()

        .antMatchers("/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.woff2",
                "/**/*.js").permitAll()                   
        .antMatchers("/organisationData/**").permitAll()
        .antMatchers("/", "/mainMenu", "/purchasingLogout", "/loginOrgSelection", "/error").permitAll()
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/login").permitAll()
        .and()
        .logout().logoutUrl("/purchasingLogout")
        .deleteCookies("_state.catalogue-private-beta-ci")
        .logoutSuccessUrl("/mainMenu").permitAll()
        ;
        
        /* Without the following line, the security settings from application.yml are not followed.
         * The OAuth2SsoDefaultConfiguration, which we are replacing (for configuration of endpoint security), 
         * uses the following line except
         * that it creates a new SsoSecurityConfigurer. However we cannot use that class as it is 
         * not visible. Hence I have needed to copy it into MySsoSecurityConfigurer.
         */
		new MySsoSecurityConfigurer(this.applicationContext).configure(http);

    }
    
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles(ADMIN.toString())
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("guest")).roles(GUEST.toString());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/    
    
    
}