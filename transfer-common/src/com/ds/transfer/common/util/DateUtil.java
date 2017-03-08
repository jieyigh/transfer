package com.ds.transfer.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 
 * @author jackson
 *
 */
public class DateUtil {

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	// 获取当前时间
	public static String getCurrentTime() {
		return sdf1.format(new Date(System.currentTimeMillis()));
	}

}
