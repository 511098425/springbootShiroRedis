package com.boot.config.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**  
 *
 *项目名称：springShiro
 *
 *描述：activeMq配置
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年12月9日 下午4:09:12 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Configuration
@EnableJms
public class ActiveMQConfig {
	
	@Bean
	public Queue queue(){
		return new ActiveMQQueue("sample.queue");
	}

}
  