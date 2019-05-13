package uk.nhs.gpitf.purchasing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cache.annotation.EnableCaching;

@EnableOAuth2Sso
@SpringBootApplication
@EnableCaching
public class PurchasingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchasingApplication.class, args);
	}

}
