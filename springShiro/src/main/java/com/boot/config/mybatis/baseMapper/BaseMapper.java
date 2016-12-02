package com.boot.config.mybatis.baseMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**  
 *
 *项目名称：WeddingBOS
 *
 *描述：基础mapper
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年8月4日 下午3:26:03 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
  