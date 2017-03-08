package com.ds.transfer.bbin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.ds.transfer.bbin.constants.BbinConstants;
import com.ds.transfer.bbin.vo.BBinTransferVo;
import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.DateUtil;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.ReflectUtil;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.BbinApiUserEntity;
import com.ds.transfer.record.entity.BbinApiUserEntityExample;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.BbinApiUserEntityMapper;
import com.ds.transfer.record.service.TransferRecordService;

/**
 * 转账业务
 * 
 * @author jackson
 *
 */
@Service
public class TransferServiceImpl extends CommonTransferService implements TransferService<BbinApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "moneyCenterService")
	private TransferService<?> moneyCenterService;

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Resource
	private BbinApiUserEntityMapper bbinApiUserEntityMapper;

	@Override
	public String transfer(TransferParam transferParam) {
		TransferRecordEntity bbinRecord = new TransferRecordEntity();
		String typeDes = null;
		String msg = "";
		try {
			String username = transferParam.getUsername();
			ApiInfoEntity entity = transferParam.getEntity();
			username = entity.getPrefix() + username;
			String type = transferParam.getType();
			typeDes = IN.equals(type) ? "入" : "出";

			//1.插入记录
			bbinRecord = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), username, transferParam.getCredit(), transferParam.getBillno(),//
					type, BbinConstants.BBIN, transferParam.getRemark(), bbinRecord);
			logger.info("BBIN视讯 转账记录插入成功,  id = {}", bbinRecord.getId());

			//2.转账
			String bbinKey = bbinKey(BbinConstants.WEB_SITE, username, transferParam.getBillno(), BbinConstants.KEYB);
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, entity.getAgent(), transferParam.getBillno(), type, Integer.valueOf(transferParam.getCredit()), bbinKey);
			String param = ReflectUtil.generateParam(vo);
			String result = StringsUtil.sendPost1(entity.getReportUrl() + BbinConstants.function.TRANSFER, param);
			JSONObject jsonMap = JSONObject.parseObject(result);
			msg = jsonMap.getJSONObject("data").getString("Message");
			if (jsonMap.getBooleanValue("result")) {
				if (BbinConstants.STATE_SUCCESS.equals(jsonMap.getJSONObject("data").get("Code"))) {
					logger.info("bbin 转账成功,type = {}", type);
					//更新转账信息
					bbinRecord = this.transferRecordService.update(SysConstants.Record.TRANS_SUCCESS, "BBIN转账成功", bbinRecord);
					return JSONUtils.map2Json(success("BBIN 转" + typeDes + "成功"));
				}
			}
			bbinRecord = this.transferRecordService.update(SysConstants.Record.TRANS_FAILURE, "BBIN转账失败", bbinRecord);
			if (IN.equals(type)) {
				logger.info("BBIN 转入失败,退回DS主账户");
				transferParam.setRemark("BBIN 转入失败,退回DS主账户");
				transferParam.setBillno(transferParam.getBillno() + "F");
				result = this.moneyCenterService.transfer(transferParam);
				Map<String, Object> resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					return JSONUtils.map2Json(failure(resultMap, "BBIN 转入失败:" + msg + ",退回DS主账户"));
				}
			}
		} catch (Exception e) {
			logger.error("bbin 转" + typeDes + "异常 : ", e);
			bbinRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "BBIN转账异常", bbinRecord);
			return JSONUtils.map2Json(maybe("BBIN 转" + typeDes + "异常"));
		}
		return JSONUtils.map2Json(failure("BBIN 转" + typeDes + "失败:" + msg));
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			username = entity.getPrefix() + username;
			//查询余额
			String key = this.bbinKeyGenerate(BbinConstants.WEB_SITE, username, BbinConstants.BALANCE_KEY, null, null);
			logger.debug("{}, {}, {}, {}", BbinConstants.WEB_SITE, username, entity.getAgent(), key);
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, entity.getAgent(), key);
			String result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendPost1(entity.getReportUrl() + BbinConstants.function.QUERY_BALANCE, result);
			JSONObject json = JSONObject.parseObject(result);
			if (json.getBooleanValue("result")) {
				String balance = json.getJSONArray("data").getJSONObject(0).getString("Balance");
				resultMap = success(balance);
				resultMap.put("balance", balance);
				return JSONUtils.map2Json(resultMap);
			}
			resultMap = failure(json.getJSONObject("data").getString("Message"));
			//result = {"result":false,"data":{"Code":"22006","Message":"Upper Level is not exist."}}
			//{"result":true,"data":[{"LoginName":"hfetest3","Currency":"RMB","Balance":30,"TotalBalance":30}],"pagination":{"Page":1,"PageLimit":500,"TotalNumber":1,"TotalPage":1}}

		} catch (Exception e) {
			logger.error("查询余额异常 : ", e);
			resultMap = maybe("查询余额异常");
		}
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String login(LoginParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();
			String key = this.bbinKeyGenerate(BbinConstants.WEB_SITE, username, BbinConstants.LOGIN_KEY, 7, 5);
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, entity.getAgent(), entity.getPassword(), param.getLanguage(), param.getPageSite(), key);
			result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendPost1(entity.getBbinUrl() + BbinConstants.function.LOGIN, result);
			if (result.contains("网站维护通知") || result.contains("System is in maintenance")) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(STATUS, MAINTAIN);
				resultMap.put(MESSAGE, result);
				return JSONUtils.map2Json(resultMap);
			}
		} catch (Exception e) {
			logger.error("登录异常", e);
			return JSONUtils.map2Json(maybe("登录bbin内部出现异常!"));
		}
		return JSONUtils.map2Json(success(result));
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();
			String key = this.bbinKeyGenerate(BbinConstants.WEB_SITE, username, BbinConstants.LOGIN_KEY, 7, 5);
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, entity.getAgent(), entity.getPassword(), param.getLanguage(), key);
			String loginParam = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendPost1(entity.getBbinUrl() + BbinConstants.function.LOGIN_SINGLE, loginParam);
			if (result.contains("网站维护通知") || result.contains("System is in maintenance")) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(STATUS, MAINTAIN);
				resultMap.put(MESSAGE, result);
				return JSONUtils.map2Json(resultMap);
			}
			if (result.contains("<meta http-equiv='Content-Type'")) {//返回错误
				return JSONUtils.map2Json(failure(result.substring(result.indexOf("("), result.indexOf(")") + 1)));
			}
			JSONObject jsonMap = JSONObject.parseObject(result);
			logger.info("result = {}, code = ", jsonMap.getBooleanValue("result"), jsonMap.getJSONObject("data").getString("Code"));
			if (jsonMap.getBooleanValue("result")) {
				if ("99999".equals(jsonMap.getJSONObject("data").getString("Code"))) {
					result = this.playGame(entity, username, entity.getPassword(), param.getGamekind(), param.getGameType(), param.getGamecode(), param.getLanguage());
					return JSONUtils.map2Json(success(result));
				}
			}
		} catch (Exception e) {
			logger.info("登陆异常 : ", e);
			return JSONUtils.map2Json(maybe("登录bbin电子游戏内部出现异常!"));
		}
		return JSONUtils.map2Json(failure(result));
	}

	@Override
	public BbinApiUserEntity queryUserExist(String username) {
		BbinApiUserEntityExample bbinApiUserExample = new BbinApiUserEntityExample();
		bbinApiUserExample.createCriteria().andUsernameEqualTo(username);
		List<BbinApiUserEntity> list = this.bbinApiUserEntityMapper.selectByExample(bbinApiUserExample);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	@Transactional
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = null;
		try {
			String cur = StringsUtil.isNull(param.getCur()) ? SysConstants.CUR : param.getCur();
			String oddType = StringsUtil.isNull(param.getOddtype()) ? SysConstants.CUR : param.getOddtype();
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();
			BbinApiUserEntity user = this.queryUserExist(username);
			if (user == null) {
				//1.创建bbin会员
				resultMap = this.createMemberByBbin(param, entity, username);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					//2.创建本地会员
					user = this.createMemberByLocal(param, entity, username, cur, oddType);
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("创建bbin会员异常 : ", e);
			return maybe("创建bbin会员异常");
		}
		return success("用户已经存在");
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		String result = null;
		try {
			String billno = param.getBillno();
			ApiInfoEntity entity = param.getEntity();
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, billno, queryOrderStatusKeyGenerate(BbinConstants.WEB_SITE, BbinConstants.CHECK_TRANSFER));
			result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendPost1(entity.getReportUrl() + BbinConstants.function.CHECK_TRANSFER, result);
			JSONObject jsonMap = JSONObject.parseObject(result);
			//			{"result":false,"data":{"Code":"44000","Message":"Key error"}}
			//{"result":true,"data":{"TransID":"1450071241608","TransType":"OUT","Status":1}}
			if (!jsonMap.getBooleanValue("result")) {
				return JSONUtils.map2Json(failure("bbin返回失败 : " + jsonMap.getJSONObject("data").getString("Message")));
			}
			String status = jsonMap.getJSONObject("data").getString("Status");
			if ("1".equals(status)) {
				return JSONUtils.map2Json(success("success!"));
			} else if ("-1".equals(status)) {
				return JSONUtils.map2Json(failure("failure: " + result));
			} else {
				logger.warn("未知状态 = {}", status);
			}
		} catch (Exception e) {
			logger.error("查询订单状态错误 : ", e);
		}
		return JSONUtils.map2Json(failure(result));
	}

	/**
	 * 进入单个游戏
	 */
	private String playGame(ApiInfoEntity entity, String username, String password, String gamekind, String gametype, String gamecode, String lang) {
		try {
			String key = this.bbinKeyGenerate(BbinConstants.WEB_SITE, username, BbinConstants.PLAY_GAME_KEY, 5, 8);
			BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, password, entity.getAgent(), gamekind, gametype, gamecode, lang, key);
			String param = ReflectUtil.generateParam(vo);
			return entity.getBbinUrl() + BbinConstants.function.PLAY_GAME + "?" + param;
		} catch (Exception e) {
			logger.error("bbin进入某个游戏异常 : ", e);
			return null;
		}
	}

	/**
	 * 本地创建会员
	 */
	private BbinApiUserEntity createMemberByLocal(UserParam param, ApiInfoEntity entity, String username, String cur, String oddType) {
		BbinApiUserEntity bbinUser = new BbinApiUserEntity();
		bbinUser.setUsername(username);
		bbinUser.setPassword(entity.getPassword());
		bbinUser.setAgentName(entity.getAgent());
		bbinUser.setApiInfoId(entity.getId().intValue());//api 的 id
		bbinUser.setSiteName(entity.getProjectAgent());
		bbinUser.setCurrencyType(cur);
		bbinUser.setOddtype(oddType);
		bbinUser.setCreateTime(DateUtil.getCurrentTime());//创建时间
		bbinUser.setUserStatus(1);//状态=1 有效会员
		bbinUser.setSiteId(entity.getSiteId());
		bbinUser.setSiteName(entity.getProjectAgent());
		this.bbinApiUserEntityMapper.insert(bbinUser);
		return bbinUser;
	}

	/**
	 * bbin创建会员
	 */
	private Map<String, Object> createMemberByBbin(UserParam param, ApiInfoEntity entity, String username) throws Exception {
		String key = this.bbinKeyGenerate(BbinConstants.WEB_SITE, username, BbinConstants.CREATE_MEMBER_KEY, 2, 4);
		BBinTransferVo vo = new BBinTransferVo(BbinConstants.WEB_SITE, username, entity.getAgent(), key, entity.getPassword());
		String result = ReflectUtil.generateParam(vo);
		result = StringsUtil.sendPost1(entity.getReportUrl() + BbinConstants.function.CREATE_MEMBER, result);
		JSONObject jsonObj = JSONObject.parseObject(result);
		if (jsonObj.getBoolean("result")) {
			logger.info("bbin创建会员成功 : {}", username);
			return success("bbin创建会员成功 : " + username);
		} else {
			if ("21001".equals(jsonObj.getJSONObject("data").getString("Code"))) { // The account is repeated.
				logger.info("bbin创建会员已经存在 : {}", username);
				return success("bbin创建会员已经存在 : " + username);
			}
		}
		return failure(jsonObj.getJSONObject("data").getString("Message"));
	}

	/**
	 * bbin 生成key
	 */
	private String bbinKey(String webSite, String username, String remitno, String keyB) {
		StringBuilder tranBBINkey = new StringBuilder();
		StringBuilder mdBuilder = new StringBuilder();
		mdBuilder.append(webSite).append(username).append(remitno).append(keyB).append(StringsUtil.updateTime());
		tranBBINkey.append(StringsUtil.randomString(8)).append(StringsUtil.toMD5(mdBuilder.toString())).append(StringsUtil.randomString(5));
		return tranBBINkey.toString();
	}

	/**
	 * 查询 bbin 生成key 
	 */
	private String bbinKeyGenerate(String website, String username, String key, Integer start, Integer end) {
		start = start == null ? 1 : start;
		end = end == null ? 1 : end;
		StringBuilder balanceBuilder = new StringBuilder();
		StringBuilder bbinMd5Builder = new StringBuilder();
		bbinMd5Builder.append(website).append(username).append(key).append(StringsUtil.updateTime());
		balanceBuilder.append(StringsUtil.randomString(start)).append(StringsUtil.toMD5(bbinMd5Builder.toString())).append(StringsUtil.randomString(end));
		return balanceBuilder.toString();
	}

	/** 查询订单状态加密方式 */
	private String queryOrderStatusKeyGenerate(String website, String keyb) {
		return StringsUtil.randomString(6) + StringsUtil.toMD5(website + keyb + StringsUtil.updateTime()) + StringsUtil.randomString(2);
	}
}
