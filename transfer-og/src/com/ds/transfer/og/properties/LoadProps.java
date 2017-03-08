package com.ds.transfer.og.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 加载og的一些配置
 * 
 * @author jackson
 *
 */
public class LoadProps {

	public static Properties props = new Properties();

	static {
		try {
			props.load(new FileInputStream(new File("resource/og.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	//	public static void main(String[] args) {
	//		String ogUrl = LoadProps.getProperty("og_url");
	//		System.out.println(ogUrl.replaceAll(":params", "pp").replaceAll(":key", "kk"));
	//		System.out.println(LoadProps.getProperty("og_key"));
	//	}
}
