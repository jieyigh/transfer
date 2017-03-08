package com.ds.transfer.ag.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.transfer.ag.constants.AgConstants;
import com.ds.transfer.ag.util.PHPDESEncrypt;
import com.ds.transfer.ag.util.PropsUtil;
import com.ds.transfer.ag.vo.AGTransferVo;
import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.DateUtil;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.ReflectUtil;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.util.XmlUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.record.entity.AgApiUserEntity;
import com.ds.transfer.record.entity.AgApiUserEntityExample;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.AgApiUserEntityMapper;
import com.ds.transfer.record.service.TransferRecordService;

@Service
public class TransferServiceImpl extends CommonTransferService implements TransferService<AgApiUserEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Resource(name = "moneyCenterService")
	private TransferService<?> moneyCenterService;

	@Autowired
	private AgApiUserEntityMapper agApiUserEntityMapper;

	@Override
	public String transfer(TransferParam transferParam) {
		TransferRecordEntity agRecord = new TransferRecordEntity();
		String msg = "";
		try {
			ApiInfoEntity entity = transferParam.getEntity();
			String username = transferParam.getUsername();
			username = entity.getPrefix() + username;
			String agent = entity.getAgent();//代理名称
			//ag转账记录
			agRecord = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), username, transferParam.getCredit(), transferParam.getBillno(),//
					transferParam.getType(), AgConstants.AG, transferParam.getRemark(), agRecord);
			logger.info("ag转账记录插入成功,  id = {}", agRecord.getId());
			int actype = SysConstants.TRY_PLAY.equals(entity.getIsDemo() + "") ? 0 : 1;
			BigDecimal remit = new BigDecimal(transferParam.getCredit()).setScale(2, BigDecimal.ROUND_DOWN);
			//ag预转账
			AGTransferVo transferVo = new AGTransferVo(agent, username, AgConstants.PRE_TRANSFER,//
					agent + transferParam.getBillno(), transferParam.getType(), remit, actype, entity.getPassword(), transferParam.getCur());
			String params = this.generateAgParam(transferVo, AgConstants.AG_MD5_KEY);
			String result = StringsUtil.sendPost1(AgConstants.AG_URL + AgConstants.Function.BUSINESS_FUNCTION, params);
			XmlUtil readXml = new XmlUtil(result);
			result = readXml.getAttribute("/result/@info").get(0).getValue();
			msg = readXml.getAttribute("/result/@msg").get(0).getValue();
			if (AgConstants.TRANS_SUCCESS.equals(result)) {
				logger.info("ag预转账成功");
				//ag确认转账
				transferVo = new AGTransferVo(agent, username, AgConstants.CONFIRM_TRANSFER, agent + transferParam.getBillno(), //
						transferParam.getType(), remit, actype, AGTransferVo.FLAG_SUCCESS, entity.getPassword(), transferParam.getCur());
				params = generateAgParam(transferVo, AgConstants.AG_MD5_KEY);
				result = StringsUtil.sendPost1(AgConstants.AG_URL + AgConstants.Function.BUSINESS_FUNCTION, params);
				readXml = new XmlUtil(result);
				result = readXml.getAttribute("/result/@info").get(0).getValue();
				msg = readXml.getAttribute("/result/@msg").get(0).getValue();
				if (AgConstants.TRANS_SUCCESS.equals(result)) {
					logger.info("ag确认转账成功,type= {}", transferParam.getType());
					agRecord = this.transferRecordService.update(SysConstants.Record.TRANS_SUCCESS, "AG转账成功", agRecord);
					return JSONUtils.map2Json(success("AG转账成功"));
				}
			}
			logger.info("ag转账异常");
			agRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "AG转账异常", agRecord);
			/*
			 * if (IN.equals(transferParam.getType())) { logger.info("AG转入失败,把钱退回DS主账户"); //this.moneyCenterService.moneyCenterTransfer(entity, transferParam.getPassword(), transferParam.getFromKeyType(), username, Integer.valueOf(transferParam.getCredit()), transferParam.getBillno(), transferParam.getType(), "AG转入失败,退回DS主账户", resultMap); transferParam.setRemark("AG转入失败,把钱退回DS主账户"); transferParam.setBillno(transferParam.getBillno() + "F"); result = this.moneyCenterService.transfer(transferParam); Map<String, Object> resultMap = JSONUtils.json2Map(result); if (SUCCESS.equals(resultMap.get(STATUS))) { return JSONUtils.map2Json(failure(resultMap, "AG转入失败:" + msg + ",把钱退回DS主账户")); } }
			 */
		} catch (Exception e) {
			logger.error("ag转账异常:", e);
			agRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "AG转账异常", agRecord);
			return JSONUtils.map2Json(maybe("AG转账异常"));
		}
		return JSONUtils.map2Json(maybe("AG转账异常:" + msg));
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			username = entity.getPrefix() + username;
			int actype = SysConstants.TRY_PLAY.equals(entity.getIsDemo() + "") ? 0 : 1;

			//1.查询余额
			AGTransferVo vo = new AGTransferVo(entity.getAgent(), username, AgConstants.QUERY_BALANCE, actype, entity.getPassword(), param.getCur());
			String params = this.generateAgParam(vo, AgConstants.AG_MD5_KEY);
			String result = StringsUtil.sendPost1(AgConstants.AG_URL + AgConstants.Function.BUSINESS_FUNCTION, params);

			//2.解析
			XmlUtil xml = new XmlUtil(result);
			result = xml.getAttribute("/result/@info").get(0).getValue();
			resultMap = (StringsUtil.isNumeric(result)) ? success(result) : failure(xml.getAttribute("/result/@msg").get(0).getValue());
			resultMap.put("balance", result);
		} catch (Exception e) {
			logger.info("查询余额异常");
			resultMap = maybe("query balance exception");
		}
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String login(LoginParam param) {
		String result = AgConstants.LOGIN_URL + AgConstants.Function.LOGIN_FUNCTION + "?";
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();
			AgApiUserEntity user = this.queryUserExist(username);
			int actype = SysConstants.TRY_PLAY.equals(entity.getIsDemo() + "") ? 0 : 1;
			//登录
			AGTransferVo vo = new AGTransferVo(entity.getAgent(), username, entity.getPassword(), actype, "1", entity.getWebUrl(), entity.getAgent() + System.currentTimeMillis(), user.getCurrencyType(), user.getOddtype(), param.getGameType());
			String loginParam = this.generateAgParam(vo, AgConstants.AG_MD5_KEY);
			result += loginParam;
			//			result = StringsUtil.sendPost1(AgConstants.AG_URL + AgConstants.Function.LOGIN_FUNCTION, loginParam);
			logger.info("return result = {}", result);
			return JSONUtils.map2Json(success(result));
		} catch (Exception e) {
			logger.error("登录异常 : ", e);
		}
		return JSONUtils.map2Json(failure("失败或异常"));
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public AgApiUserEntity queryUserExist(String username) {
		AgApiUserEntityExample agApiUserExample = new AgApiUserEntityExample();
		agApiUserExample.createCriteria().andUsernameEqualTo(username);
		List<AgApiUserEntity> list = agApiUserEntityMapper.selectByExample(agApiUserExample);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	@Override
	@Transactional
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String cur = StringsUtil.isNull(param.getCur()) ? SysConstants.CUR : param.getCur();
			String oddType = StringsUtil.isNull(param.getOddtype()) ? SysConstants.ODD_TYPE : param.getOddtype();
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			username = entity.getPrefix() + username;
			int actype = SysConstants.TRY_PLAY.equals(entity.getIsDemo() + "") ? 0 : 1;
			//1.用户存在?
			AgApiUserEntity user = this.queryUserExist(username);
			if (user == null) {
				//1.1 ag创建会员
				logger.info("开始创建ag会员 username = {}", username);
				resultMap = this.createMemberByAg(entity, username, entity.getPassword(), oddType, cur, actype, resultMap);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					logger.info("开始创建本地会员 username = {}", username);
					//1.2 本地创建会员
					user = this.createMemberByLocal(entity, username, entity.getPassword(), cur, oddType);
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.error("创建ag会员失败 : ", e);
			return maybe("创建ag失败");
		}
		return success("user exists!");
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String billno = param.getBillno();
			logger.info("agent = {}, billno = {}, isDemo = {}, cur = {}, entity is null ? {}", entity.getAgent(), billno, entity.getIsDemo(), entity.getCurrencyType(), entity == null);
			AGTransferVo vo = new AGTransferVo(entity.getAgent(), AgConstants.QUERY_ORDER_STATUS, entity.getAgent() + billno, entity.getIsDemo(), entity.getCurrencyType());
			result = this.generateAgParam(vo, AgConstants.AG_MD5_KEY);
			result = StringsUtil.sendPost1(AgConstants.AG_URL + AgConstants.Function.BUSINESS_FUNCTION, result);
			XmlUtil readXml = new XmlUtil(result);
			result = readXml.getAttribute("/result/@info").get(0).getValue();
			if ("0".equals(result)) {
				result = readXml.getAttribute("/result/@msg").get(0).getValue();
				return JSONUtils.map2Json(success(result));
			} else if ("2".equals(result)) {
				result = "因无效的转账金额引致的失败";
			} else if ("network_error".equals(result)) { //因网络错误,不能确定转账状态是否成功
				return JSONUtils.map2Json(maybe(result));
			}
		} catch (Exception e) {
			logger.info("查询订单状态出错 : ", e);
			return JSONUtils.map2Json(maybe("查询订单状态出错 : " + result));
		}
		return JSONUtils.map2Json(failure(result));
	}

	/**
	 * ag创建会员
	 */
	private Map<String, Object> createMemberByAg(ApiInfoEntity entity, String username, String password, String oddtype, String cur, int actype, Map<String, Object> resultMap) {
		AGTransferVo vo = new AGTransferVo(entity.getAgent(), username, AgConstants.CREATE_MEMBER, actype, password, cur, oddtype);
		String result = null;
		try {
			result = this.generateAgParam(vo, AgConstants.AG_MD5_KEY);
			result = StringsUtil.sendPost1(AgConstants.CREATE_MEMBER_URL + AgConstants.Function.BUSINESS_FUNCTION, result);
			logger.info("ag创建会员result = {}", result);
			XmlUtil xml = new XmlUtil(result);
			result = xml.getAttribute("/result/@info").get(0).getValue();
			return AgConstants.TRANS_SUCCESS.equals(result) ? success(resultMap, "success!") : failure(xml.getAttribute("/result/@msg").get(0).getValue());
		} catch (Exception e) {
			logger.error("创建ag会员失败 : ", e);
			return maybe("创建ag会员失败 ");
		}
	}

	/**
	 * 创建会员
	 */
	private AgApiUserEntity createMemberByLocal(ApiInfoEntity entity, String username, String password, String cur, String oddtype) {
		AgApiUserEntity userEntity = new AgApiUserEntity();
		userEntity.setUsername(username);
		userEntity.setPassword(password);
		userEntity.setApiInfoId(entity.getId().intValue());// 对应代理 id
		userEntity.setAgentName(entity.getAgent()); //ag 代理名
		userEntity.setCreateTime(DateUtil.getCurrentTime());//时间

		userEntity.setOddtype(oddtype);
		userEntity.setCurrencyType(cur);//货币类型

		userEntity.setUserStatus(1);
		userEntity.setSiteId(entity.getSiteId());
		userEntity.setSiteName(entity.getProjectAgent());

		//ApiInfoEntityExample e = new ApiInfoEntityExample();
		//e.createCriteria();
		int insert = agApiUserEntityMapper.insert(userEntity); //插入 AG代理
		logger.info("本地创建会员username = {}, result = {}", username, insert);
		return userEntity;
	}

	/**
	 * 生成ag参数
	 */
	private String generateAgParam(AGTransferVo transferVo, String keyDe) throws Exception {
		String params = ReflectUtil.generateParam(transferVo, AgConstants.AG_PARAM_JOIN);
		logger.info("agent = {}, ag 明文参数 = {}", transferVo.getCagent(), params);
		String agEnc = PropsUtil.getProperty(transferVo.getCagent());
		PHPDESEncrypt desEn = new PHPDESEncrypt(agEnc);//ag发送加密
		params = desEn.encrypt(params);
		String key = StringsUtil.toMD5(params + keyDe);
		return "params=" + params + "&" + "key=" + key;
	}

}
