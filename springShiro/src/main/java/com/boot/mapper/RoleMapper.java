package com.boot.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.boot.config.mybatis.baseMapper.BaseMapper;
import com.boot.entity.SysRole;

/**  
 *
 *项目名称：WeddingBOS
 *
 *描述：门店Mapper
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年8月4日 下午5:15:09 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
public interface RoleMapper extends BaseMapper<SysRole>{
	
	
	/**
	 * @param userId
	 * @return
	 */
	public Set<String> selectUserRole(@Param("userId")Integer userId);
	
	
	public List<SysRole> selectRoles(@Param("userId")Integer userId);
}
  