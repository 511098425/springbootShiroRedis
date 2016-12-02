package com.boot.config;

import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**  
 *
 *项目名称：WeddingBOS
 *
 *描述：数据库连接类(阿里druid数据源)
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年8月4日 下午2:01:27 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Configuration
public class DatabaseConfiguration implements EnvironmentAware{
	//日志对象
	private static Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
	private RelaxedPropertyResolver prop; 
	private Environment environment;
	  
    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
        this.prop = new RelaxedPropertyResolver(environment,"spring.datasource.");
	}
	
	/**
	 * @配置数据库连接池配置
	 * @return
	 */
	@Bean(initMethod="init",destroyMethod="close",name="dataSource")
	public DataSource dataSource(){
		if (StringUtils.isEmpty(prop.getProperty("url"))) {
			log.error("数据库连接池配置错误,请检查spring配置文件,当前spring配置文件为："+Arrays.toString(environment.getActiveProfiles()));
            throw new ApplicationContextException("数据库连接池配置错误!");
        }else{
        	DruidDataSource druid=new DruidDataSource();
        	//设置连接池连接基本信息
        	druid.setUrl(prop.getProperty("url"));
        	druid.setUsername(prop.getProperty("username"));
        	druid.setPassword(prop.getProperty("password"));
        	druid.setDriverClassName(prop.getProperty("driverClassName"));
        	//连接池初始化参数设置
        	druid.setInitialSize(10);
        	druid.setMinIdle(1);
        	druid.setMaxActive(50);
        	druid.setMaxWait(60000);
        	druid.setTimeBetweenConnectErrorMillis(60000);
        	druid.setMinEvictableIdleTimeMillis(300000);
        	druid.setValidationQuery("SELECT 'x'");
        	druid.setTestWhileIdle(true);
        	druid.setTestOnBorrow(false);
        	druid.setTestOnReturn(false);
        	 try {
        		 druid.setFilters("stat, wall");
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        	return druid;
        }
	}
	
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
  