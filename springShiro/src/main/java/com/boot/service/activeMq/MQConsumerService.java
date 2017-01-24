package com.boot.service.activeMq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年12月9日 下午4:14:25 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Service
public class MQConsumerService {
	
	private String text;  
	  
    @JmsListener(destination = "sample.queue")//监听指定消息队列  
    public void receiveQueue(String text) {  
        this.text = text;  
        System.out.println(text);  
    }  
  
    public String receive() {  
        return text;  
    }  

}
  