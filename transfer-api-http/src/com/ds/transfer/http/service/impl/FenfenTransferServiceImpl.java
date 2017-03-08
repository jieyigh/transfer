package com.ds.transfer.http.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.http.service.FenfenTransferService;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;

@Service("fenfenTransferServiceImpl")
public class FenfenTransferServiceImpl extends CommonTransferService implements FenfenTransferService<Object> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "fenfenLotto")
	private TransferService<?> fenfenTransferService;

	@Override
	public String transfer(TransferParam transferParam) {
		return null;
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		String result = this.fenfenTransferService.queryBalance(param);
		String providerIp = printProviderIp();
		logger.info("fenfen queryBalance ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String login(LoginParam param) {
		String result = this.fenfenTransferService.login(param);
		String providerIp = printProviderIp();
		logger.info("fenfen login ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return login(param);
	}

	@Override
	public Object queryUserExist(String username) {
		return null;
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		return null;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return null;
	}

	@Override
	public String totalBalanceBySiteId(TotalBalanceParam param) {
		// TODO Auto-generated method stub
		return null;
	}

}
