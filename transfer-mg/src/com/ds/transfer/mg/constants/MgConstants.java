package com.ds.transfer.mg.constants;

/**
 * mg 常量
 * 
 * @author jackson
 *
 */
public class MgConstants {
	  public static String CRID  ;  
	  public static String CRTYPE ;   
	
	  public static String NEID  ;  
	  public static String NETYPE  ;
	
	  public static String P_USM  ;    //后台管理帐号
	  public static String P_PWD  ; //后台管理密码
	
	  public static String PARTNERID  ;  
	
	  public static String MG_PREFIX  ;   //测试环境使用
	
	  public static String TARTYPE  ; 
	
	  public static String PRODUCT  ;
	
	  public static String CURRENCY_CODE;  
	
	  public static String API_USERNAME  ;  //APi帐号
	  public static String API_PASSWD  ; //APi密码
	
	  public static String MG  ;
	  public static String MG_STATE_SUCCESS  ;  
	
	  public static String MG_MEMBER_URL  ; 
	  public static String MG_WEBSITE_URL  ;  //后台管理 URL
	  public static String MG_MEMCREATION_URL  ;//创建会员URL

	  
	  public static String print(){
		  return "MgConstants [CRID=" + CRID + ", CRTYPE=" + CRTYPE + ", NEID="
					+ NEID + ", NETYPE=" + NETYPE + ", P_USM=" + P_USM + ", P_PWD="
					+ P_PWD + ", PARTNERID=" + PARTNERID + ", MG_PREFIX="
					+ MG_PREFIX + ", TARTYPE=" + TARTYPE + ", PRODUCT=" + PRODUCT
					+ ", CURRENCY_CODE=" + CURRENCY_CODE + ", API_USERNAME="
					+ API_USERNAME + ", API_PASSWD=" + API_PASSWD + ", MG=" + MG
					+ ", MG_STATE_SUCCESS=" + MG_STATE_SUCCESS + ", MG_MEMBER_URL="
					+ MG_MEMBER_URL + ", MG_WEBSITE_URL=" + MG_WEBSITE_URL
					+ ", MG_MEMCREATION_URL=" + MG_MEMCREATION_URL + "]";
	  }
	  
}
