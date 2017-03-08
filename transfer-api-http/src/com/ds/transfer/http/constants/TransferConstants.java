package com.ds.transfer.http.constants;

public interface TransferConstants {

	int BIG_MONEY = 200000;//单笔最大转账限额
	String DS_LOGIN_CMD = "LOGIN";
	String H8_LOGIN_ACTION = "login";
	String H8_LOGIN_ACC_TYPE = "HK";
	
	interface TransferMethod{
		String AG = "ag";
		String BBIN = "bbin";
		String H8 = "h8";
		String OG = "og";
		
		String AG_BBIN = "agBbin";
		String AG_H8 = "agH8";
		
		String BBIN_H8 = "bbinH8";
		
		String BALANCE_TOTAL = "balanceTotal";
	}
	
	interface FromKeyType{
		String AG_IN = "20002";
		String AG_OUT = "20005";
		
		String BBIN_IN = "20001";
		String BBIN_OUT = "20004";
		
		String H8_IN = "20003";
		String H8_OUT = "20006";
		
		String OG_IN = "";//TODO: 待给出
		String OG_OUT = "";
	}
	
}