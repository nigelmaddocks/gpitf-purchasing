package uk.nhs.gpitf.purchasing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.swagger.client.ApiClient;
import io.swagger.client.api.CapabilitiesApi;
import io.swagger.client.api.CapabilitiesImplementedApi;
import io.swagger.client.api.SearchApi;
import io.swagger.client.api.SolutionsApi;
import io.swagger.client.api.StandardsApi;
import io.swagger.client.auth.OAuth;

@Configuration
public class OnboardingIntegrationConfig {
	@Value("${onboarding.api.url}")
    private String onboarding_api_url;
	
    @Bean
    public ApiClient apiClient() {
    	ApiClient apiClient = new ApiClient();
    	apiClient.setBasePath(onboarding_api_url);
    	apiClient.setUsername("Admin");
    	apiClient.setPassword("Admin");
        //OAuth onboardingAuth = (OAuth) apiClient.getAuthentication("petstore_auth");
        //onboardingAuth.setAccessToken("special-key");
        return apiClient;
    }

//    @Autowired
//    public void configureObjectMapper(ObjectMapper objectMapper) {
//        objectMapper.findAndRegisterModules();
//        objectMapper.registerModule(new JavaTimeModule());
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
//        objectMapper.setDateFormat(dateFormat);
//
//    }    
    
    @Bean
    public CapabilitiesApi capabilitiesApi() {    	 
        return new CapabilitiesApi(apiClient());
    }
	 
    @Bean
    public SolutionsApi solutionsApi() {    	 
        return new SolutionsApi(apiClient());
    }
    
    @Bean
    public CapabilitiesImplementedApi capabilitiesImplementedApi() {    	 
        return new CapabilitiesImplementedApi(apiClient());
    }
	 
    @Bean
    public StandardsApi standardsApi() {    	 
        return new StandardsApi(apiClient());
    }

    @Bean
    public SearchApi searchApi() {
    	return new SearchApi(apiClient());
    }
    
}
