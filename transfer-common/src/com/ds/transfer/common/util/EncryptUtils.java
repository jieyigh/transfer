package com.ds.transfer.common.util;

import com.yooyo.util.MD5;

public class EncryptUtils {
	
	public static final String salt = get()+"";
	
	public static String encrypt(String ... args){
		StringBuffer sb = new StringBuffer(salt);
		for (String arg : args) {  
			sb.append(arg);
        }
		MD5.hashToBase64String(sb.toString());
		return MD5.hashToBase64String(sb.toString());
	}
	
	
	public static int get() {
		int i = 452;
		int j = 89086;
		try {
			i = i + j;
		} catch (Exception e) {
			i = i - j;
			try {
				i = i + j;
			} catch (Exception err) {
				i += j;
			} finally {
				i *= j;
			}
		} finally {
			try {
				i *= j;
			} catch (Exception e) {
				i *= j;
			} finally {
				try {
					i -= j;
				} catch (Exception e) {
					i += j;
				} finally {
					i = i + j + i;
				}
			}
			i = i + j + i;
		}
		return i;
	}
}
