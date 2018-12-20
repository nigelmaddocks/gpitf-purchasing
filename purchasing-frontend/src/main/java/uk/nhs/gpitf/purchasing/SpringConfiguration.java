package uk.nhs.gpitf.purchasing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class SpringConfiguration {

    @Bean
    public static ApplicationContextProvider contextProvider() {
        return new ApplicationContextProvider();
    }

    /** This allows for local programmatic Spring validation via use of
     *  @Autowired
  	 *  private Validator validator;
     */
    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
      return new LocalValidatorFactoryBean();
    }

}