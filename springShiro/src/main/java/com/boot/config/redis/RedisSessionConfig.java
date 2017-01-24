package com.boot.config.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *	
 *创建人：Mr.chang
 *
 *创建时间：2016年12月1日 上午11:28:12 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Configuration  
@EnableRedisHttpSession(redisNamespace="sessionRedisTemplate") 
public class RedisSessionConfig {

}
  