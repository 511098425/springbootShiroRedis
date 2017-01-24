package com.boot.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.boot.config.redis.RedisCacheManager;

/**
 *
 * 项目名称：springShiro
 *
 * 描述：Shiro集成Cas配置
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年11月25日 下午2:06:17
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */
@Configuration
public class ShiroCasConfiguration {
	
	// 日志对象
//	private static Logger log = LoggerFactory.getLogger(MyBatisConfig.class);
	/*
	 * // CasServerUrlPrefix public static final String casServerUrlPrefix =
	 * "https://localhost:8443/cas"; // Cas登录页面地址 public static final String
	 * casLoginUrl = casServerUrlPrefix + "/login"; // Cas登出页面地址 public static
	 * final String casLogoutUrl = casServerUrlPrefix + "/logout"; //
	 * 当前工程对外提供的服务地址 public static final String shiroServerUrlPrefix =
	 * "http://localhost/springShiro"; // casFilter UrlPattern public static
	 * final String casFilterUrlPattern = "/shiro-cas"; // 登录地址 public static
	 * final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix
	 * + casFilterUrlPattern;
	 */
	/**
	 * Ehcache缓存管理
	 * @return
	 */
	@Bean
	public EhCacheManager getEhCacheManager() {
		// Ehcache缓存
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return em;
	}
	
	/**
	 * session共享管理
	 * @param cacheManager
	 * @return
	 */
	@Bean(name="sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager(RedisCacheManager cacheManager) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager);
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setDeleteInvalidSessions(true);
		return sessionManager;
	}

	/**
	 * shiro安全管理器
	 * @param cacheManager
	 * @param customShiroCasRealm
	 * @param sessionManager
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(RedisCacheManager cacheManager,CustomShiroCasRealm customShiroCasRealm,DefaultWebSessionManager sessionManager) {
		DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
		//设置自定义Realm
		dwsm.setRealm(customShiroCasRealm);
		//注入缓存管理器(用户授权/认证信息Cache)
		dwsm.setCacheManager(cacheManager);
		//session共享管理器
//		dwsm.setSessionManager(sessionManager);
		//注入记住我管理器
//		dwsm.setRememberMeManager(rememberMeManager());
		// 指定 SubjectFactory
		dwsm.setSubjectFactory(new CasSubjectFactory());
		return dwsm;
	}
	/**
	 * 自定义shiro Realm
	 * @param cacheManager
	 * @param hashedCredentialsMatcher
	 * @return
	 */
	@Bean(name = "customShiroCasRealm")
	public CustomShiroCasRealm myShiroCasRealm(EhCacheManager cacheMananger,HashedCredentialsMatcher  hashedCredentialsMatcher ) {
		CustomShiroCasRealm realm = new CustomShiroCasRealm();
		realm.setCacheManager(cacheMananger);
		realm.setCredentialsMatcher(hashedCredentialsMatcher);
		realm.setCachingEnabled(true);
		realm.setAuthenticationCachingEnabled(true);
		realm.setAuthenticationCacheName("authenticationCache");
		realm.setAuthorizationCachingEnabled(true);
		realm.setAuthorizationCacheName("authorizationCache");
		return realm;
	}
	
	/**
	 * 凭证匹配器
	 * @param cacheManager
	 * @return
	 */
	@Bean(name="credentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher(EhCacheManager cacheMananger){
		// 密码错误重试次数
		RetryLimitHashedCredentialsMatcher r = new RetryLimitHashedCredentialsMatcher(cacheMananger);
		r.setHashAlgorithmName("md5");//MD5散列算法
		r.setHashIterations(2);//散列次数
		r.setStoredCredentialsHexEncoded(true);
		return r;
	}
	/**
	 * 注册DelegatingFilterProxy（Shiro）
	 * 
	 * @return
	 */
	
	 @Bean
	 public FilterRegistrationBean filterRegistrationBean() {
		 FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		 filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		 // 该值缺省为false,表示生命周期由SpringApplicationContext管理,
		 //设置为true则表示由ServletContainer管理
		 filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		 filterRegistration.setEnabled(true);
		 filterRegistration.addUrlPatterns("/*"); 
		 return filterRegistration; 
	 }

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启shiro动态代理
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}
	
	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * CAS过滤器
	 * 
	 * @return
	 */
	/*
	 * @Bean(name = "casFilter") public CasFilter getCasFilter() { CasFilter
	 * casFilter = new CasFilter(); casFilter.setName("casFilter");
	 * casFilter.setEnabled(true); // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的
	 * doGetAuthenticationInfo 方法向CasServer验证tiket
	 * casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面 return casFilter; }
	 */

	/**
	 * shiro过滤器
	 * @param securityManager
	 * @param casFilter
	 * @param stuService
	 * @param scoreDao
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 默认跳转的登录页,如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的连接
		shiroFilterFactoryBean.setSuccessUrl("/user");
		//未授权跳转的页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		
		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
       	//配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
       	filterChainDefinitionMap.put("/logout", "logout");
       	//配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/user", "user");
        filterChainDefinitionMap.put("/", "user");
        
    	filterChainDefinitionMap.put("/user", "authc");// 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
		// anon：它对应的过滤器里面是空的,什么都没做
		filterChainDefinitionMap.put("/user/**", "authc");// 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取

		filterChainDefinitionMap.put("/login", "anon");
//		filterChainDefinitionMap.put("/**", "anon");// anon 可以理解为不拦截
       	
		// 添加casFilter到shiroFilter中
		/*
		 * Map<String, Filter> filters = new HashMap<>();
		 * filters.put("casFilter", casFilter);
		 * shiroFilterFactoryBean.setFilters(filters);
		 */
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}
	
	/**
	 * cookie对象
	 * @return
	 */
/*	@Bean
    public SimpleCookie rememberMeCookie(){
       System.out.println("ShiroConfiguration.rememberMeCookie()");
       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
       //<!-- 记住我cookie生效时间30天 ,单位秒;-->
       simpleCookie.setMaxAge(259200);
       return simpleCookie;
    }
	
	 *//**
     * cookie管理对象;
     * @return
     *//*
    @Bean
    public CookieRememberMeManager rememberMeManager(){
       System.out.println("ShiroConfiguration.rememberMeManager()");
       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
       cookieRememberMeManager.setCookie(rememberMeCookie());
       return cookieRememberMeManager;
    }*/

}
