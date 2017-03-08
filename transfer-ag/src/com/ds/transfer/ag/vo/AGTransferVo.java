package com.ds.transfer.ag.vo;

import java.math.BigDecimal;

/**
 * ag预转账
 * 
 * @author jackson
 *
 */
public class AGTransferVo {

	public static final String FLAG_SUCCESS = "1";

	private String cagent;//代理编码， 数值 =“XXXXXXXX”, 这是一個常數
	private String loginname;//游戏账号的登錄名, 必須少于 20 個字元,不可以带特殊字符，只可以数字，字母，下划线
	private String method;//值 = “tc” 代表”预备转账 PrepareTransferCredit”, 是一个常数
	private String billno;//billno = (cagent+序列), 序列是唯一的13~16位数,
	private String type;//值 = “IN” or “OUT”;IN: 从网站账号转款到游戏账号;OUT: 從遊戲账號转款到網站賬號
	/**
	 * 如使用  type=OUT,  转出额度时,  只能整数转出,  不能带小数
	 */
	private BigDecimal credit;//转款额度(如 000.00), 只保留小数点后两个位,

	private int actype;//actype= “1” 代表真钱账号;actype= “0” 代表试玩账号
	private String flag;//值 = 1 代表调用‘预备转账 PrepareTransferCredit’ API成功
	private String password;

	private String dm;//‘dm’ 代表返回的网站域名,例如：您的网站域名是 www.bet.com, dm = www.bet.com

	private String sid;//sid = (cagent+序列), 序列是唯一的13~16位数,

	/**
	 * 0 大厅
	1 旗舰厅 (AGQ 厅)
	2 国际厅 (AGIN 厅)
	3 多台 (自选多台)
	4 包桌 (VIP 包桌)
	5 竞咪 (竞咪厅)
	6 捕鱼王
	8 电子游戏 (电子游戏大厅)
	101 水果拉霸
	102 新视频扑克(杰克高手)
	103 美女沙排
	104 运财羊
	 */
	private String gameType;

	/**
	 * zh-cn (简体中文)  1
	zh-tw (䌓体中文）  2
	en-us(英语)  3
	euc-jp(日语)  4
	ko(韩语)  5
	th(泰文)  6
	es(西班牙文)  7
	vi(越南文)  8
	khm(柬埔寨)  9
	lao(老挝)  10
	id(印尼语)  11
	myr(马来西亚)  12
	es(西班牙)  13
	mx(墨西哥)  14
	de(德语)  15
	fr(法文)  16
	el(希腊文)  17
	it(意大利文)  18
	pl(波兰文)  19
	ru(俄语)  20
	hu(匈牙利文)  21
	ro(罗马尼亚语)  22
	 */
	private String lang;
	/**
	 * 人民币  CNY
		港币  HKD
		韩元  KRW
		马来西亚币  MYR
		新加坡币  SGD
		美元  USD
		日元  JPY
		泰铢  THB
		页 17 / 32 Copyright©AsiaGaming
		比特币  BTC
		印尼盾  IDR
		越南盾  VND
		欧元  EUR
		澳元  AUD
		英镑  GBP
		瑞士元  CHF
		墨西哥比索  MXP
		加拿大元  CAD
		俄罗斯卢布  RUB
		印度卢比  INR
		罗马尼亚币  RON
		丹麦克朗  DKK
		挪威克朗  NOK
	 */
	private String cur;

	private String key;

	/**
	 * 盘口, 设定新玩家可下注的范围
		值: : A、B、C、D、E、F、G、H 及 I
		默认值: : A
		玩家的下注范围( ( 人民币) ): :
		其他币种 的 下注范围,  请参考 AG 国际厅游戏后
		台管理系统 内“ “ 系统公共盘口” ”
		A (20~50000)
		B (50~5000)
		C (20~10000)
		D (200~20000)
		E (300~30000)
		F (400~40000)
		G (500~50000)
		H (1000~100000)
		I (2000~200000)
	 */
	private String oddtype;

	/**
	 * pre-transfer
	 */
	public AGTransferVo(String cagent, String loginname, String method, String billno, String type, BigDecimal credit, int actype, String password, String cur) {
		this.cagent = cagent;
		this.loginname = loginname;
		this.method = method;
		this.billno = billno;
		this.type = type;
		this.credit = credit;
		this.actype = actype;
		this.password = password;
		this.cur = cur;
	}

	/**
	 * transfer
	 */
	public AGTransferVo(String cagent, String loginname, String method, String billno, String type, BigDecimal credit, int actype, String flag, String password, String cur) {
		this(cagent, loginname, method, billno, type, credit, actype, password, cur);
		this.flag = flag;
	}

	/**
	 * 查询余额
	 */
	public AGTransferVo(String cagent, String loginname, String method, int actype, String password, String cur) {
		this.cagent = cagent;
		this.loginname = loginname;
		this.method = method;
		this.actype = actype;
		this.password = password;
		this.cur = cur;
	}

	/** 登录 */
	public AGTransferVo(String cagent, String loginname, String password, int actype, String lang, String dm, String sid, String cur, String oddtype, String gameType) {
		this.cagent = cagent;
		this.loginname = loginname;
		this.password = password;
		this.actype = actype;
		this.lang = lang;
		this.dm = dm;
		this.sid = sid;
		this.cur = cur;
		this.oddtype = oddtype;
		this.gameType = gameType;
	}

	/** 查询用户是否存在,并创建账号 */
	public AGTransferVo(String cagent, String loginname, String method, int actype, String password, String cur, String oddtype) {
		this.cagent = cagent;
		this.loginname = loginname;
		this.method = method;
		this.actype = actype;
		this.password = password;
		this.cur = cur;
		this.oddtype = oddtype;
	}

	/** 查询订单状态所需参数 */
	public AGTransferVo(String cagent, String method, String billno, int actype, String cur) {
		this.cagent = cagent;
		this.method = method;
		this.billno = billno;
		this.actype = actype;
		this.cur = cur;
	}

	public String getCagent() {
		return cagent;
	}

	public void setCagent(String cagent) {
		this.cagent = cagent;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public int getActype() {
		return actype;
	}

	public void setActype(int actype) {
		this.actype = actype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOddtype() {
		return oddtype;
	}

	public void setOddtype(String oddtype) {
		this.oddtype = oddtype;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

}
