package com.boot.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**  
 *
 *项目名称：springShiro
 *
 *描述：权限
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年11月25日 下午2:31:55 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Entity
@Table(name="sys_permission")
public class Permission implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8202368910675842410L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="permission")
	private String permission;
	@Column(name="roleId")
	private Integer roleId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
  