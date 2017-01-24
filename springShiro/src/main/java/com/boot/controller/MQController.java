package com.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.service.activeMq.MQConsumerService;
import com.boot.service.activeMq.MQService;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年12月9日 下午4:15:14 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@RestController
public class MQController {
	
	@Autowired  
    private MQService mqService;  
  
    @Autowired  
    private MQConsumerService consumerService;  
    
    @RequestMapping("/send")  
    public String send() {  
    	mqService.send("this is an activemq message");  
        return "send";  
    }  
      
    @RequestMapping("/receive")  
    public String receive() {  
        String str = consumerService.receive();  
        return str;  
    }  
    
    

}
  