package com.ds.transfer.scan.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ds.transfer.common.service.TransferService;

public class ApplicationConstants {

	public static final Map<String, TransferService<?>> transerServiceMap = new ConcurrentHashMap<String, TransferService<?>>();
}
