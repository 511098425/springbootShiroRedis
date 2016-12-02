package com.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.entity.SysUser;
import com.boot.mapper.UserMapper;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月25日 下午3:15:05 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	
	public List<SysUser>queryUser(){
		return userMapper.selectAll();
	}

}
  