package com.ds.transfer.money.util;

/**
 * 钱包中心常量
 * 
 * @author jackson
 *
 */
public interface MoneyConstants {

	String MONEY_CENTER = "center";
	String LIVE_ID = "12";
	
	String TRANS_SUCCESS = "100000";
	String TRANS_NO_MONEY = "110012";

	String LOGIN_CMD = "LOGIN";
	String LOGIN_SUCCESS = "0";
	String LOGIN_MAINTAIN = "6999";
	
	interface Function{
		String TRANS_MONEY = "transMoney";
		String QUERY_BALANCE = "getMoney";
		
		String CHECK_TRANS_MONEY = "checkTransMoney";
	}

}
