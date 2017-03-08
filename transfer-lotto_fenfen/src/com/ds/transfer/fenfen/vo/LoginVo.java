package com.ds.transfer.fenfen.vo;

import lombok.Data;

/**
 * ds 登录
 * 
 * @author jackson
 *
 */
@Data
public class LoginVo {
	/**
	* 用户类型  1代理（一般不用）  2会员 3测试帐号
	*/
	private String dcUserType;

	/**
	 * 平台接入代号
	 */
	private String dcCustomerId;

	/**
	* 请求的平台的链接token 一般用 time() 就行
	*/
	private String dcToken;

	/**
	* 平台的用户名
	*/
	private String dcUsername;

	/**
	 * 平台的用户所属的 网站id
	 */
	private String dcSiteId;

	/**
	 * 用户的级别树。方便分等级统计数据。
	 * 为空的话默认3级分类（商户用户名->网站ID->用户名）
	 * 你可以通过添加中间级别来改变分类 如：  parent1,parent2,parent3那么用户的等级树就是：  商户用户名->parent1->parent2->parent3->用户名
	 */
	private String dcUserTree;

	/**
	* 平台的用户登录之后的默认游戏 cqssc表示 重庆时时彩 其他的看资料
	*/
	private String dcFirstGame;

	/**
	 * 平台的用户盘口，可选值：a,b。赔率依次降低。 可以用于区分主副网，主网a盘，副网b盘，不传默认a盘。
	 */
	private String odds;

	/**
	 * 默认的客户端方式 p：pc端，m:手机端
	 */
	private String clientType;

	public LoginVo() {
	}

	public LoginVo(String dcUserType, String dcCustomerId, String dcToken, String dcUsername, String dcSiteId, String dcUserTree, String dcFirstGame, String odds, String clientType) {
		this.dcUserType = dcUserType;
		this.dcCustomerId = dcCustomerId;
		this.dcToken = dcToken;
		this.dcUsername = dcUsername;
		this.dcSiteId = dcSiteId;
		this.dcUserTree = dcUserTree;
		this.dcFirstGame = dcFirstGame;
		this.odds = odds;
		this.clientType = clientType;
	}

	public String getDcUserType() {
		return dcUserType;
	}

	public void setDcUserType(String dcUserType) {
		this.dcUserType = dcUserType;
	}

	public String getDcCustomerId() {
		return dcCustomerId;
	}

	public void setDcCustomerId(String dcCustomerId) {
		this.dcCustomerId = dcCustomerId;
	}

	public String getDcToken() {
		return dcToken;
	}

	public void setDcToken(String dcToken) {
		this.dcToken = dcToken;
	}

	public String getDcUsername() {
		return dcUsername;
	}

	public void setDcUsername(String dcUsername) {
		this.dcUsername = dcUsername;
	}

	public String getDcSiteId() {
		return dcSiteId;
	}

	public void setDcSiteId(String dcSiteId) {
		this.dcSiteId = dcSiteId;
	}

	public String getDcUserTree() {
		return dcUserTree;
	}

	public void setDcUserTree(String dcUserTree) {
		this.dcUserTree = dcUserTree;
	}

	public String getDcFirstGame() {
		return dcFirstGame;
	}

	public void setDcFirstGame(String dcFirstGame) {
		this.dcFirstGame = dcFirstGame;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	
}
