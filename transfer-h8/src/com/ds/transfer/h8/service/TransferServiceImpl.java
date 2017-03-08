package com.ds.transfer.h8.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
import com.ds.transfer.h8.constants.H8Constants;
import com.ds.transfer.h8.vo.H8TransferVo;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.H8ApiUserEntity;
import com.ds.transfer.record.entity.H8ApiUserEntityExample;
import com.ds.transfer.record.entity.H8ApiUserEntityExample.Criteria;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.H8ApiUserEntityMapper;
import com.ds.transfer.record.service.TransferRecordService;

public class TransferServiceImpl extends CommonTransferService implements TransferService<H8ApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Resource(name = "moneyCenterService")
	private TransferService<?> moneyCenterService;

	@Resource
	private H8ApiUserEntityMapper h8ApiUserEntityMapper;

	@Override
	public String transfer(TransferParam transferParam) {
		//		TransferVo transfer = new TransferVo(username, password, credit, billno, type, cur, remark, fromKeyType);
		TransferRecordEntity record = new TransferRecordEntity();
		String msg = "";
		try {
			ApiInfoEntity entity = transferParam.getEntity();
			String type = transferParam.getType();
			record = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), transferParam.getUsername(), transferParam.getCredit(), //
					transferParam.getBillno(), type, H8Constants.H8, transferParam.getRemark(), record);
			logger.info("h8转账记录,  id = {}", record.getId());
			H8TransferVo vo = new H8TransferVo(entity.getSecret(), entity.getAgent(), transferParam.getUsername(),//
					IN.equals(type) ? H8Constants.function.TRANSFER_IN : H8Constants.function.TRANSFER_OUT,//
					transferParam.getBillno(), transferParam.getCredit());
			String params = ReflectUtil.generateParam(vo);
			String result = StringsUtil.sendGet(entity.getReportUrl(), params);
			XmlUtil xml = new XmlUtil(result);
			String errcode = xml.getSelectNodes("/response/errcode").get(0).getStringValue();
			msg = xml.getSelectNodes("/response/errtext").get(0).getStringValue();
			if (H8Constants.STATE_SUCCESS.equals(errcode)) {
				logger.info("h8转账成功,type = {}", type);
				record = this.transferRecordService.update(SysConstants.Record.TRANS_SUCCESS, "H8转账成功", record);
				return JSONUtils.map2Json(success("H8 转账成功"));
			}
			logger.info("h8转账失败");
			record = this.transferRecordService.update(SysConstants.Record.TRANS_FAILURE, "H8转账失败", record);
			if (IN.equals(type)) {
				logger.info("H8转入失败,退回DS主账户");
				transferParam.setType("H8转入失败,退回DS主账户");
				transferParam.setBillno(transferParam.getBillno() + "F");
				result = this.moneyCenterService.transfer(transferParam);
				Map<String, Object> resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					return JSONUtils.map2Json(failure(resultMap, "H8转入失败:" + msg + ",退回DS主账户"));
				}
			}
		} catch (Exception e) {
			logger.error("h8转账异常:", e);
			record = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "H8转账异常", record);
			return JSONUtils.map2Json(maybe("H8 转账异常"));
		}
		return JSONUtils.map2Json(failure("H8 转账失败:" + msg));
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			//查询余额
			H8TransferVo vo = new H8TransferVo(entity.getSecret(), entity.getAgent(), username, H8Constants.function.QUERY_BALANCE);
			String queryParam = ReflectUtil.generateParam(vo);
			String result = StringsUtil.sendGet(entity.getReportUrl(), queryParam);
			XmlUtil readXml = new XmlUtil(result);
			String errcode = readXml.getSelectNodes("/response/errcode").get(0).getStringValue();
			if ("0".equals(errcode)) {
				resultMap = success(readXml.getSelectNodes("/response/result").get(0).getStringValue());
				resultMap.put("balance", readXml.getSelectNodes("/response/result").get(0).getStringValue());
				return JSONUtils.map2Json(resultMap);
			}
			resultMap = failure(readXml.getSelectNodes("/response/errtext").get(0).getStringValue());
		} catch (Exception e) {
			logger.error("h8余额查询异常 : ", e);
			resultMap = maybe("h8余额查询异常");
		}
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String login(LoginParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			String username = param.getUsername();
			String params = "action=update&agent=" + entity.getAgent() + "&username=" + username + "&secret=" + entity.getSecret() + "&max1=5000&lim1=20000&lim2=20000&comtype=A&com1=0.1&com2=0.2&com3=0.3&suspend=0";
			result = StringsUtil.sendGet(entity.getReportUrl(), params);
			logger.info("login update result = {}", result);
			//试玩?
			//			if (H8Constants.TRY_PLAY_AGENT.equals(entity.getAgent())) {
			//				username = "cs" + username;
			//			}
			H8TransferVo vo = new H8TransferVo(entity.getSecret(), entity.getAgent(), username, param.getAction(),//
					entity.getH8Host(), param.getLanguage(), param.getAccType());
			result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendGet(entity.getReportUrl(), result);

			//TODO: to modify
			StringBuilder h8Url = new StringBuilder();

			Document document = DocumentHelper.parseText(result.trim());
			Element root = document.getRootElement();
			Node host = document.selectSingleNode("//host");

			List<Element> loginParam = document.selectNodes("//param");

			List<Element> elements = root.elements();
			JSONObject object = new JSONObject();
			for (Iterator<Element> it = elements.iterator(); it.hasNext();) {

				Element element = it.next();

				logger.info("element value######" + element);

				if ("errcode".equals(element.getName())) { // 0代表成功
					if ("0".equals(element.getText())) { //h8 登陆成功
						object.put("status", "10000");

						//h8 登陆成功 进行检索
						Node us = document.selectSingleNode("//us");
						Node k = document.selectSingleNode("//k");
						Node lang = document.selectSingleNode("//lang");
						Node accTypeNode = document.selectSingleNode("//accType");
						Node r = document.selectSingleNode("//r");
						logger.info("us test value:::::" + us.getText());

						//h8Url.append(host.getText()).append("?");

						/**
						 * update by jay 2016-05-04
						 * 
						 * 
						 */

						if (null != param.getLottoType() && param.getLottoType().equals("PM")) {
							String text = host.getText();

							StringBuilder sb = new StringBuilder();

							String strs[] = text.split("\\.", 2);
							if (strs.length == 2) {
								sb.append(strs[0]);
								sb.append("mobi.");
								sb.append(strs[1]);
							}

							h8Url.append(sb.toString()).append("?");

						} else {
							h8Url.append(host.getText()).append("?");
						}

						h8Url.append("us=").append(us.getText());
						h8Url.append("&k=").append(k.getText());
						h8Url.append("&lang=").append(lang.getText());
						h8Url.append("&accType=").append(accTypeNode.getText());
						h8Url.append("&r=").append(r.getText());

						logger.info("#########h8 登陆路径" + h8Url);

						return JSONUtils.map2Json(success(h8Url.toString()));

					} else {
						//<?xml version="1.0" encoding="UTF-8"?><response><errcode>1</errcode>
						//<errtext>Username exist</errtext><result></result></response>
						object.put("status", "10050");
						object.put("message", result);
						return object.toString();
					}
				}

				logger.info("H8 url 登陆路径显示:::" + h8Url);
			}
		} catch (Exception e) {
			logger.error("login error : ", e);
			return JSONUtils.map2Json(maybe("系统内部异常 : " + result));
		}
		return JSONUtils.map2Json(failure(result));
	}

	private void updateComtype(String agent, String secret, String m8Url, String username) {

	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public H8ApiUserEntity queryUserExist(String username) {
		logger.info("h8 username = {}", username);
		String user = username.split("&")[0];
		Integer siteId = Integer.valueOf(username.split("&")[1]);
		H8ApiUserEntityExample h8ApiUserExample = new H8ApiUserEntityExample();
		Criteria createCriteria = h8ApiUserExample.createCriteria();
		createCriteria.andUsernameEqualTo(user);
		createCriteria.andSiteIdEqualTo(siteId);
		List<H8ApiUserEntity> list = h8ApiUserEntityMapper.selectByExample(h8ApiUserExample);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	@Transactional
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		ApiInfoEntity entity = param.getEntity();
		String username = param.getUsername();
		Map<String, Object> resultMap = null;
		try {
			String cur = StringsUtil.isNull(param.getCur()) ? SysConstants.CUR : param.getCur();
			String oddType = StringsUtil.isNull(param.getOddtype()) ? SysConstants.CUR : param.getOddtype();
			H8ApiUserEntity user = this.queryUserExist(username + "&" + entity.getSiteId());
			if (user == null) {
				//1.创建h8会员
				resultMap = this.createMemberByH8(entity, username);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					logger.info("创建h8会员");
					user = this.createMemberByLocal(entity, username, entity.getPassword(), cur, oddType);
				}
				return resultMap;
			}
		} catch (Exception e) {
			logger.info("创建会员异常 : ", e);
			return maybe("创建h8会员异常");
		}
		return success("用户已经存在");
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			H8TransferVo vo = new H8TransferVo(entity.getSecret(), entity.getAgent(), param.getUsername(), H8Constants.function.QUERY_ORDER_STATUS, param.getBillno());
			result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendGet(entity.getReportUrl(), result);
			//<?xml version="1.0" encoding="UTF-8"?><response><errcode>0</errcode><errtext></errtext><result><payment><username>3r88etest3</username><amount>-1</amount></payment></result></response>
			XmlUtil xmlUtil = new XmlUtil(result);
			String status = xmlUtil.getSelectNodes("/response/errcode").get(0).getStringValue();
			if ("0".equals(status)) { //成功
				result = JSONUtils.map2Json(success("success!"));
			} else {
				result = JSONUtils.map2Json(failure("failure!"));
			}
		} catch (Exception e) {
			logger.error("h8查询订单出错 : ", e);
			result = JSONUtils.map2Json(maybe("查询订单出错 : " + result));
		}
		return result;
	}

	/**
	 * H8创建会员
	 */
	private Map<String, Object> createMemberByH8(ApiInfoEntity entity, String username) {
		String result = null;
		int max = H8Constants.MAX;
		int lim = H8Constants.LIM;
		if (H8Constants.TRY_PLAY_AGENT.equals(entity.getAgent())) {
			max = H8Constants.MAX_TEST;
			lim = H8Constants.LIM_TEST;
		}
		try {
			H8TransferVo vo = new H8TransferVo(entity.getSecret(), entity.getAgent(), username, H8Constants.function.CREATE_MEMBER);
			result = ReflectUtil.generateParam(vo);
			result = StringsUtil.sendGet(entity.getReportUrl(), result);
		} catch (Exception e) {
			logger.error("h8创建会员异常 : ", e);
			return maybe("h8创建会员异常1");
		}
		XmlUtil xml = new XmlUtil(result);
		result = xml.getSelectNodes("/response/errcode").get(0).getStringValue();
		if (!H8Constants.STATE_SUCCESS.equals(result)) {
			result = xml.getSelectNodes("/response/errtext").get(0).getStringValue();
			if ("Username exist".equalsIgnoreCase(result)) {
				return success(result);
			}
			return failure(result);
		}
		H8TransferVo updateVO = new H8TransferVo(entity.getSecret(), entity.getAgent(), username, H8Constants.function.UPDATE, max + "", lim + "", //
				H8Constants.COMTYPE_A, H8Constants.COM1 + "",//
				H8Constants.COM2 + "", H8Constants.COM3 + "", H8Constants.SUSPEND + "");
		try {
			result = ReflectUtil.generateParam(updateVO);
			result = StringsUtil.sendGet(entity.getReportUrl(), result);
			logger.info("H8 修改会员下注限额 {}", result);
		} catch (Exception e) {
			logger.error("h8创建会员更新限红异常 : ", e);
			return maybe("h8创建会员更新限红异常2");
		}
		return success("成功");
	}

	/**
	 * 本地创建会员
	 */
	private H8ApiUserEntity createMemberByLocal(ApiInfoEntity entity, String username, String password, String cur, String oddtype) {
		H8ApiUserEntity h8InsertUser = new H8ApiUserEntity();
		h8InsertUser.setUsername(username);
		h8InsertUser.setPassword(password);
		h8InsertUser.setAgentName(entity.getAgent());
		h8InsertUser.setApiInfoId(entity.getId().intValue());//api 的 id
		h8InsertUser.setSiteName(entity.getProjectAgent());
		h8InsertUser.setOddtype(oddtype);
		h8InsertUser.setCurrencyType(cur);

		h8InsertUser.setCreateTime(DateUtil.getCurrentTime());//创建时间
		h8InsertUser.setUserStatus(1);//状态=1 有效会员
		h8InsertUser.setSiteId(entity.getSiteId());
		h8InsertUser.setSiteName(entity.getProjectAgent());

		this.h8ApiUserEntityMapper.insert(h8InsertUser);//插入 h8 会员资料
		return h8InsertUser;
	}

}
