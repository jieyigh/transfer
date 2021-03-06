package com.ds.transfer.record.entity;

import java.io.Serializable;

public class OgApiUserEntity implements Serializable {

	private static final long serialVersionUID = 4635303452986514732L;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private Long id;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.username
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String username;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.password
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String password;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.api_info_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private Integer apiInfoId;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.site_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private Integer siteId;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.site_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String siteName;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.agent_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String agentName;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.oddtype
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String oddtype;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.currency_type
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String currencyType;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.create_time
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private String createTime;

	/**
	 * This field was generated by MyBatis Generator.
	 * This field corresponds to the database column og_api_user.user_status
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	private Integer userStatus;

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.id
	 *
	 * @return the value of og_api_user.id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.id
	 *
	 * @param id the value for og_api_user.id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.username
	 *
	 * @return the value of og_api_user.username
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.username
	 *
	 * @param username the value for og_api_user.username
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.password
	 *
	 * @return the value of og_api_user.password
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.password
	 *
	 * @param password the value for og_api_user.password
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.api_info_id
	 *
	 * @return the value of og_api_user.api_info_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public Integer getApiInfoId() {
		return apiInfoId;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.api_info_id
	 *
	 * @param apiInfoId the value for og_api_user.api_info_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setApiInfoId(Integer apiInfoId) {
		this.apiInfoId = apiInfoId;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.site_id
	 *
	 * @return the value of og_api_user.site_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.site_id
	 *
	 * @param siteId the value for og_api_user.site_id
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.site_name
	 *
	 * @return the value of og_api_user.site_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.site_name
	 *
	 * @param siteName the value for og_api_user.site_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName == null ? null : siteName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.agent_name
	 *
	 * @return the value of og_api_user.agent_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.agent_name
	 *
	 * @param agentName the value for og_api_user.agent_name
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName == null ? null : agentName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.oddtype
	 *
	 * @return the value of og_api_user.oddtype
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getOddtype() {
		return oddtype;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.oddtype
	 *
	 * @param oddtype the value for og_api_user.oddtype
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setOddtype(String oddtype) {
		this.oddtype = oddtype == null ? null : oddtype.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.currency_type
	 *
	 * @return the value of og_api_user.currency_type
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getCurrencyType() {
		return currencyType;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.currency_type
	 *
	 * @param currencyType the value for og_api_user.currency_type
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType == null ? null : currencyType.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.create_time
	 *
	 * @return the value of og_api_user.create_time
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.create_time
	 *
	 * @param createTime the value for og_api_user.create_time
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime == null ? null : createTime.trim();
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method returns the value of the database column og_api_user.user_status
	 *
	 * @return the value of og_api_user.user_status
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public Integer getUserStatus() {
		return userStatus;
	}

	/**
	 * This method was generated by MyBatis Generator.
	 * This method sets the value of the database column og_api_user.user_status
	 *
	 * @param userStatus the value for og_api_user.user_status
	 *
	 * @mbggenerated Thu Nov 26 17:10:08 CST 2015
	 */
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
}