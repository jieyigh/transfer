package com.ds.transfer.http.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.http.constants.TransferConstants;
import com.ds.transfer.http.entity.TransferRecordDetailEntity;
import com.ds.transfer.http.entity.TransferStatus;
import com.ds.transfer.http.service.AgTransferService;
import com.ds.transfer.http.service.ApiInfoService;
import com.ds.transfer.http.service.BbinTransferService;
import com.ds.transfer.http.service.DsTransferService;
import com.ds.transfer.http.service.H8TransferService;
import com.ds.transfer.http.service.OgTransferService;
import com.ds.transfer.http.service.TransferRecordDetailService;
import com.ds.transfer.record.entity.AgApiUserEntity;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.BbinApiUserEntity;
import com.ds.transfer.record.entity.DsApiUserEntity;
import com.ds.transfer.record.entity.H8ApiUserEntity;
import com.ds.transfer.record.entity.OgApiUserEntity;

/**
 * http转账接口
 * 
 * @author jackson
 *
 */
@RestController
public class TransferController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(TransferController.class);

//	@Resource(name = "agTransferServiceImpl")
//	private AgTransferService<AgApiUserEntity> agTransferService;
//
//	@Resource(name = "bbinTransferServiceImpl")
//	private BbinTransferService<BbinApiUserEntity> bbinTransferService;
//
//	@Resource(name = "h8TransferServiceImpl")
//	private H8TransferService<H8ApiUserEntity> h8TransferService;
//
//	//	@Resource(name = "ogTransferServiceImpl")
//	//	private OgTransferService<OgApiUserEntity> ogTransferService;
//
//	@Resource(name = "dsTransferServiceImpl")
//	private DsTransferService<DsApiUserEntity> dsTransferService;
//
//	@Resource(name = "transferRecordDetailServiceImpl")
//	private TransferRecordDetailService transferRecordDetailService;
//
//	@Resource
//	private ApiInfoService apiInfoService;
//
//	/**
//	 * 额度转换新接口
//	 * 
//	 * @param username  用户名
//	 * @param password 	密码
//	 * @param key		key
//	 * @param siteId	网站id
//	 * @param live		视讯类别		存放AG=2  BBIN=11  DS=12 H8=13
//	 * @param oddtype 	限红			在查询余额用户不存在,创建会员的时候用到
//	 * @param billno	 唯一转账编码
//	 * @param type		 转账类型
//	 * @param credit 	金额
//	 * @param isDemo 	是否是试玩 		试玩:0,正式:1(默认,不传就是正式)  
//	 * @param cur 		货币类型
//	 * @param transMethod  转账类型		ag | bbin | h8 | agBbin | agH8 | bbinH8 | balanceTotal;优先级ag,bbin,h8
//	 * @param request
//	 * @return
//	 */
//	//	@RequestMapping(value = "transfer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody String transfer(String username, String password, String key, String siteId, String live, String oddtype,//
//			String billno, String type, String credit, String isDemo, String cur, String transMethod, HttpServletRequest request, HttpServletResponse response) {
//		logger.info("转账 : username = " + username + ", password = " + password + ", key = " + key + ", siteId = " + siteId + ", live = " + live + ", oddtype = " + oddtype + //
//				", billno = " + billno + ", type = " + type + ", credit = " + credit + ", isDemo = " + isDemo + ", cur = " + cur + ", transMethod = " + transMethod);
//		String ip = StringsUtil.getIpAddr(request);// 获取请求的 ip
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//
//		isDemo = StringsUtil.isNull(isDemo) ? "1" : isDemo;
//		oddtype = StringsUtil.isNull(oddtype) ? "A" : oddtype;
//		cur = StringsUtil.isNull(cur) ? "CNY" : cur;
//		if (StringsUtil.isNull(billno)) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "billno is null");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (StringsUtil.isNull(key)) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "key is null");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (StringsUtil.isNull(live)) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "live is null");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (credit == null) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "credit is null");
//			return JSONUtils.map2Json(resultMap);
//		}
//		// 设置 转账 限额在 二十万以下
//		if (Integer.valueOf(credit) > TransferConstants.BIG_MONEY) {
//			resultMap.put(STATUS, "100100");
//			resultMap.put(MESSAGE, "It exceeds the maximum transfer limit");
//			logger.info("{}超过最大限额{}", credit, TransferConstants.BIG_MONEY);
//			return JSONUtils.map2Json(resultMap);
//		}
//
//		if (Integer.valueOf(credit) <= 0) {
//			resultMap.put(STATUS, "100100");
//			resultMap.put(MESSAGE, "money is error, must > 0");
//			return JSONUtils.map2Json(resultMap);
//		}
//
//		if (StringsUtil.isNull(siteId) || !StringsUtil.isNumeric(siteId)) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "siteId is error");
//			return JSONUtils.map2Json(resultMap);
//		}
//
//		String siteIdLiveIdMapKey = siteId + "" + isDemo + "" + live;
//		Map<String, ApiInfoEntity> siteIdLiveIdMap = apiInfoService.getSiteIdLiveIdMap();
//		ApiInfoEntity entity = siteIdLiveIdMap.get(siteIdLiveIdMapKey);
//		if (entity == null) {
//			resultMap.put(STATUS, "100002");
//			resultMap.put(MESSAGE, "siteId is not found in api");
//			return JSONUtils.map2Json(resultMap);
//		}
//
//		// ------------------------------------------------------------------------------------------------
//		if (entity.getIp().indexOf(ip) < 0) { // 不包含 ip
//			logger.info(ip + ":获取余额加入 ip白名单进入 找不到代理里面");
//			resultMap.put(STATUS, "100003");// 用户名
//			resultMap.put(MESSAGE, "ip not whitelist");// 密码
//			return JSONUtils.map2Json(resultMap);
//		}
//		/***********************************/
//		logger.info("转账接口#######参数::::username:" + username + "#password#" + password + "#keyB#" + entity.getKeyb() + "#时间#" + StringsUtil.updateTime());
//		if (!validKeyByTransfer(username, password, key, entity)) {// md5验证
//			logger.info("md5 is error");
//			resultMap.put(STATUS, "100002");// 用户名
//			resultMap.put(MESSAGE, "md5 is error");// 密码
//			return JSONUtils.map2Json(resultMap);
//		}
//		logger.info("------------------->>进入 转账 接口<<-------------------");
//		ApiInfoEntity bbinEntity = siteIdLiveIdMap.get(siteId + isDemo + SysConstants.LiveId.BBIN);
//		ApiInfoEntity h8Entity = siteIdLiveIdMap.get(siteId + isDemo + SysConstants.LiveId.H8);
//		TransferParam param = new TransferParam(entity, username, password, credit, billno, type, cur, null, Integer.valueOf(live), false);
//		try {
//			TransferRecordDetailEntity record = new TransferRecordDetailEntity();
//			record.setRemark(transMethod);
//			if (this.transferRecordDetailService.insert(record, param) <= 0) {
//				return JSONUtils.map2Json(failure("转账记录插入失败"));
//			}
//			if (TransferConstants.TransferMethod.AG.equals(transMethod)) {//DS主账户 <==> ag 互转
//				resultMap = JSONUtils.json2Map(this.agTransferService.transfer(param));
//			} else if (TransferConstants.TransferMethod.BBIN.equals(transMethod)) {//DS主账户 <==> bbin 互转
//				resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(param));
//			} else if (TransferConstants.TransferMethod.H8.equals(transMethod)) {//DS主账户 <==> h8 互转
//				resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(param));
//			} else if (TransferConstants.TransferMethod.OG.equals(transMethod)) {//DS主账户 <==> OG 互转
//			//				resultMap = this.ogTransferService.transfer(param, false);
//			} else if (TransferConstants.TransferMethod.AG_BBIN.equals(transMethod)) {//ag <==> bbin 互转
//				if (IN.equals(type)) {//1.ag --- 2.bbin +++
//					param.setType(OUT);
//					resultMap = JSONUtils.json2Map(this.agTransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(bbinEntity);
//						param.setType(type);
//						resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(param));
//					}
//				} else {//1.bbin --- 2.ag +++
//					param.setEntity(bbinEntity);
//					resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(entity);
//						param.setType(IN);
//						resultMap = JSONUtils.json2Map(this.agTransferService.transfer(param));
//					}
//				}
//			} else if (TransferConstants.TransferMethod.AG_H8.equals(transMethod)) {//ag <==> h8 互转
//				if (IN.equals(type)) {//1.ag --- 2.h8 +++
//					param.setType(OUT);
//					resultMap = JSONUtils.json2Map(this.agTransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(h8Entity);
//						param.setType(type);
//						resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(param));
//					}
//				} else {//1.h8 --- 2.ag +++
//					param.setEntity(h8Entity);
//					resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(entity);
//						param.setType(IN);
//						resultMap = JSONUtils.json2Map(this.agTransferService.transfer(param));
//					}
//				}
//			} else if (TransferConstants.TransferMethod.BBIN_H8.equals(transMethod)) {//bbin <==> h8 互转
//				if (IN.equals(type)) {//1.bbin --- 2.h8 +++
//					param.setType(OUT);
//					resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(h8Entity);
//						param.setType(type);
//						resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(param));
//					}
//				} else {//1.h8 --- 2.bbin +++
//					param.setEntity(h8Entity);
//					resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(param));
//					if (SUCCESS.equals(resultMap.get(STATUS))) {
//						record.setVersion(record.getVersion() + 1);
//						param.setEntity(bbinEntity);
//						param.setType(IN);
//						resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(param));
//					}
//				}
//			} else if (TransferConstants.TransferMethod.BALANCE_TOTAL.equals(transMethod)) { // 资金归集
//				String bbinSiteIdKey = siteId + isDemo + SysConstants.LiveId.BBIN;
//				String h8SiteIdKey = siteId + isDemo + SysConstants.LiveId.H8;
//				logger.info("agKey = {}, bbinKey = {}, h8Key = {}", siteIdLiveIdMapKey, bbinSiteIdKey, h8SiteIdKey);
//				int count = this.balanceTotalTransfer(username, password, billno, credit, cur, oddtype, bbinSiteIdKey, h8SiteIdKey, siteIdLiveIdMap, entity, resultMap);
//				if (count == 3) {
//					logger.info("资金归集成功");
//					return JSONUtils.map2Json(success("资金归集成功"));
//				}
//				return JSONUtils.map2Json(failure("资金归集失败"));
//			} else {
//				logger.info("live = {},没有对用到处理方式...", live);
//				resultMap.put(STATUS, "100002");
//				resultMap.put(MESSAGE, "live is error,no process way");
//			}
//
//			//更新状态
//			if (SUCCESS.equals(resultMap.get(STATUS))) {
//				this.transferRecordDetailService.update(record, TransferStatus.SUCCESS.value());
//			} else if (ERROR.equals(resultMap.get(STATUS))) {
//				this.transferRecordDetailService.update(record, TransferStatus.FAILURE.value());
//			} else { //其他状态码当作异常处理
//				this.transferRecordDetailService.update(record, TransferStatus.MAYBE.value());
//			}
//		} catch (Exception e) {
//			logger.error("转账出错 : ", e);
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "System inner errors,getBalance Member errors");
//		}
//		logger.info("resultMap end = {}   ,   {}", resultMap, JSONUtils.map2Json(resultMap));
//		return JSONUtils.map2Json(resultMap);
//	}
//
//	/**
//	 * 
//	 * @param username
//	 * @param password
//	 * @param siteId
//	 * @param isDemo	
//	 * @param live		存放AG=2  BBIN=11  DS=12 H8=13
//	 * @return
//	 */
//	//	@RequestMapping(value = "queryBalance", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//	public @ResponseBody String queryBalance(String username, String password, String siteId, String isDemo, String live, String cur, String key) {
//		logger.info("查询余额 : username = {}, password = {}, siteId = {}, isDemo = {}, live = {},cur = {}, key = {}",//
//				username, password, siteId, isDemo, live, cur, key);
//
//		isDemo = StringsUtil.isNull(isDemo) ? "1" : isDemo;
//		cur = StringsUtil.isNull(cur) ? "CNY" : cur;
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		String result = null;
//		resultMap.put("username", username);
//		resultMap.put("password", password);
//		resultMap.put("live", live);
//		resultMap.put("siteId", siteId);
//		resultMap.put("key", key);
//
//		resultMap = StringsUtil.isNull(resultMap);
//		if (!SUCCESS.equals(resultMap.get(STATUS))) {
//			return JSONUtils.map2Json(resultMap);
//		}
//		String siteIdKey = siteId + isDemo + live;
//		Map<String, ApiInfoEntity> siteIdLiveIdMap = apiInfoService.getSiteIdLiveIdMap();
//		ApiInfoEntity entity = siteIdLiveIdMap.get(siteIdKey);
//		if (entity == null) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "siteId or live is not found in api");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (!this.valiKeyByQueryBalance(username, password, key, entity)) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "md5 is error");
//			return JSONUtils.map2Json(resultMap);
//		}
//		QueryBalanceParam param = new QueryBalanceParam(entity, username, password, cur);
//		if (SysConstants.LiveId.DS.equals(live)) {
//			//TODO:待完善
//		} else if (SysConstants.LiveId.AG.equals(live)) {
//			result = this.agTransferService.queryBalance(param);
//		} else if (SysConstants.LiveId.BBIN.equals(live)) {
//			result = this.bbinTransferService.queryBalance(param);
//		} else if (SysConstants.LiveId.H8.equals(live)) {
//			result = this.h8TransferService.queryBalance(param);
//		} else {
//			logger.info("live={} isn't process way", live);
//			result = JSONUtils.map2Json(failure("live isn't process way"));
//		}
//		return result;
//	}
//
//	//	@RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//	public @ResponseBody String login(String username, String password, String siteId, String live, String billno, String gameType, String cur, String lang, String isDemo, String key, HttpServletRequest request, HttpServletResponse response) {
//		logger.info("login : username = {}, password = {}, siteId = {}, live = {}, billno = {}, gameType = {}, cur = {}, lang = {}, isDemo = {}, key = {} ",//
//				username, password, siteId, live, billno, gameType, cur, lang, isDemo, key);
//		String ip = StringsUtil.getIpAddr(request);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("username", username);
//		resultMap.put("password", password);
//		resultMap.put("siteId", siteId);
//		resultMap.put("live", live);
//		resultMap.put("billno", billno);
//		resultMap.put("key", key);
//		resultMap = StringsUtil.isNull(resultMap);
//		if (!SUCCESS.equals(resultMap.get(STATUS))) {
//			return JSONUtils.map2Json(resultMap);
//		}
//		resultMap.clear();
//
//		isDemo = StringsUtil.isNull(isDemo) ? "1" : isDemo;
//		cur = StringsUtil.isNull(cur) ? SysConstants.CUR : cur;
//		String siteIdKey = siteId + isDemo + live;
//		Map<String, ApiInfoEntity> siteIdLiveIdMap = this.apiInfoService.getSiteIdLiveIdMap();
//		ApiInfoEntity entity = siteIdLiveIdMap.get(siteIdKey);
//		if (entity == null) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "siteId or live is not found in api");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (!this.validKeyByLogin(username, password, key, entity)) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "md5 is error");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (entity.getIp().indexOf(ip) == -1) {
//			resultMap.put(STATUS, "100003");
//			resultMap.put(MESSAGE, "ip is not in whiteList");
//			return JSONUtils.map2Json(resultMap);
//		}
//		try {
//			if (SysConstants.LiveId.AG.equals(live)) {
//				gameType = StringsUtil.isNull(gameType) ? "0" : gameType;
//				logger.info("ag login : gameType = {}", gameType);
//				LoginParam loginParam = new LoginParam(entity, username, password);
//				loginParam.setGameType(gameType);
//				return this.agTransferService.login(loginParam);
//			} else if (SysConstants.LiveId.BBIN.equals(live)) {
//				String pageSite = request.getParameter("pageSite");//BB体育:ball 3D厅:3DHall 视讯:live 视讯:live 视讯:live 机率:game 若为空白则导入整合页
//				String gamekind = request.getParameter("gameKind");
//				String gamecode = request.getParameter("gameCode");
//
//				lang = StringsUtil.isNull(lang) ? SysConstants.LANGUAGE_Chinese : lang;
//				pageSite = StringsUtil.isNull(pageSite) ? "live" : pageSite;
//				logger.info("bbin login : lang = {}, page_site = {}, gameType = {}, gamekind = {}, gamecode = {}", lang, pageSite, gameType, gamekind, gamecode);
//				if (StringsUtil.isNull(gameType)) {//大厅登录
//					LoginParam loginParam = new LoginParam(entity, username, password);
//					loginParam.setLanguage(lang);
//					loginParam.setPageSite(pageSite);
//					return this.bbinTransferService.login(loginParam);
//				} else {//游戏登录
//					if (!StringsUtil.isNull(gamekind) && !StringsUtil.isNumeric(gamekind)) {
//						return JSONUtils.map2Json(failure("gamekind is not Numberic"));
//					}
//					LoginParam loginParam = new LoginParam(entity, username, password);
//					loginParam.setLanguage(lang);
//					loginParam.setGamekind(gamekind);
//					loginParam.setGameType(gameType);
//					loginParam.setGamecode(gamecode);
//					return this.bbinTransferService.loginBySingGame(loginParam);//登录
//				}
//			} else if (SysConstants.LiveId.DS.equals(live)) {//gameType:lotto|lottery
//				//是香港彩还是彩票
//				String lottoTray = request.getParameter("lottoTray");//盘口
//				String lottoType = request.getParameter("lottoType");//PC|PM
//
//				String line = request.getParameter("line");//线路(1, 2, …)  默认线路为1,
//				line = StringsUtil.isNull(line) ? "1" : line;
//				lang = StringsUtil.isNull(lang) ? "CN" : lang;
//				if (StringsUtil.isNull(gameType)) {//LOTTERY时时彩|LOTTO香港彩|DS大厅登录
//					resultMap.put(STATUS, "100099");
//					resultMap.put(MESSAGE, "gameType is null");
//					return JSONUtils.map2Json(resultMap);
//				}
//				LoginParam param = new LoginParam(entity, username, password);
//				param.setGameType(gameType);
//				param.setCur(cur);
//				param.setLanguage(lang);
//				param.setLine(line);
//				param.setLottoTray(lottoTray);
//				param.setLottoType(lottoType);
//				this.dsTransferService.login(param);
//			} else if (SysConstants.LiveId.H8.equals(live)) { //h8 不能登陆
//				String action = request.getParameter("action");
//				String accType = request.getParameter("accType");
//
//				action = StringsUtil.isNull(action) ? TransferConstants.H8_LOGIN_ACTION : action;
//				accType = StringsUtil.isNull(accType) ? TransferConstants.H8_LOGIN_ACC_TYPE : accType;
//				lang = StringsUtil.isNull(lang) ? SysConstants.LANGUAGE_Chinese : lang;
//				logger.info("h8 login : action = {}, accType = {}, lang = {}", action, accType, lang);
//
//				LoginParam loginParam = new LoginParam(entity, username, password);
//				loginParam.setAction(action);
//				loginParam.setAccType(accType);
//				loginParam.setLanguage(lang);
//				return this.h8TransferService.login(loginParam);
//			} else {
//				resultMap.put(STATUS, "100099");
//				resultMap.put(MESSAGE, "live isn't process way");
//			}
//		} catch (Exception e) {
//			resultMap.put(STATUS, "100020");
//			resultMap.put(MESSAGE, "system error");
//		}
//		return JSONUtils.map2Json(resultMap);
//	}
//
//	/**
//	 * 查询订单状态
//	 * @param username
//	 * @param password
//	 * @param siteId
//	 * @param live
//	 * @param billno
//	 * @param key
//	 * @return
//	 */
//	public String queryStatusByBillno(String username, String password, String siteId, String live, String billno, String key, String isDemo, HttpServletRequest request) {
//		logger.info("username = {}, password = {}, siteId = {}, live = {}, billno = {}, key = {}", //
//				username, password, siteId, live, billno, key);
//		String ip = StringsUtil.getIpAddr(request);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("username", username);
//		resultMap.put("password", password);
//		resultMap.put("siteId", siteId);
//		resultMap.put("live", live);
//		resultMap.put("billno", billno);
//		resultMap.put("key", key);
//		resultMap = StringsUtil.isNull(resultMap);
//		if (!SUCCESS.equals(resultMap.get(STATUS))) {
//			return JSONUtils.map2Json(resultMap);
//		}
//		resultMap.clear();
//
//		isDemo = StringsUtil.isNull(isDemo) ? "1" : isDemo;
//		String siteIdKey = siteId + isDemo + live;
//		Map<String, ApiInfoEntity> siteIdLiveIdMap = this.apiInfoService.getSiteIdLiveIdMap();
//		ApiInfoEntity entity = siteIdLiveIdMap.get(siteIdKey);
//		if (entity == null) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "siteId or live is not found in api");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (!this.validKeyByLogin(username, password, key, entity)) {
//			resultMap.put(STATUS, "100001");
//			resultMap.put(MESSAGE, "md5 is error");
//			return JSONUtils.map2Json(resultMap);
//		}
//		if (entity.getIp().indexOf(ip) == -1) {
//			resultMap.put(STATUS, "100003");
//			resultMap.put(MESSAGE, "ip is not in whiteList");
//			return JSONUtils.map2Json(resultMap);
//		}
//
//		try {
//			TransferRecordDetailEntity record = new TransferRecordDetailEntity();
//			record.setUsername(username);
//			record.setPassword(password);
//			record.setSiteId(siteIdKey);
//			record.setLiveId(live);
//			record.setBillno(billno);
//			record = this.transferRecordDetailService.query(record);
//			resultMap.put(STATUS, SUCCESS);
//			resultMap.put(MESSAGE, record.getStatus());
//		} catch (Exception e) {
//			logger.error("查询订单状态异常 : ", e);
//			resultMap = failure("查询异常 : ");
//		}
//		return JSONUtils.map2Json(resultMap);
//	}
//
//	//资金归集
//	private int balanceTotalTransfer(String username, String password, String billno, String credit, String cur, String oddtype, String bbinSiteIdKey, String h8SiteIdKey, Map<String, ApiInfoEntity> siteIdLiveIdMap, ApiInfoEntity entity, Map<String, Object> resultMap) {
//		ApiInfoEntity bbinEntity = siteIdLiveIdMap.get(bbinSiteIdKey);
//		ApiInfoEntity h8Entity = siteIdLiveIdMap.get(h8SiteIdKey);
//		int countBalanceCollection = 0;
//		String type = OUT;
//		TransferParam transferParam = null;
//		//ag
//		QueryBalanceParam balanceParam = new QueryBalanceParam(entity, username, password, cur);
//		resultMap = JSONUtils.json2Map(this.agTransferService.queryBalance(balanceParam));
//		if (SUCCESS.equals(resultMap.get(STATUS))) {
//			credit = resultMap.get("balance") + "";
//			String agBillno = "AGBT" + billno;
//			if (Integer.valueOf(credit) > 0) {
//				transferParam = new TransferParam(entity, username, password, credit, agBillno, type, cur, null, entity.getLiveId(), true);
//				resultMap = JSONUtils.json2Map(this.agTransferService.transfer(transferParam));
//				if (SUCCESS.equals(resultMap.get(STATUS))) {
//					logger.info("ag资金归集成功");
//					countBalanceCollection++;
//				}
//			} else {
//				logger.info("ag资金归集余额为0");
//				countBalanceCollection++;
//			}
//		}
//		//bbin
//		balanceParam = new QueryBalanceParam(bbinEntity, username, password, cur);
//		resultMap = JSONUtils.json2Map(this.bbinTransferService.queryBalance(balanceParam));
//		if (SUCCESS.equals(resultMap.get(STATUS))) {
//			credit = resultMap.get("balance") + "";
//			if (Integer.valueOf(credit) > 0) {
//				transferParam = new TransferParam(bbinEntity, username, password, credit, billno, type, bbinEntity.getCurrencyType(), null, bbinEntity.getLiveId(), true);
//				resultMap = JSONUtils.json2Map(this.bbinTransferService.transfer(transferParam));
//				if (SUCCESS.equals(resultMap.get(STATUS))) {
//					logger.info("bbin资金归集成功");
//					countBalanceCollection++;
//				}
//			} else {
//				logger.info("bbin资金归集余额为0");
//				countBalanceCollection++;
//			}
//		}
//		//h8
//		balanceParam = new QueryBalanceParam(h8Entity, username, password, cur);
//		resultMap = JSONUtils.json2Map(this.h8TransferService.queryBalance(balanceParam));
//		if (SUCCESS.equals(resultMap.get(STATUS))) {
//			billno = "H8BT" + billno;
//			credit = resultMap.get("balance") + "";
//			if (Integer.valueOf(credit) > 0) {
//				transferParam = new TransferParam(h8Entity, username, password, credit, billno, type, h8Entity.getCurrencyType(), null, h8Entity.getLiveId(), true);
//				resultMap = JSONUtils.json2Map(this.h8TransferService.transfer(transferParam));
//				if (SUCCESS.equals(resultMap.get(STATUS))) {
//					logger.info("h8资金归集成功");
//					countBalanceCollection++;
//				}
//			} else {
//				logger.info("h8资金归集余额为0");
//				countBalanceCollection++;
//			}
//		}
//		return countBalanceCollection;
//	}
//
//	/**
//	 * 登录验证key
//	 */
//	private boolean validKeyByLogin(String username, String password, String key, ApiInfoEntity entity) {
//		return this.validKey(username, password, key, entity, 6, 9);
//	}
//
//	/**
//	 * 查询余额验证key
//	 */
//	private boolean valiKeyByQueryBalance(String username, String password, String key, ApiInfoEntity entity) {
//		return this.validKey(username, password, key, entity, 7, 3);
//	}
//
//	/**
//	 * 转账验证key 
//	 */
//	private boolean validKeyByTransfer(String username, String password, String key, ApiInfoEntity entity) {
//		return this.validKey(username, password, key, entity, 4, 1);
//	}
//
//	/**
//	 * 验证通信key
//	 */
//	private boolean validKey(String username, String password, String key, ApiInfoEntity entity, int startIndex, int atEnd) {
//		StringBuilder keyParam = new StringBuilder();
//		keyParam.append(username).append(password).append(entity.getKeyb()).append(StringsUtil.updateTime());
//		key = key.substring(startIndex, key.length() - atEnd);
//		return key.equals(StringsUtil.toMD5(keyParam.toString()));
//	}
//
}
