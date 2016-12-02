package com.boot.config.redis;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月30日 下午4:16:14 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Component
public class RedisCacheManager implements CacheManager{
	
	@Resource
	private RedisTemplate<String,Object> redisTemplate;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new RedisValueCache<>(name, redisTemplate);
	}

}
  