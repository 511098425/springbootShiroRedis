package com.boot.service.activeMq;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * 项目名称：springShiro
 *
 * 描述：
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年12月9日 下午4:12:55
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */

@Service
public class MQService {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queue;
	
	public void send(String msg) {// 向指定队列中发送消息
		this.jmsMessagingTemplate.convertAndSend(this.queue, msg);
	}
	

}
