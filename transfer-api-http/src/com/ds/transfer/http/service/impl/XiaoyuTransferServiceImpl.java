package com.ds.transfer.http.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.http.service.XiaoyuTransferService;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;

@Service("xiaoyuTransferServiceImpl")
public class XiaoyuTransferServiceImpl extends CommonTransferService implements XiaoyuTransferService<Object> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "xiaoyuLotto")
	private TransferService<?> xiaoyuTransferService;

	@Override
	public String transfer(TransferParam transferParam) {
		return null;
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		return null;
	}

	@Override
	public String login(LoginParam param) {
		//验证代理层级
		if (StringsUtil.isNull(param.getAccType())) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(STATUS, USER_TREE_EMPTY);
			resultMap.put(MESSAGE, "user tree is null");
			return JSONUtils.map2Json(resultMap);
		}
		String result = xiaoyuTransferService.login(param);
		String providerIp = printProviderIp();
		logger.info("xiaoyu login ip = {}, result = {}", providerIp, result);
		return result;
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return null;
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
