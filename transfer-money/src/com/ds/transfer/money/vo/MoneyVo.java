package com.ds.transfer.money.vo;

/**
 * 钱包中心实体vo
 * 
 * @author jackson
 *
 */
public class MoneyVo {

	private String fromKey;//是	由我公司提供
	private int siteId;//是	网站id
	private String remitno;//是	存取序号（唯一值），可以用A公司存取记录流水号，避免重复存取
	private String username;//是	用户名
	private String transType;//是	转入或转出标识（in:转入 out：转出）
	private String remit;//是	存取金额，均为正数
	private String hashCode;//否	用我公司提供
	private int wagerCancel;//否	0:否   1是（如果注单取消，用户金额可能出现负数）
	private Integer fromKeyType;//是	转账类型
	private String memo;//是	不能为空，（什么用途，比如人工转账，补帐，游戏输赢，第三方）
	private String key;//是	验证码，需要小写，组成方式如下：key=A+B+C（验证码组合方式）,A无意义字符串长度5码,B MD5（fromKey+username+remitno）,C 无意义字符串长度6码

	/** 必填 */
	public MoneyVo(String fromKey, int siteId, String remitno, String username, String transType, String remit, Integer fromKeyType, String memo, String key) {
		this.fromKey = fromKey;
		this.siteId = siteId;
		this.remitno = remitno;
		this.username = username;
		this.transType = transType;
		this.remit = remit;
		this.fromKeyType = fromKeyType;
		this.memo = memo;
		this.key = key;
	}

	/** 查询转账 */
	public MoneyVo(String fromKey, int siteId, String remitno, String username, String key) {
		this.fromKey = fromKey;
		this.siteId = siteId;
		this.remitno = remitno;
		this.username = username;
		this.key = key;
	}

	/** 查询余额 */
	public MoneyVo(String fromKey, int siteId, String username, String key) {
		this.fromKey = fromKey;
		this.siteId = siteId;
		this.username = username;
		this.key = key;
	}

	public String getFromKey() {
		return fromKey;
	}

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getRemitno() {
		return remitno;
	}

	public void setRemitno(String remitno) {
		this.remitno = remitno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getRemit() {
		return remit;
	}

	public void setRemit(String remit) {
		this.remit = remit;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public int getWagerCancel() {
		return wagerCancel;
	}

	public void setWagerCancel(int wagerCancel) {
		this.wagerCancel = wagerCancel;
	}

	public Integer getFromKeyType() {
		return fromKeyType;
	}

	public void setFromKeyType(Integer fromKeyType) {
		this.fromKeyType = fromKeyType;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
