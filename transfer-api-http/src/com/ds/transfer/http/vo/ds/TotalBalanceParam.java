package com.ds.transfer.http.vo.ds;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

/**
 * 统计余额的参数
 * 
 * @author jackson
 *
 */
public class TotalBalanceParam implements Serializable {

	private static final long serialVersionUID = 5511190725890489701L;

	private ApiInfoEntity entity;

	private Integer siteId;

	private String liveType;

	private String fromDate;

	private String toDate;

	public TotalBalanceParam() {
	}

	public TotalBalanceParam(ApiInfoEntity entity, Integer siteId, String liveType, String fromDate, String toDate) {
		this.entity = entity;
		this.siteId = siteId;
		this.liveType = liveType;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public ApiInfoEntity getEntity() {
		return entity;
	}

	public void setEntity(ApiInfoEntity entity) {
		this.entity = entity;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getLiveType() {
		return liveType;
	}

	public void setLiveType(String liveType) {
		this.liveType = liveType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
