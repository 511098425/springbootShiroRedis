package com.boot.config.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * 项目名称：springShiro
 *
 * 描述：
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年11月30日 下午4:17:55
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */
public class RedisValueCache<K, V> implements Cache<K, V>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -327493024746553059L;
	
	@Resource
	private RedisTemplate<K, V> redisTemplate;

	private final String REDIS_SHIRO_CACHE = "shiro-cache:";
	private String cacheKey;
	private long globExpire = 1 * 60 * 60;// *60*60;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedisValueCache(String name, RedisTemplate redisTemplate) {
		this.cacheKey = this.REDIS_SHIRO_CACHE + name + ":";
		this.redisTemplate = redisTemplate;
	}

	@Override
	public V get(K key) throws CacheException {
		redisTemplate.boundValueOps(getCacheKey(key)).expire(globExpire, TimeUnit.MINUTES);
		return redisTemplate.boundValueOps(getCacheKey(key)).get();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		V old = get(key);
		redisTemplate.boundValueOps(getCacheKey(key)).set(value);
		return old;
	}

	@Override
	public V remove(K key) throws CacheException {
		V old = get(key);
		redisTemplate.delete(getCacheKey(key));
		return old;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.delete(keys());
	}

	@Override
	public int size() {
		return keys().size();
	}

	@SuppressWarnings("unchecked")
	private K getCacheKey(Object k) {
		return (K) (this.cacheKey + k);
	}

	@Override
	public Set<K> keys() {
		return redisTemplate.keys(getCacheKey("*"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Collection<V> values() {
		Set<K> set = keys();
		List list = new LinkedList();
		for (K s : set) {
			list.add(get(s));
		}
		return list;
	}

}
