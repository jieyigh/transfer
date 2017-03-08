package com.ds.transfer.http.util;

import com.ds.transfer.common.util.StringsUtil;

/**
 * 项目常用工具
 * 
 * @author jackson
 *
 */
public class TransferUtil {

	/**
	 * 删除用户名前缀
	 * @param username
	 * @param prefix
	 * @return
	 */
	public static String removePrefix(String username, String prefix) {
		if (StringsUtil.isNull(username)) {
			throw new RuntimeException("username");
		}
		if (StringsUtil.isNull(prefix)) {
			return username;
		}
		return username.substring(username.indexOf(prefix) + prefix.length());
	}

	/**
	 * 移除用户余额小数部分
	 * @param balance
	 * @return
	 */
	public static String removeDecimals(String balance) {
		if (StringsUtil.isNull(balance)) {
			throw new RuntimeException("balance为空");
		}
		if (balance.contains(".")) {
			balance = balance.substring(0, balance.indexOf("."));
		}
		return balance;
	}
}
