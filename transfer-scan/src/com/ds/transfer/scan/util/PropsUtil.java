package com.ds.transfer.scan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropsUtil {

	public static Properties props = new Properties();

	static {
		try {
			props.load(new FileInputStream(new File("resource/scan.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
}
