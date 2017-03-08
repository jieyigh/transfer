package com.ds.transfer.og.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.ds.transfer.og.constants.OgConstants;
import com.ds.transfer.og.properties.LoadProps;
import com.ds.transfer.og.util.DigitalUtil;
import com.ds.transfer.og.util.EncUtil;
import com.ds.transfer.og.vo.OgTransferVo;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.OgApiUserEntity;
import com.ds.transfer.record.entity.OgApiUserEntityExample;
import com.ds.transfer.record.entity.OgApiUserEntityExample.Criteria;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.OgApiUserEntityMapper;
import com.ds.transfer.record.service.TransferRecordService;

@Service
public class TransferServiceImpl extends CommonTransferService implements TransferService<OgApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "moneyCenterService")
	private TransferService<?> moneyCenterService;

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Resource
	private OgApiUserEntityMapper ogApiUserEntityMapper;

	@Override
	public String transfer(TransferParam transferParam) {
		TransferRecordEntity ogRecord = new TransferRecordEntity();
		String typeDes = null;
		String result = null;
		try {
			ApiInfoEntity entity = transferParam.getEntity();
			String username = entity.getPrefix() + transferParam.getUsername();
			String type = transferParam.getType();
			typeDes = IN.equals(type) ? "入" : "出";

			//1.插入记录
			ogRecord = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), username, transferParam.getCredit(), transferParam.getBillno(),//
					type, entity.getLiveName(), transferParam.getRemark(), ogRecord);
			logger.info("OG视讯 转账记录插入成功, id = {}", ogRecord.getId());

			//2.转账
			OgTransferVo vo = new OgTransferVo(entity.getAgent(), username, entity.getPassword(), entity.getAgent() + transferParam.getBillno(),//
					type, transferParam.getCredit(), OgConstants.TransMethods.PREPARED_TRANS);
			String voParam = ReflectUtil.generateParam(vo, "$");
			logger.info("transfer vo param = {}", voParam);
			String params = EncUtil.encodeStrBase64(voParam);
			String key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
			result = StringsUtil.sendGet(LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key));
			result = new XmlUtil(result).getSelectNodes("/result").get(0).getStringValue();
			if ("1".equals(result)) {
				vo = new OgTransferVo(entity.getAgent(), username, entity.getPassword(), entity.getAgent() + transferParam.getBillno(),//
						type, transferParam.getCredit(), OgConstants.TransMethods.CONFIRM_TRANS);
				voParam = ReflectUtil.generateParam(vo, "$");
				logger.info("confirm transfer vo param = {}", voParam);
				params = EncUtil.encodeStrBase64(voParam);
				key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
				result = StringsUtil.sendGet(LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key));
				result = new XmlUtil(result).getSelectNodes("/result").get(0).getStringValue();
				if ("1".equals(result)) {
					ogRecord = this.transferRecordService.update(SysConstants.Record.TRANS_SUCCESS, "OG转" + typeDes + "成功", ogRecord);
					return JSONUtils.map2Json(success("OG 转" + typeDes + "成功!"));
				}
			}
			ogRecord = this.transferRecordService.update(SysConstants.Record.TRANS_FAILURE, "OG转" + typeDes + "失败", ogRecord);
			if (IN.equals(type)) {
				logger.info("OG转入失败,退回DS主账户");
				transferParam.setRemark("OG转入失败,退回DS主账户");
				transferParam.setBillno(transferParam.getBillno() + "F");
				result = this.moneyCenterService.transfer(transferParam);
				Map<String, Object> resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					return JSONUtils.map2Json(failure(resultMap, "OG转入失败:" + result + ",退回DS主账户"));
				}
			}
			return JSONUtils.map2Json(failure("OG 转" + typeDes + "失败 : " + result));
		} catch (Exception e) {
			logger.error("OG 转" + typeDes + "异常 : ", e);
			ogRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "OG转" + typeDes + "异常", ogRecord);
			return JSONUtils.map2Json(maybe("OG 转" + typeDes + "异常"));
		}
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();

			//1.查询余额
			OgTransferVo vo = new OgTransferVo(entity.getAgent(), username, entity.getPassword(), OgConstants.TransMethods.QUERY_BALANCE);
			String params = EncUtil.encodeStrBase64(ReflectUtil.generateParam(vo, "$"));
			String key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
			String result = StringsUtil.sendGet(LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key));
			//2.解析
			result = new XmlUtil(result).getSelectNodes("/result").get(0).getStringValue();
			if ("10".equals(result)) {
				return JSONUtils.map2Json(failure("The agent not exist"));
			}
			if (DigitalUtil.isMoney(result)) {
				resultMap = success("success!");
				resultMap.put("balance", result);
			} else {
				resultMap = failure(result);
			}
		} catch (Exception e) {
			logger.info("查询余额异常");
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
			OgTransferVo vo = new OgTransferVo(entity.getAgent(), username, entity.getPassword(), entity.getWebUrl(), param.getGameType(), param.getGamekind(),//
					LoadProps.getProperty("og_plat"), param.getLanguage(), OgConstants.TransMethods.LOGIN);
			String params = EncUtil.encodeStrBase64(ReflectUtil.generateParam(vo, "$"));
			String key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
			result = LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key);
			logger.info("result = {}", result);
		} catch (Exception e) {
			logger.error("登录异常 : ", e);
			return JSONUtils.map2Json(maybe("登录异常"));
		}
		return JSONUtils.map2Json(success(result));
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		this.login(param);
		return null;
	}

	@Override
	public OgApiUserEntity queryUserExist(String username) {
		OgApiUserEntityExample example = new OgApiUserEntityExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		List<OgApiUserEntity> list = this.ogApiUserEntityMapper.selectByExample(example);
		return (list == null || list.size() <= 0) ? null : list.get(0);
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = entity.getPrefix() + param.getUsername();
			OgApiUserEntity user = this.queryUserExist(username);
			if (user == null) {
				OgTransferVo vo = new OgTransferVo(entity.getAgent(), username, param.getCur(), entity.getPassword(),//
						param.getOddtype(), param.getOddtype(), param.getOddtype(), OgConstants.TransMethods.CHECK_AND_CREATE_ACCOUNT);
				String params = EncUtil.encodeStrBase64(ReflectUtil.generateParam(vo, "$"));
				String key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
				String result = StringsUtil.sendGet(LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key));
				result = new XmlUtil(result).getSelectNodes("/result").get(0).getStringValue();
				if ("1".equals(result)) {//success
					logger.info("创建og会员成功!");
					user = this.createMemberByLocal(param, entity, username);
					logger.info("创建本地会员 = {} 成功!", username);
					return success("成功!");
				} else if ("0".equals(result)) {
					return success("Maybe username = " + username + ", already exist!");
				} else if ("2".equals(result)) {
					return failure("the password no right!");
				} else if ("3".equals(result)) {
					return failure("Username is too long!");
				} else if ("10".equals(result)) {
					return failure("The agent not exist");
				} else {
					return failure("OG return : " + result);
				}
			}
			return success("用户已经存在!");
		} catch (Exception e) {
			logger.error("系统内部出错 : ", e);
			return maybe("系统内部错误");
		}
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			OgTransferVo vo = new OgTransferVo(entity.getAgent(), param.getUsername(), param.getPassword(), //
					entity.getAgent() + param.getBillno(),//
					param.getType(), param.getCredit(), OgConstants.TransMethods.CONFIRM_TRANS);
			String voParam = ReflectUtil.generateParam(vo, "$");
			String params = EncUtil.encodeStrBase64(voParam);
			String key = EncUtil.toMD5(params + LoadProps.getProperty("og_key"));
			result = StringsUtil.sendGet(LoadProps.getProperty("og_url").replaceAll(":params", params).replaceAll(":key", key));
			result = new XmlUtil(result).getSelectNodes("/result").get(0).getStringValue();
			if ("1".equals(result)) {
				return JSONUtils.map2Json(success("success!"));
			} else if ("Network_error".equals(result)) {
				return JSONUtils.map2Json(maybe("网络延迟,请重新再试!"));
			}
		} catch (Exception e) {
			logger.error("OG查询订单出错 : ", e);
			return JSONUtils.map2Json(maybe("系统内部出错"));
		}
		return JSONUtils.map2Json(failure("result = " + result));
	}

	private OgApiUserEntity createMemberByLocal(UserParam param, ApiInfoEntity entity, String username) {
		OgApiUserEntity user = new OgApiUserEntity();
		user = new OgApiUserEntity();
		user.setAgentName(entity.getAgent());
		user.setApiInfoId(entity.getId().intValue());
		user.setCreateTime(DateUtil.getCurrentTime());
		user.setCurrencyType("RMB");
		user.setOddtype(SysConstants.ODD_TYPE);
		user.setPassword(entity.getPassword());
		user.setSiteId(entity.getSiteId());
		user.setSiteName(entity.getProjectAgent());
		user.setUsername(username);
		user.setUserStatus(1);
		this.ogApiUserEntityMapper.insert(user);
		return user;
	}

}
