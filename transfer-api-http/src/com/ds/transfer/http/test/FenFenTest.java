package com.ds.transfer.http.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.junit.Test;

import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.util.StringsUtil;

public class FenFenTest {

	@Test
	public void testIndex() throws Exception {

		transferDs("etest4", "123213", "12", "IN", 1);

	}
	
	@Test
	public void testSemaphore() throws Exception {
		
	}
	
	@Test
	public void testThreadCallable() throws Exception {
		ExecutorService exec = Executors.newFixedThreadPool(6);
		CountDownLatch latch = new CountDownLatch(6);
		Map<String, Future<?>> futureMap = new HashMap<String, Future<?>>();
		for (int i = 1; i <= 7; i++) {
			ThreadTest t = new ThreadTest(i, latch);
//			FutureTask<String> task = new FutureTask<String>(t);
			Future<?> submit = exec.submit(t);
			futureMap.put("call" + i, submit);
		}
		latch.await();
		Set<Entry<String, Future<?>>> entrySet = futureMap.entrySet();
		for (Entry<String, Future<?>> entry : entrySet) {
			System.out.println(entry.getValue().get().toString());
		}
	}
	
	@Test
	public void testBigDecimal() throws Exception {
		BigDecimal balance = new BigDecimal("0");
		balance = balance.add(new BigDecimal("20"));
		balance = balance.add(new BigDecimal(30));
		balance = balance.add(new BigDecimal(40));
		System.out.println(balance.toPlainString());
	}

	public class ThreadTest implements Callable<String> {
		private int i;
		private CountDownLatch latch;

		public ThreadTest(int i, CountDownLatch latch) {
			this.i = i;
			this.latch = latch;
		}

		@Override
		public String call() throws Exception {
			latch.countDown();
			return "ab" + i;
		}
	}

	public static void transferDs(String username, String password, String live, String type, Integer money) throws Exception {
		String billno = String.valueOf(System.currentTimeMillis());
		//secret=a878624d&agent=3r01&username=csgame1234&action=deposit&serial=1414443942811487403&amount=6
		//agent=3r01&secret=a878624d&amount=5&serial=1414443942811487403&action=deposit&username=csgame1234
		String url = "http://119.9.91.186:19007/index";
		//		String password = "20IuNngjW675ZMfamwnsr";
		String key = "1234" + StringsUtil.toMD5(username + password + "qqqq" + StringsUtil.updateTime()) + "5";
		String siteId = "99999";
		//		String live = "13";//DS主账户 <==> H8 互转

		//		String type = "IN";//IN or OUT
		String credit = money + "";
		String isDemo = "1";
		String cur = "RMB";
		String transMethod = "ds";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("username", username);
		paramMap.put("password", password);
		paramMap.put("key", key);
		paramMap.put("hashCode", "djhfalhdflhjasfljdhasfilsf");
		paramMap.put("live", 12);
		paramMap.put("billno", billno);
		paramMap.put("type", type);
		paramMap.put("credit", credit);
		paramMap.put("isDemo", isDemo);
		paramMap.put("cur", cur);

		String param = "command=transfer&param=" + JSONUtils.map2Json(paramMap);

		String returnTransfer = StringsUtil.sendPost1(url, param);
		System.out.println("OG 返回值:" + returnTransfer);
	}
}
