package com.ds.transfer.http.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.http.service.DsTransferService;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;
import com.ds.transfer.record.entity.DsApiUserEntity;

/**
 * ds业务
 * @author jackson
 *
 */
@Service("dsTransferServiceImpl")
public class DsTransferServiceImpl extends CommonTransferService implements DsTransferService<DsApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "moneyCenter")
	private TransferService<DsApiUserEntity> moneyCenter;

	@Override
	public String transfer(TransferParam transferParam) {
		String result = moneyCenter.transfer(transferParam);
		String providerIp = printProviderIp();
		logger.info("ds transfer ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		String result = this.moneyCenter.queryBalance(param);
		String providerIp = printProviderIp();
		logger.info("ds queryBalance ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String login(LoginParam param) {
		String result = this.moneyCenter.login(param);
		String providerIp = printProviderIp();
		logger.info("ds login ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public DsApiUserEntity queryUserExist(String username) {
		return this.moneyCenter.queryUserExist(username);
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = this.moneyCenter.checkAndCreateMember(param);
		String providerIp = printProviderIp();
		logger.info("ds checkAndCreateMember ip = {}, result = {}", providerIp, JSONUtils.map2Json(resultMap));
		return resultMap;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return this.moneyCenter.queryStatusByBillno(param);
	}

	@Override
	public String totalBalanceBySiteId(TotalBalanceParam param) {
		// TODO Auto-generated method stub
		return null;
	}

}
