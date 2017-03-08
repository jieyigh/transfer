package com.ds.transfer.common.vo;

import java.io.Serializable;

import com.ds.transfer.record.entity.ApiInfoEntity;

/**
 * 登录公共参数
 * 
 * @author jackson
 *
 */
public class LoginParam implements Serializable {

	private static final long serialVersionUID = 6922806211425138551L;

	private ApiInfoEntity entity;
	private String username;//用户名

	private String billno;//订单号
	private String gameType;//游戏类型
	private String isDemo;//是否试玩
	private String pageSite;//

	private String action;//
	private String accType;//ag; 分分彩--> dcUserTree; 经典彩--> userTree

	private String oddType;//限红
	private String cur;//货币
	private String line;//线路; 分分彩--> dcCustomerId 
	private String language;//语言

	private String lottoTray;
	private String lottoType;

	private String gamekind;//bbin; 
	private String gamecode;//bbin; 

	private String terminal;//终端

	//Mg 新增登录参数
	private String bankingUrl;
	private String lobbyUrl;
	private String logoutRedirectUrl;

	public LoginParam(ApiInfoEntity entity, String username) {
		this.entity = entity;
		this.username = username;
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

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getIsDemo() {
		return isDemo;
	}

	public void setIsDemo(String isDemo) {
		this.isDemo = isDemo;
	}

	public String getPageSite() {
		return pageSite;
	}

	public void setPageSite(String pageSite) {
		this.pageSite = pageSite;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLottoTray() {
		return lottoTray;
	}

	public void setLottoTray(String lottoTray) {
		this.lottoTray = lottoTray;
	}

	public String getLottoType() {
		return lottoType;
	}

	public void setLottoType(String lottoType) {
		this.lottoType = lottoType;
	}

	public String getOddType() {
		return oddType;
	}

	public void setOddType(String oddType) {
		this.oddType = oddType;
	}

	public String getGamekind() {
		return gamekind;
	}

	public void setGamekind(String gamekind) {
		this.gamekind = gamekind;
	}

	public String getGamecode() {
		return gamecode;
	}

	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getBankingUrl() {
		return bankingUrl;
	}

	public void setBankingUrl(String bankingUrl) {
		this.bankingUrl = bankingUrl;
	}

	public String getLobbyUrl() {
		return lobbyUrl;
	}

	public void setLobbyUrl(String lobbyUrl) {
		this.lobbyUrl = lobbyUrl;
	}

	public String getLogoutRedirectUrl() {
		return logoutRedirectUrl;
	}

	public void setLogoutRedirectUrl(String logoutRedirectUrl) {
		this.logoutRedirectUrl = logoutRedirectUrl;
	}

}
