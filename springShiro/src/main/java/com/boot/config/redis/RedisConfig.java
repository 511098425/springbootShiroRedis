package com.boot.config.redis;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**  
 *
 *项目名称：WeddingBOS
 *
 *描述：redis缓存配置
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年8月19日 上午10:26:12 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	
	//日志对象
	private static Logger log = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<Object, Object> template) {
        RedisCacheManager cacheManager = new RedisCacheManager();
        return cacheManager;
    }
	
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();  
                sb.append(target.getClass().getName());  
                sb.append(method.getName());  
                for (Object obj : params) {  
                    sb.append(obj.toString());  
                }  
                return sb.toString();
			}
		};
	}

	@Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory factory) {
		log.debug("=================redisTemplate实例化======================");
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();  
        template.setConnectionFactory(factory);  
        setMySerializer(template);  
        template.afterPropertiesSet();  
        log.info("template{}"+template.toString());  
        return template;  
    }
	
	 /** 
     * 设置序列化方法 
     */  
    private void setMySerializer(RedisTemplate<Object, Object> template) {  
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);  
        ObjectMapper om = new ObjectMapper();  
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);  
        jackson2JsonRedisSerializer.setObjectMapper(om);  
        template.setKeySerializer(template.getStringSerializer());  
        template.setValueSerializer(jackson2JsonRedisSerializer);  
    }
	
}
  