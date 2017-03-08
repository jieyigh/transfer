package com.ds.transfer.http.controller;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

	protected static final String IN = "IN";//转入
	protected static final String OUT = "OUT";//转出

	protected static final String SUCCESS = "10000";
	protected static final String ERROR = "100020";
	protected static final String MAYBE = "100099";
	
	protected static final String IP_NOT_ALLOW = "110004";
	protected static final String PARAM_FORMAT_ERROR = "110009";
	protected static final String SITEID_OR_LIVE_NOEXIST = "110020";
	
	//分分彩
	protected static final String CUSTOMER_NO_NULL = "120001";
	protected static final String CUSTOMER_KEY_NULL = "120002";
	
	//小于彩
	protected static final String LOTTO_TRAY_NO_NULL = "130001";

	protected static final String STATUS = "status";
	protected static final String MESSAGE = "message";

	/**
	 * 获取ip
	 */

	protected Map<String, Object> success(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> failure(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(STATUS, ERROR);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> maybe(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(STATUS, MAYBE);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

}
