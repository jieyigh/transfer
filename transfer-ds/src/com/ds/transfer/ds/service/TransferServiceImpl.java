package com.ds.transfer.ds.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
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
import com.ds.transfer.ds.constants.DsConstants;
import com.ds.transfer.ds.vo.LoginVo;
import com.ds.transfer.ds.vo.MoneyVo;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.DsApiUserEntity;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.service.TransferRecordService;

public class TransferServiceImpl extends CommonTransferService implements TransferService<DsApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Override
	public String transfer(TransferParam transferParam) {
		TransferRecordEntity dsRecord = new TransferRecordEntity();
		String username = null;
		String msg = null;
		try {
			ApiInfoEntity entity = transferParam.getEntity();
			username = transferParam.getUsername();
			//ag转账记录
			dsRecord = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), username, transferParam.getCredit(), transferParam.getBillno(),//
					transferParam.getType(), DsConstants.DS, transferParam.getRemark(), dsRecord);
			logger.info("ds转账记录插入成功,  id = {}", dsRecord.getId());
			MoneyVo vo = new MoneyVo(transferParam.getUsername(), entity.getPassword(), transferParam.getBillno(),//
					"ds钱包转" + (IN.equals(transferParam.getType()) ? "入" : "出"), transferParam.getCredit());
			String paramBody = JSONUtils.bean2Json(vo);
			Map<String, Object> param = new HashMap<>();
			param.put("hashCode", entity.getHashcode());
			param.put("command", (IN.equals(transferParam.getType()) ? DsConstants.IN : DsConstants.OUT));
			param.put("params", paramBody);
			String result = StringsUtil.sendPost1(entity.getReportUrl(), JSONUtils.map2Json(param));
			JSONObject resultObj = JSONObject.parseObject(result);
			if ("0".equals(resultObj.getString("errorCode"))) {
				logger.info("ds确认转账成功,type= {}", transferParam.getType());
				dsRecord = this.transferRecordService.update(SysConstants.Record.TRANS_SUCCESS, "DS转账成功", dsRecord);
				return JSONUtils.map2Json(success("AG转账成功"));
			}
			logger.info("ds转账失败");
			dsRecord = this.transferRecordService.update(SysConstants.Record.TRANS_FAILURE, "DS转账失败", dsRecord);
			return JSONUtils.map2Json(failure("DS转账失败"));
		} catch (Exception e) {
			logger.error("DS主账户查询余额出错", e);
			logger.info("ds转账异常");
			dsRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "DS转账异常", dsRecord);
			return JSONUtils.map2Json(maybe("system error !" + msg));
		}
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		try {
			ApiInfoEntity entity = param.getEntity();
			Map<String, Object> dsParam = new HashMap<>();
			dsParam.put("hashCode", entity.getHashcode());
			dsParam.put("command", "GET_BALANCE");
			Map<String, Object> paramBody = new HashMap<>();
			paramBody.put("username", param.getUsername());
			paramBody.put("password", entity.getPassword());
			dsParam.put("params", paramBody);
			String result = StringsUtil.sendPost1(entity.getReportUrl(), JSONUtils.map2Json(dsParam));
			JSONObject resultObj = JSONObject.parseObject(result);
			if ("0".equals(resultObj.getString("errorCode"))) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(STATUS, SUCCESS);
				resultMap.put(MESSAGE, resultObj.getJSONObject("params").getString("balance"));
				resultMap.put("balance", resultObj.getJSONObject("params").getString("balance"));
				return JSONUtils.map2Json(resultMap);
			}
			return JSONUtils.map2Json(failure(result));
		} catch (Exception e) {
			logger.error("查询余额失败 : ", e);
			return JSONUtils.map2Json(maybe("查询余额失败"));
		}
	}

	@Override
	public String login(LoginParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			String gameType = param.getGameType();
			LoginVo vo = new LoginVo(entity.getAgent(), "LOGIN");
			LoginVo.Param params = null;
			if (gameType.startsWith("LOTT")) {//彩票登录
				params = vo.new Param(username, entity.getPassword(), StringsUtil.isNull(param.getCur()) ? entity.getCurrencyType() : param.getCur(), username, param.getLanguage(), Integer.valueOf(param.getLine()), //
						gameType, param.getLottoTray(), username, param.getLottoType(), "LOTTO".equalsIgnoreCase(gameType));
			} else {//大厅登录
				params = vo.new Param(username, entity.getPassword(), StringsUtil.isNull(param.getCur()) ? entity.getCurrencyType() : param.getCur(), username, param.getLanguage(), Integer.valueOf(param.getLine()), username);
			}
			vo.setParams(params);
			String loginParam = JSONUtils.bean2Json(vo);
			String url = entity.getReportUrl();
			result = StringsUtil.sendPost1(url, loginParam);
			JSONObject jsonMap = JSONObject.parseObject(result);
			if ("0".equals(jsonMap.getString("errorCode"))) {
				result = jsonMap.getJSONObject("params").getString("link");
				return JSONUtils.map2Json(success(result));
			}
		} catch (Exception e) {
			logger.error("钱包中心登录错误 : ", e);
			return JSONUtils.map2Json(maybe("system error : " + result));
		}
		return JSONUtils.map2Json(failure(result));
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	@Deprecated
	public DsApiUserEntity queryUserExist(String username) {
		return null;
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		return null;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		try {
			ApiInfoEntity entity = param.getEntity();
			Map<String, Object> dsParam = new HashMap<>();
			dsParam.put("hashCode", entity.getHashcode());
			dsParam.put("command", "CHECK_REF");
			Map<String, Object> paramBody = new HashMap<>();
			paramBody.put("ref", param.getBillno());
			dsParam.put("params", paramBody);
			String result = StringsUtil.sendPost1(entity.getReportUrl(), JSONUtils.map2Json(dsParam));
			JSONObject resultObj = JSONObject.parseObject(result);
			if ("6601".equals(resultObj.getString("errorCode"))) {
				return JSONUtils.map2Json(success("success!"));
			} else if ("6617".equals(resultObj.getString("errorCode"))) {
				return JSONUtils.map2Json(maybe("billno = " + param.getBillno() + " is processing!"));
			} else {
				return JSONUtils.map2Json(failure(result));
			}
		} catch (Exception e) {
			logger.error("查询订单失败", e);
			return JSONUtils.map2Json(failure("system error!"));
		}
	}
}
