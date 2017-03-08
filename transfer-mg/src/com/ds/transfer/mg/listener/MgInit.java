package com.ds.transfer.mg.listener;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ds.transfer.mg.constants.MgConstants;

/**
 * spring 初始化需要准备些数据
 * 
 * @author jackson
 *
 */
@Component
public class MgInit {
	private static final Logger logger = LoggerFactory.getLogger(MgInit.class);

	public static Properties props = new Properties();

	static {
		try {
			logger.info("MgInit start!");
			props.load(new FileInputStream("resource/mg.properties"));
			initMgConstants();
			logger.info(MgConstants.print());
		} catch (Exception e) {
			logger.error("MgInit init error",e);
		}
	}
	
	static void initMgConstants(){
		MgConstants.CRID = props.getProperty("CRID");
		MgConstants.CRTYPE = props.getProperty("CRTYPE");
		MgConstants.NEID = props.getProperty("NEID");
		MgConstants.NETYPE = props.getProperty("NETYPE");
		MgConstants.P_USM = props.getProperty("P_USM");
		MgConstants.P_PWD = props.getProperty("P_PWD");
		MgConstants.PARTNERID = props.getProperty("PARTNERID");
		MgConstants.MG_PREFIX = props.getProperty("MG_PREFIX");
		MgConstants.TARTYPE = props.getProperty("TARTYPE");
		MgConstants.PRODUCT = props.getProperty("PRODUCT");
		MgConstants.CURRENCY_CODE = props.getProperty("CURRENCY_CODE");
		MgConstants.API_USERNAME = props.getProperty("API_USERNAME");
		MgConstants.API_PASSWD =props.getProperty("API_PASSWD");
		MgConstants.MG = props.getProperty("MG");
		MgConstants.MG_STATE_SUCCESS = props.getProperty("MG_STATE_SUCCESS");
		MgConstants.MG_MEMBER_URL = props.getProperty("MG_MEMBER_URL");
		MgConstants.MG_WEBSITE_URL = props.getProperty("MG_WEBSITE_URL");
		MgConstants.MG_MEMCREATION_URL = props.getProperty("MG_MEMCREATION_URL");
	}
}



