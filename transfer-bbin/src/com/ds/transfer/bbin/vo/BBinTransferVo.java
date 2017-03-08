package com.ds.transfer.bbin.vo;

/**
 * 第三方支付vo
 * 
 * @author jackson
 *
 */
public class BBinTransferVo {

	private String website;//网站名称-->下级代理'kkw910',目前发展就是这个,以后待扩展
	private String username;//会员帐号
	private String uppername;//上层帐号
	private String remitno;//存提序号(唯一值)，可以用A公司存提纪录流水号，避免重覆存提，别名transid
	private String transid;//跟remitno一样,这个用于查账的字段
	private String action;//IN(存入视讯额度) OUT(提出视讯额度)
	private Integer remit;//存提额度(正整数

	private String password;

	/********* playGame start *************/
	private String gamekind;//1:BB体育游戏 15:3D厅 3:视讯游戏3:视讯游戏 3:视讯游戏 5:机率游戏
	private String gametype;
	private String gamecode;
	/********* playGame end *************/
	private String lang;
	private String page_site;

	/**
	 * 验证码(需全小写)，组成方式如下:
	 *	key=A+B+C(验证码组合方式)
	 *	A= 无意义字串长度5码
	 *	B=MD5(website+ username + remitno + KeyB +
	 *	YYYYMMDD)
	 *	C=无意义字串长度6码
	 *	YYYYMMDD为美东时间(GMT-4)(20140528)
	 */
	private String key;

	public BBinTransferVo() {
	}

	/** 必填字段 */
	public BBinTransferVo(String website, String username, String uppername, String remitno, String action, Integer remit, String key) {
		this.website = website;
		this.username = username;
		this.uppername = uppername;
		this.remitno = remitno;
		this.action = action;
		this.remit = remit;
		this.key = key;
	}

	/** 查询余额 */
	public BBinTransferVo(String website, String username, String uppername, String key) {
		this.website = website;
		this.username = username;
		this.uppername = uppername;
		this.key = key;
	}

	/** 创建会员 */
	public BBinTransferVo(String website, String username, String uppername, String key, String password) {
		this.website = website;
		this.username = username;
		this.uppername = uppername;
		this.key = key;
		this.password = password;
	}

	/** 登录 */
	public BBinTransferVo(String website, String username, String uppername, String password, String lang, String page_site, String key) {
		this(website, username, uppername, password, lang, key);
		this.page_site = page_site;
	}

	/** 登陆某个游戏 */
	public BBinTransferVo(String website, String username, String uppername, String password, String lang, String key) {
		this.website = website;
		this.username = username;
		this.uppername = uppername;
		this.password = password;
		this.lang = lang;
		this.key = key;
	}

	/** 玩某个游戏 */
	public BBinTransferVo(String website, String username, String uppername, String password, String gamekind, String gametype, String gamecode, String lang, String key) {
		this.website = website;
		this.username = username;
		this.uppername = uppername;
		this.password = password;
		this.gamekind = gamekind;
		this.gametype = gametype;
		this.gamecode = gamecode;
		this.lang = lang;
		this.key = key;
	}

	/** 查询订单状态 */
	public BBinTransferVo(String website, String transid, String key) {
		this.website = website;
		this.transid = transid;
		this.key = key;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUppername() {
		return uppername;
	}

	public void setUppername(String uppername) {
		this.uppername = uppername;
	}

	public String getRemitno() {
		return remitno;
	}

	public void setRemitno(String remitno) {
		this.remitno = remitno;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getRemit() {
		return remit;
	}

	public void setRemit(Integer remit) {
		this.remit = remit;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getPage_site() {
		return page_site;
	}

	public void setPage_site(String page_site) {
		this.page_site = page_site;
	}

	public String getGamekind() {
		return gamekind;
	}

	public void setGamekind(String gamekind) {
		this.gamekind = gamekind;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public String getGamecode() {
		return gamecode;
	}

	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

}
