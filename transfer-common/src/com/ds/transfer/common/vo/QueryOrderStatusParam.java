package com.ds.transfer.common.vo;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

public class QueryOrderStatusParam implements Serializable {

	private static final long serialVersionUID = -5718059568719758694L;

	private ApiInfoEntity entity;

	private String billno;

	private String username;

	private String password;

	private String type;

	private String credit;

	public QueryOrderStatusParam() {
	}

	public QueryOrderStatusParam(ApiInfoEntity entity, String billno) {
		this.entity = entity;
		this.billno = billno;
	}

	/** og查询账单 */
	public QueryOrderStatusParam(ApiInfoEntity entity, String billno, String username, String password, String type, String credit) {
		this.entity = entity;
		this.billno = billno;
		this.username = username;
		this.password = password;
		this.type = type;
		this.credit = credit;
	}

	public ApiInfoEntity getEntity() {
		return entity;
	}

	public void setEntity(ApiInfoEntity entity) {
		this.entity = entity;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

}
