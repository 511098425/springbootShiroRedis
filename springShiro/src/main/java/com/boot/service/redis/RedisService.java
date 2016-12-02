package com.boot.service.redis;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 *
 * 项目名称：springShiro
 *
 * 描述：redis操作service
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年11月29日 上午11:25:58
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */
@Service
public class RedisService {
/*
	@Autowired
	RedisTemplate<Serializable,Object> redisTemplate;

	*//**
	 * @Description : 根据key获取缓存对象
	 * @return : Object
	 * @Author : Mr.Chang
	 *//*
	public Object get(final String key) {
		ValueOperations<Serializable, Object> opt = redisTemplate.opsForValue();
		return opt.get(key);
	}

	*//**
	 * @Description : 写入缓存
	 * @return : boolean
	 * @Author : Mr.Chang
	 *//*
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> opt = redisTemplate.opsForValue();
			opt.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	*//**
	 * @Description : Mr.Chang 写入缓存,可设置过期时间(单位：秒)
	 * @return : boolean
	 * @Author : Mr.Chang
	 *//*
	public boolean setex(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> opt = redisTemplate.opsForValue();
			opt.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	*//**
	 * @Description :判断缓存中是否存在指定key
	 * @return : boolean
	 * @Author : Mr.Chang
	 *//*
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	*//**
	 * @Description : 删除缓存
	 * @return : void
	 * @Author : Mr.Chang
	 *//*
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	*//**
	 * @Description : 批量删除缓存
	 * @return : void
	 * @Author : Mr.Chang
	 *//*
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}

	*//**
	 * @Description : 批量删除缓存
	 * @return : void
	 * @Author : Mr.Chang
	 *//*
	public void remove(final String... keys) {
		for (int i = 0; i < keys.length; i++) {
			remove(keys[i]);
		}
	}*/
}
