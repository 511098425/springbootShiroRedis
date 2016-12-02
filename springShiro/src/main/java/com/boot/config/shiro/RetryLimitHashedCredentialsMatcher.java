package com.boot.config.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;

/**  
 *
 *项目名称：springShiro
 *
 *描述：认证错误重试次数
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月28日 上午11:10:08 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{
	
	private Cache<String, AtomicInteger> passwordRetryCache;
	
	public RetryLimitHashedCredentialsMatcher(EhCacheManager cacheManager) {
	        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	 }
	 
	 @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
  