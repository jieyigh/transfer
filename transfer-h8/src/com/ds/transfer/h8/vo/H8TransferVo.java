package com.ds.transfer.h8.vo;

public class H8TransferVo {

	private String secret;
	private String agent;
	private String username;
	private String action;
	private String serial;
	private String amount;

	//update
	private String max1;//<Max Single Bet Limit>  
	private String lim1;//<Match Limit>    
	private String lim2;//<Mix Parlay Limit Per Combination>
	private String comtype;//<Choice of A,B,C,D,E,F,4 for HDP/OU/OE>
	private String com1;//<Commission for HDP/OU/OE>
	private String com2;//<Commisson for 1X2/Outright>
	private String com3;//<Commission for PAR/CS/TG >
	private String suspend;//延迟<0: no suspend, 1:suspend>

	//login
	private String host;
	private String lang;
	private String accType;

	/** transfer */
	public H8TransferVo(String secret, String agent, String username, String action, String serial, String amount) {
		this.secret = secret;
		this.agent = agent;
		this.username = username;
		this.action = action;
		this.serial = serial;
		this.amount = amount;
	}

	/** query balance */
	public H8TransferVo(String secret, String agent, String username, String action) {
		this.secret = secret;
		this.agent = agent;
		this.username = username;
		this.action = action;
	}

	/**  update */
	public H8TransferVo(String secret, String agent, String username, String action, String max1, String lim1, String comtype, String com1, String com2, String com3, String suspend) {
		this.secret = secret;
		this.agent = agent;
		this.username = username;
		this.action = action;
		this.max1 = max1;
		this.lim1 = lim1;
		this.comtype = comtype;
		this.com1 = com1;
		this.com2 = com2;
		this.com3 = com3;
		this.suspend = suspend;
	}

	/** login */
	public H8TransferVo(String secret, String agent, String username, String action, String host, String lang, String accType) {
		this.secret = secret;
		this.agent = agent;
		this.username = username;
		this.action = action;
		this.host = host;
		this.lang = lang;
		this.accType = accType;
	}

	/** Check Payment */
	public H8TransferVo(String secret, String agent, String username, String action, String serial) {
		this.secret = secret;
		this.agent = agent;
		this.username = username;
		this.action = action;
		this.serial = serial;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMax1() {
		return max1;
	}

	public void setMax1(String max1) {
		this.max1 = max1;
	}

	public String getLim1() {
		return lim1;
	}

	public void setLim1(String lim1) {
		this.lim1 = lim1;
	}

	public String getLim2() {
		return lim2;
	}

	public void setLim2(String lim2) {
		this.lim2 = lim2;
	}

	public String getComtype() {
		return comtype;
	}

	public void setComtype(String comtype) {
		this.comtype = comtype;
	}

	public String getCom1() {
		return com1;
	}

	public void setCom1(String com1) {
		this.com1 = com1;
	}

	public String getCom2() {
		return com2;
	}

	public void setCom2(String com2) {
		this.com2 = com2;
	}

	public String getCom3() {
		return com3;
	}

	public void setCom3(String com3) {
		this.com3 = com3;
	}

	public String getSuspend() {
		return suspend;
	}

	public void setSuspend(String suspend) {
		this.suspend = suspend;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

}
