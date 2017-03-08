package com.ds.transfer.ag.constants;

/**
 * ag 常量
 * 
 * @author jackson
 *
 */
public interface AgConstants {

	/**
	 * 	public static String ag_url = "http://gi.s1117.com:81/";
		public static String ag_login_url = "http://gci.s1117.com:81/";//ag 登陆 域名
		public static String ag_agent = "D59_AGIN";
		public static String ag_md5 = "DSF23DS3D3";
		public static String ag_DES = "DFF23S2D";
		public static String ftp_url = "br.agingames.com";//ftp.agingames.com
		public static String ftp_username = "D59.dingsheng";///ftp 用户名
		public static String ftp_password = "EczMUlUCcb"; ////ftp 密码
		public static String ftp_fileName = "/AGIN/";   //ftp 文件名
		public static final String ftp_lostand_found = "/AGIN/lostAndfound/";
		public static int ftp_port = 21;    ///ftp 文件端口
	 */
	String AG = "ag";
	String AG_PARAM_JOIN = "/\\\\/";
	String TRANS_SUCCESS = "0";
	String PRE_TRANSFER = "tc";
	String CONFIRM_TRANSFER = "tcc";
	String QUERY_BALANCE = "gb";
	String CREATE_MEMBER = "lg";
	String QUERY_ORDER_STATUS = "qos";
	String AG_MD5_KEY = "QpXZZmCfJgGQ";
	String LOGIN_MD5_KEY = "DSF23DS3D3";
	
	/**
	 * 更换 URL
	 */
	/*String CREATE_MEMBER_URL = "http://gi.s1117.com:81";
	String LOGIN_URL = "http://gci.chengyuanshengyu.com:81";
	String AG_URL = "http://gi.chengyuanshengyu.com:81";*/
	
	
	String CREATE_MEMBER_URL = "http://gi.kgit2.com:81";
	String LOGIN_URL = "http://gci.kgit2.com:81";
	String AG_URL = "http://gi.kgit2.com:81";
	/*gi.kgit2.com
	gci.kgit2.com*/
	
	String LIVE_ID = "2";
	String AG_DES_F30 = "C9BO5hiE";
	String AG_DES_D59 = "DFF23S2D";

	interface Function {
		String BUSINESS_FUNCTION = "/doBusiness.do";
		String LOGIN_FUNCTION = "/forwardGame.do";
	}
}
