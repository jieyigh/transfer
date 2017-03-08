package com.ds.transfer.scan.util;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {

	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	private static final String DEFAULT_JOIN = "&";//参数默认连接符

	/**
	 * vo转成字符连接,,默认是& 
	 */
	public static <T> String generateParam(T t, String join) {
		StringBuffer paramBuffer = new StringBuffer();
		try {
			Class<?> clazz = t.getClass();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("get") && !"getClass".equals(methodName)) {
					methodName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
					String value = method.invoke(t) + "";
					if (!StringUtil.isNull(value)) {
						paramBuffer.append(methodName).append("=").append(value).append(join);
					}
				}
			}
			return paramBuffer.substring(0, paramBuffer.length() - join.length()).toString();
		} catch (Exception e) {
			logger.error("已经解析 = {}", paramBuffer.toString());
			logger.error("反射错误 : ", e);
		}
		return paramBuffer.toString();
	}

	public static <T> String generateParam(T t) {
		return generateParam(t, DEFAULT_JOIN);
	}
}
