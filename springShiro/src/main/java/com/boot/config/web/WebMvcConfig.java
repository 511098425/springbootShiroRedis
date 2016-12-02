package com.boot.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * 项目名称：WeddingBOS
 *
 * 描述：mvc配置类
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年8月5日 上午11:31:22
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * 静态资源拦截器
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	/**
	 * 自定义拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new
		// AuthInterceptor()).addPathPatterns("/**");
	}

}
