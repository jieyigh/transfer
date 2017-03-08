package com.ds.transfer.scan.constants;

/**
 * 系统常量
 * 
 * @author jackson
 *
 */
public interface ScanConstants {

	int IS_DEMO = 1;

	interface TransType {
		String IN = "IN";
		String OUT = "OUT";
	}

	interface TransStatus {
		//0=正在转账,1=转账成功 50=转账失败 20=转账异常 10=客服处理
		int ING = 0;
		int SUCCESS = 1;
		int FAILURE = 50;
		int MAYBE = 20;
		int SERVICE_PROCESS = 10;
	}

	interface liveId {
		int DS = 12;
		int AG = 2;
		int BBIN = 11;
		int H8 = 13;
		int OG = 3;
		int MG = 15;
	}

	interface LiveType {
		String DS = "center";
		String AG = "ag";
		String BBIN = "bbin";
		String H8 = "h8";
		String OG = "OG";
	}

}
