package com.ds.transfer.money.test;

import org.junit.Test;

import com.ds.transfer.common.util.StringsUtil;

public class DSTest {

	@Test
	public void testLiveChangPassword() throws Exception {
		String username = "xianjin";
		String password = "b4abf85105d2c0d398ed23fa7bcdf1f1";
		String hashcode = "dsjishucs_784vb8d_df9s84df54_g8s4ss";
		String command = "CHANGE_PASSWORD";
		String params = "{\"hashCode\":\""+hashcode+"\",\"command\":\""+command+"\",\"params\":{\"username\":\""+username+"\",\"password\":\""+password+"\"}}"; 
		String result = StringsUtil.sendPost1("http://dswfapi.dsbet87.com/dsapiwf/app/api.do", params);
		System.out.println(result);
	}

}
