package uk.nhs.gpitf.purchasing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import uk.nhs.gpitf.purchasing.utils.CustomRedisSerializer;

@EnableOAuth2Sso
@SpringBootApplication
public class PurchasingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchasingApplication.class, args);
	}
}
