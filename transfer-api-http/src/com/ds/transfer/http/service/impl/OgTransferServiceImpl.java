package com.ds.transfer.http.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.http.service.OgTransferService;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.OgApiUserEntity;

@Service
public class OgTransferServiceImpl extends CommonTransferService implements OgTransferService<OgApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "ogTransfer")
	private TransferService<OgApiUserEntity> ogTransfer;

	@Resource(name = "moneyCenter")
	private TransferService<?> moneyCenter;

	@Override
	public String transfer(TransferParam param) {
		String transferRemark = null;
		String oppositeType = null;
		String result = null;
		String billno = null;
		Map<String, Object> resultMap = null;
		String providerIp = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			billno = param.getBillno();
			String type = param.getType();
			if (IN.equals(type)) {
				if (param.isTotalBalance()) {
					transferRemark = "DS主账户 转账至 OG视讯(资金归集)";
					param.setBillno(billno + "T" + "_" + entity.getLiveName());
				} else {
					transferRemark = "DS主账户 转账至 OG视讯" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = OUT;
				param.setType(oppositeType);
				param.setRemark(transferRemark);
				param.setLiveId(SysConstants.LiveId.OG);
				result = this.moneyCenter.transfer(param);
				providerIp = printProviderIp();
				logger.info("money transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(type);
					param.setBillno(billno);
					result = this.ogTransfer.transfer(param);
					providerIp = printProviderIp();
					logger.info("og transfer ip = {}, result = {}", providerIp, result);
				}
			} else {
				if (param.isTotalBalance()) {
					transferRemark = "OG视讯 转账至 DS主账户(资金归集)";
					billno = billno + "T" + "_" + entity.getLiveName();
				} else {
					transferRemark = "OG视讯 转账至 DS主账户" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = IN;
				param.setType(type);
				param.setRemark(transferRemark);
				result = this.ogTransfer.transfer(param);
				providerIp = printProviderIp();
				logger.info("og transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(oppositeType);
					param.setLiveId(SysConstants.LiveId.OG);
					param.setBillno(billno);
					result = this.moneyCenter.transfer(param);
					providerIp = printProviderIp();
					logger.info("money transfer ip = {}, result = {}", providerIp, result);
				}
			}
			resultMap = JSONUtils.json2Map(result);
		} catch (Exception e) {
			logger.error("money<---->og转账错误", e);
			return JSONUtils.map2Json(maybe("og转账异常"));
		}
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		ApiInfoEntity entity = param.getEntity();
		Map<String, Object> resultMap = null;
		String result = null;
		String providerIp = null;
		try {
			//1.用户存在? 
			UserParam userParam = new UserParam(entity, param.getUsername(), null, null, null);
			resultMap = this.checkAndCreateMember(userParam);
			if (!SUCCESS.equals(resultMap.get(STATUS))) {
				return JSONUtils.map2Json(resultMap);
			}
			//2.查询余额
			result = this.ogTransfer.queryBalance(param);
			providerIp = printProviderIp();
			logger.info("og queryBalance ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("og查询余额异常 : ", e);
			return JSONUtils.map2Json(maybe("og查询余额异常"));
		}
	}

	@Override
	public String login(LoginParam param) {
		Map<String, Object> resultMap = null;
		String result = null;
		String providerIp = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			//1.用户存在? 
			UserParam userParam = new UserParam(entity, param.getUsername(), null, null, entity.getIsDemo() + "");
			OgApiUserEntity user = this.queryUserExist(entity.getPrefix() + param.getUsername());
			if (user == null) {
				resultMap = this.ogTransfer.checkAndCreateMember(userParam);
				if (!SUCCESS.equals(resultMap.get(STATUS))) {
					return JSONUtils.map2Json(resultMap);
				}
			}

			//2.登录
			result = this.ogTransfer.login(param);
			providerIp = printProviderIp();
			logger.info("og login ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("og登录异常 : ", e);
			return JSONUtils.map2Json(maybe("og登录异常"));
		}
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return null;
	}

	@Override
	public OgApiUserEntity queryUserExist(String username) {
		return this.ogTransfer.queryUserExist(username);
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = this.ogTransfer.checkAndCreateMember(param);
		String providerIp = printProviderIp();
		logger.info("og checkAndCreateMember ip = {}, result = {}", providerIp, JSONUtils.map2Json(resultMap));
		return resultMap;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return this.ogTransfer.queryStatusByBillno(param);
	}

	@Override
	public String totalBalanceBySiteId(TotalBalanceParam param) {
		// TODO Auto-generated method stub
		return null;
	}

}
