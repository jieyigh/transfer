package com.ds.transfer.common.vo;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

public class TransferParam implements Serializable {

	private static final long serialVersionUID = 3473954243713526060L;

	private ApiInfoEntity entity;
	private String username; //用户名
	private String credit; //金额
	private String billno; //查询唯一标识:时间戳
	private String type; //IN or OUT
	private String cur; //货币类型
	private String remark; //备注

	private String liveId;//转账方liveid,用于钱包中心交互取出fromKeyType||用于转账记录

	private Long transRecordId;// 单词转账记录的id

	private boolean totalBalance;//资金归集

	public TransferParam() {
	}

	public TransferParam(ApiInfoEntity entity, String username, String credit, String billno, String type, String cur, String remark, String liveId, Long transRecordId, boolean totalBalance) {
		this.entity = entity;
		this.username = username;
		this.credit = credit;
		this.billno = billno;
		this.type = type;
		this.cur = cur;
		this.remark = remark;
		this.liveId = liveId;
		this.transRecordId = transRecordId;
		this.totalBalance = totalBalance;
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

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public boolean isTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(boolean totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Long getTransRecordId() {
		return transRecordId;
	}

	public void setTransRecordId(Long transRecordId) {
		this.transRecordId = transRecordId;
	}

}
