package com.ds.transfer.common.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.rpc.RpcContext;

public class CommonTransferService {

	protected static final String SUCCESS = "10000";
	protected static final String ERROR = "100020";
	protected static final String TRANS_NO_MONEY = "100012";
	protected static final String USER_TREE_EMPTY = "100050";
	protected static final String MAYBE = "100099";
	protected static final String MAINTAIN = "100030";

	protected static final String STATUS = "status";
	protected static final String MESSAGE = "message";

	protected static final String IN = "IN";//转入
	protected static final String OUT = "OUT";//转出

	protected Map<String, Object> success(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.success(resultMap, msg);
	}

	protected Map<String, Object> failure(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.failure(resultMap, msg);
	}

	protected Map<String, Object> maybe(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.maybe(resultMap, msg);
	}

	protected Map<String, Object> success(Map<String, Object> resultMap, String msg) {
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> failure(Map<String, Object> resultMap, String msg) {
		resultMap.put(STATUS, ERROR);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> maybe(Map<String, Object> resultMap, String msg) {
		resultMap.put(STATUS, MAYBE);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected String printProviderIp() {
		//消费端?
		if (RpcContext.getContext().isConsumerSide()) {
			//获取当前线程最后一次调用的提供方IP地址
			return RpcContext.getContext().getRemoteHost();
		}
		return null;
	}

}
