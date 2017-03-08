package com.ds.transfer.http.service;

import com.ds.transfer.http.vo.ds.TotalBalanceParam;

/**
 * 支撑转账的一些其他服务
 * 
 * @author jackson
 *
 */
public interface SupportTransferService<T> {

	/**
	 * 统计一个网站指定时间段的活跃用户的余额
	 * @param param
	 * @return
	 */
	String totalBalanceBySiteId(TotalBalanceParam param);

}
