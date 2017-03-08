package com.ds.transfer.scan.util;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 关于字符串的工具类
 * 
 * @author jackson
 *
 */
public class StringUtil {

	private static Random random = new Random();
	private static char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static String randomString(int digit) {

		StringBuilder build = new StringBuilder(digit);
		for (int i = 0; i < digit; i++) {
			build.append(str[random.nextInt(26)]);
		}

		return build.toString();
	}

	/**
	 * 判断是否为空,反之!
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return (str == null || "".equals(str.trim()) || "null".equals(str.trim())) ? true : false;
	}

	public static Map<String, Object> isNull(Map<String, Object> resultMap) {
		Set<Entry<String, Object>> entrySet = resultMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			if (isNull(entry.getValue() + "")) {
				resultMap.clear();
				resultMap.put("status", "100099");
				resultMap.put("message", entry.getKey() + " is null");
				return resultMap;
			}
		}
		resultMap.clear();
		resultMap.put("status", "10000");
		resultMap.put("message", "success");
		return resultMap;
	}

}
