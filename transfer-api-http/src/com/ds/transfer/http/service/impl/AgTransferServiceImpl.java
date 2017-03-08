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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ds.transfer.http.constants.ApplicationContstants;
import com.ds.transfer.http.dao.TransferRecordDao;
import com.ds.transfer.http.service.AgTransferService;
import com.ds.transfer.http.util.TransferUtil;
import com.ds.transfer.http.vo.ds.TotalBalanceParam;
import com.ds.transfer.http.vo.thread.TotalBalanceByLive;
import com.ds.transfer.http.vo.thread.TotalBalanceThread;
import com.ds.transfer.record.entity.AgApiUserEntity;
import com.ds.transfer.record.entity.ApiInfoEntity;

@Service("agTransferServiceImpl")
public class AgTransferServiceImpl extends CommonTransferService implements AgTransferService<AgApiUserEntity> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "agTransfer")
	private TransferService<AgApiUserEntity> agTransfer;

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
			String type = param.getType();
			billno = param.getBillno();
			if (IN.equals(type)) {
				if (param.isTotalBalance()) {
					transferRemark = "DS主账户 转账至 AG视讯(资金归集)";
					param.setBillno(billno + "T" + "_" + entity.getLiveName());
				} else {
					transferRemark = "DS主账户 转账至 AG视讯" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = OUT;
				param.setType(oppositeType);
				param.setRemark(transferRemark);
				param.setLiveId(SysConstants.LiveId.AG);
				result = this.moneyCenter.transfer(param);
				providerIp = printProviderIp();
				logger.info("money transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(type);
					param.setBillno(billno);
					result = this.agTransfer.transfer(param);
					providerIp = printProviderIp();
					logger.info("ag transfer ip = {}, result = {}", providerIp, result);
				}
			} else {
				if (param.isTotalBalance()) {
					transferRemark = "AG视讯 转账至 DS主账户(资金归集)";
					billno = billno + "T" + "_" + entity.getLiveName();
				} else {
					transferRemark = "AG视讯 转账至 DS主账户" + (StringsUtil.isNull(param.getRemark()) ? "" : param.getRemark());
				}
				oppositeType = IN;
				param.setType(type);
				param.setRemark(transferRemark);
				result = this.agTransfer.transfer(param);
				providerIp = printProviderIp();
				logger.info("ag transfer ip = {}, result = {}", providerIp, result);
				resultMap = JSONUtils.json2Map(result);
				if (SUCCESS.equals(resultMap.get(STATUS))) {
					param.setType(oppositeType);
					param.setLiveId(SysConstants.LiveId.AG);
					param.setBillno(billno);
					result = this.moneyCenter.transfer(param);
					providerIp = printProviderIp();
					logger.info("money transfer ip = {}, result = {}", providerIp, result);
				}
			}
			resultMap = JSONUtils.json2Map(result);
		} catch (Exception e) {
			logger.error("money<---->ag转账错误", e);
			return JSONUtils.map2Json(maybe("ag转账异常"));
		}
		return JSONUtils.map2Json(resultMap);
	}

	/*
	 * @override public string transfer(transferparam param) { return jsonutils.map2json(this.transfer(param, false)); }
	 */

	@Override
	public String queryBalance(QueryBalanceParam param) {
		Map<String, Object> resultMap = null;
		String result = null;
		String providerIp = null;
		try {
			ApiInfoEntity entity = param.getEntity();
			//1.用户存在? 
			UserParam userParam = new UserParam(entity, param.getUsername(), null, null, null);
			resultMap = this.checkAndCreateMember(userParam);
			if (!SUCCESS.equals(resultMap.get(STATUS))) {
				return JSONUtils.map2Json(resultMap);
			}
			//2.查询余额
			result = this.agTransfer.queryBalance(param);
			providerIp = printProviderIp();
			logger.info("ag queryBalance ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("ag查询余额异常 : ", e);
			return JSONUtils.map2Json(maybe("ag查询余额异常"));
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
			resultMap = this.checkAndCreateMember(userParam);
			if (!SUCCESS.equals(resultMap.get(STATUS))) {
				return JSONUtils.map2Json(resultMap);
			}

			//2.登录
			result = this.agTransfer.login(param);
			providerIp = printProviderIp();
			logger.info("ag login ip = {}, result = {}", providerIp, result);
			return result;
		} catch (Exception e) {
			logger.error("ag登录异常 : ", e);
			return JSONUtils.map2Json(maybe("ag登录异常"));
		}
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public AgApiUserEntity queryUserExist(String username) {
		return this.agTransfer.queryUserExist(username);
	}

	@Override
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		Map<String, Object> resultMap = this.agTransfer.checkAndCreateMember(param);
		String providerIp = printProviderIp();
		logger.info("ag checkAndCreateMember ip = {}, result = {}", providerIp, JSONUtils.map2Json(resultMap));
		return resultMap;
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return this.agTransfer.queryStatusByBillno(param);
	}

	@Override
	public String totalBalanceBySiteId(TotalBalanceParam param) {
		logger.info("ag统计余额参数 : siteId = {}, liveType = {}, fromDate = {}, toDate = {}", param.getSiteId(), param.getLiveType(), param.getFromDate(), param.getToDate());
		Long balance = null;
		try {
			List<String> userNameList = this.transferRecordDao.totalBalanceBySiteId(param);
			logger.info("ag用户数 = {}", userNameList.size());
			if (userNameList != null && userNameList.size() > 0) {
				Map<String, Future<Long>> resultMap = queryBalanceMultipartByUserName(param, userNameList, ApplicationContstants.TotalBalance.AG_THREAD_COUNT);
				logger.info("ag收集到结果数 = {}", resultMap.size());
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
			logger.info("{} result ag : username = {}, balance = {}", liveName, entry.getKey(), entry.getValue().get());
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

}
