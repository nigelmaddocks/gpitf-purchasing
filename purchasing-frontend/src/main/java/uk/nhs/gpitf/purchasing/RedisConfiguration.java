package uk.nhs.gpitf.purchasing;

import java.time.Duration;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.nhs.gpitf.purchasing.utils.CustomRedisSerializer;
import uk.nhs.gpitf.purchasing.utils.JsonRedisSerializer;

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