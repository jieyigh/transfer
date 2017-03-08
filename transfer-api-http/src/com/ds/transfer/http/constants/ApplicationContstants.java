package com.ds.transfer.http.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.http.service.SupportTransferService;

public class ApplicationContstants {

	/**
	 * 存储转账
	 */
	public static final Map<String, TransferService<?>> TRANSFER_SERVICE_MAP = new ConcurrentHashMap<String, TransferService<?>>();

	/**
	 * 存储支撑转账
	 */
	public static final Map<String, SupportTransferService<?>> SUPPORT_SERVICE_MAP = new ConcurrentHashMap<String, SupportTransferService<?>>();

	/**
	 * 每个平台的线程数
	 */
	public interface TotalBalance {
		int AG_THREAD_COUNT = 20;
		int BBIN_THREAD_COUNT = 20;
	}
}
