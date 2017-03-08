package com.ds.transfer.http.util;

import java.io.IOException;
import java.util.Properties;

public class PropsUtil {

	public static Properties props = new Properties();

	static {
		try {
			props.load(PropsUtil.class.getClassLoader().getResourceAsStream("transfer.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

}
