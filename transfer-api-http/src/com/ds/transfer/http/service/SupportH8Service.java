package com.ds.transfer.http.service;

import com.ds.transfer.record.entity.ApiInfoEntity;

/**
 * H8单独的服务
 * 
 * @author jackson
 *
 */
public interface SupportH8Service {

	/**
	 * 修改限红的接口
	 * 
	 * @param entity
	 * @param username 用户名
	 * @param maxCreditPerBet 单次下注最大金额
	 * @param maxCreditPerMatch 每次赛事最大金额
	 */
	String changeOddType(ApiInfoEntity entity, String username, Integer maxCreditPerBet, Integer maxCreditPerMatch);

}
