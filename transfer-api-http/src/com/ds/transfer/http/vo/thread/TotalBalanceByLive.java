package com.ds.transfer.http.vo.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ds.transfer.http.service.SupportTransferService;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;
import com.ds.transfer.record.entity.ApiInfoEntity;

public class TotalBalanceByLive implements Callable<String> {

	private static final Logger logger = LoggerFactory.getLogger(TotalBalanceByLive.class);

	private SupportTransferService<?> supportService;
	private ApiInfoEntity entity;
	private String fromDate;
	private String toDate;
	private CountDownLatch latch;

	public TotalBalanceByLive(SupportTransferService<?> supportService, ApiInfoEntity entity, String fromDate, String toDate, CountDownLatch latch) {
		this.supportService = supportService;
		this.entity = entity;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.latch = latch;
	}

	@Override
	public String call() throws Exception {
		try {
			TotalBalanceParam param = new TotalBalanceParam(entity, entity.getSiteId(), entity.getLiveName().toLowerCase(), fromDate, toDate);
			return this.supportService.totalBalanceBySiteId(param);
		} catch (Exception e) {
			logger.error("按平台统计 : ", e);
		} finally {
			latch.countDown();
		}
		return null;
	}
}
