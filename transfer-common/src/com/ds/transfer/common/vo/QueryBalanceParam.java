package com.ds.transfer.common.vo;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

/**
 * 查询余额所需参数
 * 
 * @author jackson
 *
 */
public class QueryBalanceParam implements Serializable {

	private static final long serialVersionUID = 8634718150541610215L;
	
	private ApiInfoEntity entity;
	private String username;
	private String cur;

	public QueryBalanceParam() {
	}

	public QueryBalanceParam(ApiInfoEntity entity, String username, String cur) {
		this.entity = entity;
		this.username = username;
		this.cur = cur;
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

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

}
