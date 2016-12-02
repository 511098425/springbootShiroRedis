package com.boot.config.shiro;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月28日 下午4:03:33 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@ControllerAdvice
public class DefaultExceptionHandler {

	 /**
     * 没有权限 异常
     * <p/>
     * 后续根据不同的需求定制即可
     */
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", e);
        mv.setViewName("403");
        return mv;
    }
}
  