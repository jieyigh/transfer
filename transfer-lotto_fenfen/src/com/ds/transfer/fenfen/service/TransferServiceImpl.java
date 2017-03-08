package com.ds.transfer.fenfen.service;

import java.security.NoSuchAlgorithmException;
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
import com.ds.transfer.fenfen.util.HMacMD5;
import com.ds.transfer.fenfen.util.Hex;
import com.ds.transfer.fenfen.util.PropsUtil;
import com.ds.transfer.fenfen.vo.LoginVo;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.DsApiUserEntity;
import com.ds.transfer.record.service.TransferRecordService;

public class TransferServiceImpl extends CommonTransferService implements TransferService<DsApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String DEFAULT_TRAY = "A";

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
			result = "1".equals(entity.getIsDemo() + "") ? "2" : "3";
			LoginVo vo = new LoginVo(result, param.getLine(), System.currentTimeMillis() + "", param.getUsername(),//
					entity.getSiteId() + "", param.getAccType(), param.getPageSite(), StringsUtil.isNull(param.getLottoTray()) ? DEFAULT_TRAY : param.getLottoTray(), //
					param.getLottoType().equals("MP") ? "m" : "p");
			result = ReflectUtil.generateParam(vo);
			String key = this.generateKey(vo, param.getAction());
			String url = "2".equals(vo.getDcUserType()) ? PropsUtil.getProperty("apiUrl") : PropsUtil.getProperty("testApiUrl");
			logger.info("result = {}, key = {}, encrypt = {}", result, param.getAction(), key);
			result = url += "&" + result + "&dcEncrypt=" + key;
			return JSONUtils.map2Json(success(result));
		} catch (Exception e) {
			logger.error("分分彩登录错误 : ", e);
			return JSONUtils.map2Json(maybe("系统内部出错 : " + result));
		}
	}

	/**
	 * 生成分分彩加密串
	 */
	private String generateKey(LoginVo vo, String key) {
		//$dcEncryptStr = dcEncrypt("dcCustomerId={$dcCustomerId}&dcToken={$dcToken}&dcUsername={$dcUsername}&dcSiteId={$dcSiteId}", $dcCustomerSec);
		try {
			String data = "dcCustomerId=" + vo.getDcCustomerId() + "&dcToken=" + vo.getDcToken() + //
					"&dcUsername=" + vo.getDcUsername() + "&dcSiteId=" + vo.getDcSiteId();
			String enc = Hex.byte2HexStr(HMacMD5.getHmacMd5Bytes(key.getBytes(), data.getBytes()));
			return enc;
		} catch (NoSuchAlgorithmException e) {
			logger.error("加密出错 : ", e);
		}
		return null;
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

}
