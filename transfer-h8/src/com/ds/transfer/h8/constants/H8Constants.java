package com.ds.transfer.h8.constants;

public interface H8Constants {

	String H8 = "h8";
	String STATE_SUCCESS = "0";
	String LIVE_ID = "13";
	int MAX = 5000;
	int MAX_TEST = 50;
	int LIM = 20000;
	int LIM_TEST = 100;
	String TRY_PLAY_AGENT = "3r01";
	String COMTYPE_A = "A";
	String LANG = "zh-cn";
	String LOGIN_ACC_TYPE = "HK";
	int COM1 = 0;
	int COM2 = 0;
	int COM3 = 0;
	int SUSPEND = 0;

	interface function {
		String CREATE_MEMBER = "create";
		String UPDATE = "update";
		String LOGIN = "login";
		
		String TRANSFER_IN = "deposit";
		String TRANSFER_OUT = "withdraw";
		String QUERY_BALANCE = "balance";
		String QUERY_ORDER_STATUS = "check_payment";
	}

}
