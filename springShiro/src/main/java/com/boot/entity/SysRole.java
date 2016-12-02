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
 * 项目名称：springShiro
 *
 * 描述：角色
 *
 * 创建人：Mr.chang
 *
 * 创建时间：2016年11月25日 下午2:29:43
 * 
 * Copyright @ 2016 by Mr.chang
 * 
 */
@Entity
@Table(name = "sys_role")
public class SysRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7447755268611628478L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="rolename")
	private String rolename;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	

}
