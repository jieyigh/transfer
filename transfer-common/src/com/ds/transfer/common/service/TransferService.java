package com.ds.transfer.common.service;

import java.util.Map;

import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;

/**
 * 公共转账服务接口
 * 
 * @author jackson
 *
 */
public interface TransferService<T> {

	/**
	 * 转账
	 * @param transferParam 转账参数
	 * @param resultMap 转账返回引用
	 * @return
	 */
	String transfer(TransferParam transferParam);

	/**
	 * 查询余额
	 * @param param 查询余额参数
	 * @return
	 */
	String queryBalance(QueryBalanceParam param);

	/**
	 * 登录
	 * @param param 登录所需参数
	 * @return
	 */
	String login(LoginParam param);

	/**
	 * 登录单个游戏
	 * @param param 登录单个游戏所需参数
	 * @return
	 */
	String loginBySingGame(LoginParam param);

	/**
	 * 查询用户是否存在,ag比较特殊不存在就创建
	 * @param param 用户参数
	 * @return 用户实体
	 */
	T queryUserExist(String username);

	Map<String, Object> checkAndCreateMember(UserParam param);

	/**
	 * 查询订单状态
	 * @param billno 订单号
	 * @return
	 */
	String queryStatusByBillno(QueryOrderStatusParam param);
}
