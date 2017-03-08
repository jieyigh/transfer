package com.ds.transfer.common.constants;

public interface SysConstants {

	String TRY_PLAY = "0";
	String LANGUAGE = "CN";
	String LANGUAGE_Chinese = "zh-cn";
	String MG_LANGUAGE = "zh";   //MG 中文标识
	String CUR = "CNY";
	String ODD_TYPE = "A";
	String AGENT = "F30_AGIN";

	interface Record {
		int TRANS_START = 0;
		int TRANS_MAYBE = 20;
		int TRANS_SUCCESS = 1;
		int TRANS_FAILURE = 50;
	}

	interface LiveId {//AG=2  BBIN=11  DS=12 H8=13
		String AG = "2";
		String BBIN = "11";
		String DS = "12";
		String H8 = "13";
		String OG = "3";
		String MG = "15";      //MG=15
		String XIAOYU = "21";
		String PC = "22";
		String TOTAL = "99999";// 统计资金归集
	}

}
