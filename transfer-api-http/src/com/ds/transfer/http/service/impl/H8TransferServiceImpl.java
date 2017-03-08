package com.ds.transfer.http.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;
import com.ds.transfer.common.util.XmlUtil;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.http.constants.ApplicationContstants;
import com.ds.transfer.http.dao.TransferRecordDao;
import com.ds.transfer.http.service.H8TransferService;
import com.ds.transfer.http.util.TransferUtil;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;
import com.ds.transfer.http.vo.thread.TotalBalanceThread;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.H8ApiUserEntity;

@Service("h8TransferServiceImpl")
public class H8TransferServiceImpl extends CommonTransferService implements H8TransferService<H8ApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "h8Transfer")
	private TransferService<H8ApiUserEntity> h8Transfer;

	@Resource(name = "moneyCenter")
	private TransferService<?> moneyCenter;

	@Autowired
	private TransferRecordDao transferRecordDao;

	@Override
	public String transfer(TransferParam param) {
		String transferRemark = null;
		String oppositeType = null;
		String result = null;
		String billno = null;
		String providerIp = null;
		Map<String, Object> resultMap = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			billno = param.getBillno();
			String type = param.getType();
			if (IN.equals(type)) {
				if (param.isTotalBalance()) {
					transferRemark = "DS主账户 转账至 H8视讯(资金归集)";
					param.setBillno(billno + "T" + "_" + entity.getLiveName());
				} else {
					transferRemark = "DS主账户 转账至 H8视讯" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = OUT;
				param.setType(oppositeType);
				param.setRemark(transferRemark);
				param.setLiveId(SysConstants.LiveId.H8);
				result = this.moneyCenter.transfer(param);
				providerIp = printProviderIp();
				logger.info("money transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(type);
					param.setBillno(billno);
					result = this.h8Transfer.transfer(param);
					providerIp = printProviderIp();
					logger.info("h8 transfer ip = {}, result = {}", providerIp, result);
				}
			} else {
				if (param.isTotalBalance()) {
					transferRemark = "H8视讯 转账至 DS主账户(资金归集)";
					billno = billno + "T" + "_" + entity.getLiveName();
				} else {
					transferRemark = "H8视讯 转账至 DS主账户" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = IN;
				param.setType(type);
				param.setRemark(transferRemark);
				result = this.h8Transfer.transfer(param);
				providerIp = printProviderIp();
				logger.info("h8 transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(oppositeType);
					param.setLiveId(SysConstants.LiveId.H8);
					param.setBillno(billno);
					result = this.moneyCenter.transfer(param);
					providerIp = printProviderIp();
					logger.info("money transfer ip = {}, result = {}", providerIp, result);
				}
			}
			return result;
		} catch (Exception e) {
			logger.error("转账业务异常 : ", e);
			resultMap = maybe("h8转账异常");
		}
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		String result = null;
		String providerIp = null;
		try {
			//1.用户存在?
			ApiInfoEntity entity = param.getEntity();
			UserParam userParam = new UserParam(entity, param.getUsername(), null, null, null);
			resultMap = this.checkAndCreateMember(userParam);
			if (!SUCCESS.equals(resultMap.get(STATUS))) {
				return JSONUtils.map2Json(resultMap);
			}
			//2.查询余额
			result = this.h8Transfer.queryBalance(param);
			providerIp = printProviderIp();
			logger.info("h8 queryBalance ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("h8查询余额异常 : ", e);
			return JSONUtils.map2Json(maybe("h8查询余额异常"));
		}
	}

	@Override
	public String login(LoginParam param) {
		ApiInfoEntity entity = param.getEntity();
		Map<String, Object> resultMap = null;
		String result = null;
		String providerIp = null;
		try {
			//1.用户存在? 
			UserParam userParam = new UserParam(entity, param.getUsername(), null, null, "1");
			resultMap = this.checkAndCreateMember(userParam);
			if (!SUCCESS.equals(resultMap.get(STATUS))) {
				return JSONUtils.map2Json(resultMap);
			}

			//2.登录
			result = this.h8Transfer.login(param);
			providerIp = printProviderIp();
			logger.info("h8 login ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("h8登录异常", e);
			return JSONUtils.map2Json(maybe("h8登录异常"));
		}
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public H8ApiUserEntity queryUserExist(String username) {
		return this.h8Transfer.queryUserExist(username);
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = this.h8Transfer.checkAndCreateMember(param);
		String providerIp = printProviderIp();
		logger.info("h8 checkAndCreateMember ip = {}, result = {}", providerIp, JSONUtils.map2Json(resultMap));
		return resultMap;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return this.h8Transfer.queryStatusByBillno(param);
	}

	@Override
	public String totalBalanceBySiteId(TotalBalanceParam param) {
		logger.info("h8统计余额参数 : siteId = {}, liveType = {}, fromDate = {}, toDate = {}", param.getSiteId(), param.getLiveType(), param.getFromDate(), param.getToDate());
		Long balance = null;
		try {
			List<String> userNameList = this.transferRecordDao.totalBalanceBySiteId(param);
			logger.info("bbin用户数 = {}", userNameList.size());
			if (userNameList != null && userNameList.size() > 0) {
				Map<String, Future<Long>> resultMap = queryBalanceMultipartByUserName(param, userNameList, ApplicationContstants.TotalBalance.BBIN_THREAD_COUNT);
				logger.info("bbin收集到结果数 = {}", resultMap.size());
				balance = totalBalanceByResultFuture(param.getEntity().getLiveName(), resultMap);
			}
		} catch (Exception e) {
			logger.error("ag统计余额 系统错误 :　", e);
			return JSONUtils.map2Json(failure("failure!"));
		}
		return JSONUtils.map2Json(success(balance + ""));
	}

	/**
	 * 根据future结果统计余额
	 * @param liveName
	 * @param resultMap
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private Long totalBalanceByResultFuture(String liveName, Map<String, Future<Long>> resultMap) throws InterruptedException, ExecutionException {
		Long balance = 0L;
		Iterator<Entry<String, Future<Long>>> iterator = resultMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Future<Long>> entry = iterator.next();
			logger.info("{} result h8 : username = {}, balance = {}", liveName, entry.getKey(), entry.getValue().get());
			balance += entry.getValue().get();
		}
		return balance;
	}

	/**
	 * 根据用户名统计余额
	 * 
	 * @param param
	 * @param userNameList
	 * @param pool
	 * @param latch
	 * @return
	 * @throws InterruptedException
	 */
	private Map<String, Future<Long>> queryBalanceMultipartByUserName(TotalBalanceParam param, List<String> userNameList, Integer threadCount) throws Exception {
		Map<String, Future<Long>> resultMap = new HashMap<String, Future<Long>>();
		QueryBalanceParam queryBalanceParam = null;
		ExecutorService pool = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = null;
		//大于线程数
		if (userNameList.size() >= threadCount) {
			latch = new CountDownLatch(threadCount);
		} else { //小于线程数
			latch = new CountDownLatch(userNameList.size());
		}
		for (int i = 0; i < userNameList.size(); i++) {
			queryBalanceParam = new QueryBalanceParam(param.getEntity(), TransferUtil.removePrefix(userNameList.get(i), param.getEntity().getPrefix()), param.getEntity().getCurrencyType());
			TotalBalanceThread thread = new TotalBalanceThread(this, queryBalanceParam, latch);
			Future<Long> future = pool.submit(thread);
			resultMap.put(userNameList.get(i), future);
			if (userNameList.size() >= threadCount) {
				if (i != 0 && i % 20 == 0) {
					latch.await();
				}
			} else {
				if (i == userNameList.size() - 1) {
					latch.await();
				}
			}
		}
		return resultMap;
	}

	@Override
	public String changeOddType(ApiInfoEntity entity, String username, Integer maxCreditPerBet, Integer maxCreditPerMatch) {
		//http://<host>/api.aspx?secret=<secret>&action=update&agent=<agent>&username=<username>&max1=1100&lim1=1000&comtype=A&com1=0.1&com2=0.2&com3=0.3&suspend=0
		String result = null;
		String params = "secret=" + entity.getSecret() + "&action=update&agent=" + entity.getAgent() + "&username=" + username + "&max1="//
				+ maxCreditPerBet + "&lim1=" + maxCreditPerMatch + "&comtype=A&com1=0&com2=0&com3=0&suspend=0";
		//		entity.getReportUrl()
		try {
			result = StringsUtil.sendGet(entity.getReportUrl(), params);
			XmlUtil xmlUtil = new XmlUtil(result);
			String code = xmlUtil.getSelectNodes("/response/errcode").get(0).getStringValue();
			if ("0".equals(code)) {
				return JSONUtils.map2Json(success("success!"));
			}
			return JSONUtils.map2Json(failure(xmlUtil.getSelectNodes("/response/errtext").get(0).getStringValue()));
		} catch (Exception e) {
			logger.error("H8变更限红异常 : ", e);
			return JSONUtils.map2Json(maybe("H8变更限红异常"));
		}
	}

}
