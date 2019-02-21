package uk.nhs.gpitf.purchasing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Profile({"auth0", "docker"})
@EnableRedisHttpSession
@Configuration
public class RedisConfiguration {
/*
	   @Bean(name = {"defaultRedisSerializer","springSessionDefaultRedisSerializer"})
	    RedisSerializer<Object> defaultRedisSerializer() {
		   return new CustomRedisSerializer();
	    }
*/	    	  
}