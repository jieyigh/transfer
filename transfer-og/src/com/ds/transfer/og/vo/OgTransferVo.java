package com.ds.transfer.og.vo;

import lombok.Data;

/**
 * og 转账所需参数
 * 
 * @author jackson
 *
 */
@Data
public class OgTransferVo {

	private String agent;

	private String username;

	/**
	 * U.S. Dollar(USD), 
	 * RMB(RMB), 
	 * Malaysia Dollar(MYR), 
	 * Korea Won(KOW), 
	 * Singapore Dollar(SGD), 
	 * Hong Kong Dollar(HKD)
	 */
	private String moneysort;//货币类型

	private String password;

	private String billno;

	private String type;

	private String credit;

	private String limit;//限制游戏Game Limit Ex:1,1,1,1,1,1,1,1,1,1,1,1,1, and 1 is can ,0 is not can.13 numbers

	private String limitvideo;//百家乐/龙虎/骰宝/翻摊

	private String limitroulette;//轮盘的

	private String domain;//域名

	/**
	 * 	1: 视讯
	 *	2: 体育
	 *	3: 彩票
	 *	4: 电子游戏
	 *	11:新平台(明升)
	 *	21:手机体育
	 */
	private String gametype;

	private String gamekind;

	private String platformname;//平台名称:Oriental,ibc,ag,opus

	private String lang;//zh中文，en英文，jp日文，kr 韩文

	private String method;

	public OgTransferVo() {
	}

	/** 转账 */
	public OgTransferVo(String agent, String username, String password, String billno, String type, String credit, String method) {
		this.agent = agent;
		this.username = username;
		this.password = password;
		this.billno = billno;
		this.type = type;
		this.credit = credit;
		this.method = method;
	}

	/** CheckAndCreateAccount（检测并创建游戏帐户） */
	public OgTransferVo(String agent, String username, String moneysort, String password, String limit, String limitvideo, String limitroulette, String method) {
		this.agent = agent;
		this.username = username;
		this.moneysort = moneysort;
		this.password = password;
		this.limit = limit;
		this.limitvideo = limitvideo;
		this.limitroulette = limitroulette;
		this.method = method;
	}

	/** GetBalance（查询余额） */
	public OgTransferVo(String agent, String username, String password, String method) {
		this.agent = agent;
		this.username = username;
		this.password = password;
		this.method = method;
	}

	/** TransferGame（进入游戏）登录 */
	public OgTransferVo(String agent, String username, String password, String domain, String gametype, String gamekind, String platformname, String lang, String method) {
		this.agent = agent;
		this.username = username;
		this.password = password;
		this.domain = domain;
		this.gametype = gametype;
		this.gamekind = gamekind;
		this.platformname = platformname;
		this.lang = lang;
		this.method = method;
	}

}