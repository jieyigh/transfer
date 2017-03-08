package com.ds.transfer.scan.vo;

import lombok.Data;

/**
 * 钱包中心交互的vo
 * 
 * @author jackson
 *
 */
@Data
public class MoneyVo {

	private Integer fromKey;
	private String remitno;
	private String username;
	private Integer siteId;
	private String key;

	public MoneyVo() {
	}

	public MoneyVo(Integer fromKey, String remitno, String username, Integer siteId, String key) {
		this.fromKey = fromKey;
		this.remitno = remitno;
		this.username = username;
		this.siteId = siteId;
		this.key = key;
	}

}
