package com.ds.transfer.http.entity;

/**
 * 转账状态
 * 
 * @author jackson
 *
 */
public enum TransferStatus {

	//0=正在转账,1=转账成功 50=转账失败 20=转账异常
	START(0), SUCCESS(1), FAILURE(50), MAYBE(20);

	private int statusCode;

	// 构造函数，枚举类型只能为私有
	private TransferStatus(int statusCode) {

		this.statusCode = statusCode;

	}

	public int value() {
		return this.statusCode;
	}
}