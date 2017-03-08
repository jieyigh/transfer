package com.ds.transfer.ds.vo;

/**
 * ds transfer vo
 * 
 * @author jackson
 *
 */
public class MoneyVo {

	private String username;
	private String password; // md5
	private String ref;
	private String desc;
	private String amount;

	public MoneyVo(String username, String password, String ref, String desc, String amount) {
		this.username = username;
		this.password = password;
		this.ref = ref;
		this.desc = desc;
		this.amount = amount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
