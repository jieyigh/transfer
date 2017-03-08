package com.ds.transfer.xiaoyu.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.ReflectUtil;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.DsApiUserEntity;
import com.ds.transfer.record.service.TransferRecordService;
import com.ds.transfer.xiaoyu.util.EncUtil;
import com.ds.transfer.xiaoyu.util.PropsUtil;
import com.ds.transfer.xiaoyu.vo.LoginVo;

public class TransferServiceImpl extends CommonTransferService implements TransferService<DsApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Override
	public String transfer(TransferParam transferParam) {
		return null;
	}

	@Override
	@Deprecated
	public String queryBalance(QueryBalanceParam param) {
		return null;
	}

	@Override
	public String login(LoginParam param) {
		String result = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			//TODO: 层级关系获取
			LoginVo vo = new LoginVo(param.getUsername(), entity.getSiteId() + "", param.getLottoTray(), param.getAccType(), null, //
					(1 == entity.getIsDemo().intValue()) ? "0" : "1");
			String encrypt = generateKey(vo);
			vo.setEncrypt(encrypt);
			result = ReflectUtil.generateParam(vo);
			if (StringsUtil.isNull(result)) {
				return JSONUtils.map2Json(failure("param is null!"));
			}
			if ("MP".equals(param.getTerminal())) {
				result = PropsUtil.getProperty("loginUrlPhone") + "?" + result;
			} else {
				result = PropsUtil.getProperty("loginUrl") + "?" + result;
			}
			logger.info("login result = {}", result);
			return JSONUtils.map2Json(success(result));
		} catch (Exception e) {
			logger.error("经典彩登录错误 : ", e);
			return JSONUtils.map2Json(maybe("系统内部出错 : " + result));
		}
	}

	/**
	 * 生成分分彩加密串
	 */
	private String generateKey(LoginVo vo) {
		return EncUtil.SHA1(vo.getUser() + vo.getSiteId() + vo.getUserTree() + vo.getPan() + vo.getIsTest() + PropsUtil.getProperty("loginKey"));
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
		return null;
	}

	public static void main(String[] args) {
		System.out.println(StringsUtil.toMD5("1a"));
	}
}
