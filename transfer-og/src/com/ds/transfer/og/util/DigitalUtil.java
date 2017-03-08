package com.ds.transfer.og.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 数字工具类
 * 
 * @author jackson
 *
 */
public class DigitalUtil {
	
	private static final String MONEY_REGEX = "[\\d\\.]";//数字和点
	
	/**
	 * 必须是大于等于0的数
	 */
	public static boolean isMoney(String s) {
		for(int i=0; i< s.length() ;i ++){
			Pattern p = Pattern.compile(MONEY_REGEX);
			Matcher m = p.matcher(s.charAt(i) + "");
			if(!m.matches()){
				return false;
			}
		}
		return true;
	}
	
}
