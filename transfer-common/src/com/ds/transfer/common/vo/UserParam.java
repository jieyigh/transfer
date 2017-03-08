package com.ds.transfer.common.vo;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

/**
 * 创建会员存在参数
 * 
 * @author jackson
 *
 */
public class UserParam implements Serializable {

	private static final long serialVersionUID = 4622340834592967436L;

	private ApiInfoEntity entity;
	private String username;
	private String oddtype;
	private String cur;
	private String isDemo;//isDemo 是ag专用: 0=试玩, 1=真钱账号

	public UserParam() {
	}

	public UserParam(ApiInfoEntity entity, String username, String oddtype, String cur, String isDemo) {
		this.entity = entity;
		this.username = username;
		this.oddtype = oddtype;
		this.cur = cur;
		this.isDemo = isDemo;
	}

	public ApiInfoEntity getEntity() {
		return entity;
	}

	public void setEntity(ApiInfoEntity entity) {
		this.entity = entity;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOddtype() {
		return oddtype;
	}

	public void setOddtype(String oddtype) {
		this.oddtype = oddtype;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

	public String getIsDemo() {
		return isDemo;
	}

	public void setIsDemo(String isDemo) {
		this.isDemo = isDemo;
	}

}