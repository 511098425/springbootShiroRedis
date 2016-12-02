package com.boot.config.shiro;

import java.io.Serializable;

import org.apache.shiro.util.SimpleByteSource;

/**  
 *
 *项目名称：springShiro
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月30日 上午11:34:41 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public class CustomSimpleByteSource extends SimpleByteSource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3968372981839017931L;

	public CustomSimpleByteSource(byte[] bytes) {
		super(bytes);
	}


}
  