package com.boot.aop.aspect;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.boot.controller.LoginController;


/**  
 *
 *项目名称：springShiro
 *
 *描述：切点
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年12月5日 上午11:25:39 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Aspect
@Component
public class SystemLogAcpect {
	
	//注入Service用于把日志保存数据库    
   /* @Resource    
    private LogService logService;    */
    //本地异常日志记录对象    
    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    //Service层切点    
    @Pointcut("@annotation(com.boot.aop.annotation.SystemServiceLog)")    
     public  void serviceAspect() {    
    }    
    
    //Controller层切点    
    @Pointcut("@annotation(com.boot.aop.annotation.SystemControllerLog)")    
     public  void controllerAspect() {    
    }  
    
    /**
     * controller前置通知
     * @param joinPoint
     */
    @Before("controllerAspect()")    
    public  void doBefore(JoinPoint joinPoint) { 
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
    	  
        // 记录下请求内容  
        log.info("URL : " + request.getRequestURL().toString());  
        log.info("METHOD : " + request.getMethod());  
        log.info("IP : " + getIpAddr(request));  
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());  
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));    
    }
    
    /**
     * service异常记录日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut="serviceAspect()",throwing="e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e){
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
    	 // 记录下请求内容  
        log.info("URL : " + request.getRequestURL().toString());  
        log.info("METHOD : " + request.getMethod());  
        log.info("IP : " + getIpAddr(request)); 
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));    
        log.info("Exception:"+e.getMessage());
    }
    
    
 /*   @AfterReturning(returning = "ret", pointcut = "controllerAspect()")  
    public void doAfterReturning(Object ret) throws Throwable {  
        // 处理完请求，返回内容  
    	log.info("RESPONSE : " + ret);  
    	log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));  
    } */
    
    //获取客户端IP
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
  