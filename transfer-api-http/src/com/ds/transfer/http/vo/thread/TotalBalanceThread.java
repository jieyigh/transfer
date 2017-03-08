package com.ds.transfer.http.vo.thread;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.http.util.TransferUtil;

public class TotalBalanceThread implements Callable<Long> {

	private static final Logger logger = LoggerFactory.getLogger(TotalBalanceThread.class);

	private TransferService<?> transferService;

	private QueryBalanceParam queryBalanceParam;

	private CountDownLatch latch;

	public TotalBalanceThread(TransferService<?> transferService, QueryBalanceParam queryBalanceParam, CountDownLatch latch) {
		this.transferService = transferService;
		this.queryBalanceParam = queryBalanceParam;
		this.latch = latch;
	}

	@Override
	public Long call() throws Exception {
		try {
			Map<String, Object> resultMap = JSONUtils.json2Map(this.transferService.queryBalance(queryBalanceParam));
			if ("10000".equals(resultMap.get("status"))) { //成功
				String balance = resultMap.get("message") + "";
				return Long.valueOf(TransferUtil.removeDecimals(balance));
			} else { //失败返回0
				logger.error("username = {}, 查询金额错误!", queryBalanceParam.getUsername());
				return 0L;
			}
		} catch (Exception e) {
			logger.error("处理出错 : ", e);
		} finally {
			latch.countDown();
		}
		return 0L;
	}

}
