package com.ds.transfer.mg.service;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.onetwo.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.ds.transfer.common.constants.SysConstants;
import com.ds.transfer.common.service.CommonTransferService;
import com.ds.transfer.common.service.TransferService;
import com.ds.transfer.common.util.JSONUtils;
import com.ds.transfer.common.vo.LoginParam;
import com.ds.transfer.common.vo.QueryBalanceParam;
import com.ds.transfer.common.vo.QueryOrderStatusParam;
import com.ds.transfer.common.vo.TransferParam;
import com.ds.transfer.common.vo.UserParam;
import com.ds.transfer.mg.constants.MgConstants;
import com.ds.transfer.mg.util.MGUtil;
import com.ds.transfer.mg.util.TimeUtil;
import com.ds.transfer.record.entity.ApiInfoEntity;
import com.ds.transfer.record.entity.MgApiUserEntity;
import com.ds.transfer.record.entity.MgApiUserEntityExample;
import com.ds.transfer.record.entity.TransferRecordEntity;
import com.ds.transfer.record.mapper.MgApiUserEntityMapper;
import com.ds.transfer.record.service.TransferRecordService;

@Service
public class TransferServiceImpl extends CommonTransferService implements TransferService<MgApiUserEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

	@Resource(name = "transferRecordService")
	private TransferRecordService transferRecordService;

	@Resource(name = "moneyCenterService")
	private TransferService<?> moneyCenterService;

	@Autowired
	private MgApiUserEntityMapper mgApiUserEntityMapper;
	
	@Override
	public String transfer(TransferParam transferParam) {
		TransferRecordEntity mgRecord = new TransferRecordEntity();
		String msg = "";
		try {
			ApiInfoEntity entity = transferParam.getEntity();
			String username = transferParam.getUsername();
			username =MgConstants.MG_PREFIX+MgConstants.PARTNERID+entity.getPrefix() + username;
			//mg 转账记录
			mgRecord = this.transferRecordService.insert(entity.getSiteId(), entity.getLiveId(), transferParam.getTransRecordId(), entity.getPassword(), username, transferParam.getCredit(), transferParam.getBillno(),//
					transferParam.getType(), MgConstants.MG, transferParam.getRemark(), mgRecord);
			logger.info("mg转账记录插入成功,  id = {}", mgRecord.getId());
			//会员登录
			String token=loginMember(username, entity.getPassword(), entity.getIp());

			//转账
			boolean result=transaction(token,MgConstants.PRODUCT,transferParam.getType().equals("IN")?"topup":"withdraw",transferParam.getCredit(),transferParam.getBillno());
			
			
			if(result){
				return JSONUtils.map2Json(success("MG transfer success"));
			}
			logger.info("MG transfer fail");
			//失败更新下记录
			mgRecord = this.transferRecordService.update(SysConstants.Record.TRANS_MAYBE, "MG转账异常", mgRecord);
		} catch (Exception e) {
			logger.error("mg转账异常:", e);
		}
		return JSONUtils.map2Json(maybe("MG转账异常:" + msg));
	}

	
	@Override
	public String queryBalance(QueryBalanceParam param) {
		ApiInfoEntity entity = param.getEntity();
		String username = param.getUsername();
		username =MgConstants.MG_PREFIX+MgConstants.PARTNERID+entity.getPrefix() + username;
		
		String token=loginMember(username, param.getEntity().getPassword(), param.getEntity().getIp());
		if(token==null){
			return JSONUtils.map2Json(failure("MG queryBalance token is null"));
		}
		String balance=getPlayerBalance(token);
		if(balance==null){
			return JSONUtils.map2Json(failure("MG getPlayerBalance  is null"));
		}
		Map<String,Object> resultMap=success("MG queryBalance  sucess");
		resultMap.put("balance", balance);
		return  JSONUtils.map2Json(resultMap);
		
	}

	@Override
	public String login(LoginParam param) {
		String username = param.getUsername();
		username =MgConstants.MG_PREFIX+MgConstants.PARTNERID+param.getEntity().getPrefix() + username;
		String token=loginMember(username, param.getEntity().getPassword(), param.getEntity().getIp());
		if(token==null){
			return JSONUtils.map2Json(failure("MG loginMember  is null"));
		}
		String launchUrl= loginGame(token, param.getGameType(), param.getBankingUrl(),param.getLobbyUrl(),param.getLogoutRedirectUrl(), param.getIsDemo(),param.getLanguage());
		if(launchUrl==null){
			return JSONUtils.map2Json(failure("MG loginGame  is null"));
		}
		Map<String, Object> resultMap=success("MG login success");
		resultMap.put("launchUrl", launchUrl);
		return JSONUtils.map2Json(resultMap);
	}

	@Override
	public String loginBySingGame(LoginParam param) {
		return this.login(param);
	}

	@Override
	public MgApiUserEntity queryUserExist(String username) {
		MgApiUserEntityExample agApiUserExample = new MgApiUserEntityExample();
		agApiUserExample.createCriteria().andUsernameEqualTo(username);
		List<MgApiUserEntity> list = mgApiUserEntityMapper.selectByExample(agApiUserExample);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	@Override
	@Transactional
	public Map<String, Object> checkAndCreateMember(UserParam param) {
		ApiInfoEntity entity = param.getEntity();
		String username = param.getUsername();
		//测试环境需要MG_PREFIX
		username =MgConstants.MG_PREFIX+MgConstants.PARTNERID+entity.getPrefix() + username; //DF91CSceshi
		MgApiUserEntity user = this.queryUserExist(username);
		if (user == null) {
			String token=loginWebSite();
			if(token==null){
				return failure("MG checkAndCreateMember token is null");	
			}
			boolean result=mgMemberCreate(username,entity.getPassword(), token,entity.getCurrencyType(),entity.getLanguage());
			if(!result){
				return failure("MG checkAndCreateMember MG memberCreate is fail");
			}
			MgApiUserEntity mgApiUserEntity=localMemberCreate(entity, username, entity.getPassword(),entity.getCurrencyType());
			if(mgApiUserEntity==null){
				return failure("MG checkAndCreateMember local memberCreate is fail");
			}
		}
		
		return success("MG user exist!");
	}

	@Override
	public String queryStatusByBillno(QueryOrderStatusParam param) {
		return null;
	}

	
	//login to member api 
	private  String loginMember(String username,String passwd,String ip){
		logger.info("MG loginMember  username = {}, passwd = {}, ip = {}",username,passwd,ip);
		ip=getRealIp();
		String token=null;
		try{
			String xml = null;
			Document document;
			Element root;
			root = new Element("mbrapi-login-call");
			document = new Document(root);
			root.setAttribute("timestamp", DateUtil.format("yyyy-MM-dd HH:mm:ss", new Date())+" UTC");//此为测试，正式必须获取UTC时间
			root.setAttribute("apiusername", MgConstants.API_USERNAME);
			root.setAttribute("apipassword", MgConstants.API_PASSWD);
			root.setAttribute("username", username);
			root.setAttribute("password",passwd);
			root.setAttribute("ipaddress", ip);
			xml =MGUtil.doc2String(document);
			HttpClient httpClients =  HttpClients.createDefault();
			HttpPost request = new HttpPost(MgConstants.MG_MEMBER_URL);
		    HttpResponse response=null;
		    request.addHeader("Content-Type",  ContentType.APPLICATION_XML.toString());
			StringEntity params =new StringEntity(xml);
			request.setEntity(params);
			response = httpClients.execute(request);
			String xmlResult=EntityUtils.toString(response.getEntity());
			logger.info("MG loginMember  xmlResult={}",xmlResult);
			token = MGUtil.getXmlValue(xmlResult, "token");
		}catch(Exception e){
			logger.error("MG loginMember error",e);
		}
		return token;
	}
	
	
	//Get Player Details & Balance 
	private String getPlayerBalance(String token){
		logger.info("MG getPlayerBalance  token = {}",token);

		String balance=null;
		try {
			Document document;
			Element root;
			root = new Element("mbrapi-account-call");
			document = new Document(root);
			String UtcTime = (String)TimeUtil.getUtcTime();
			root.setAttribute("timestamp", UtcTime+" UTC");//此为测试，正式必须获取UTC时间
			root.setAttribute("apiusername",MgConstants.API_USERNAME);
			root.setAttribute("apipassword",MgConstants.API_PASSWD);
			root.setAttribute("token", token);
			String xml = MGUtil.doc2String(document);
			String result = MGUtil.sendXml(MgConstants.MG_MEMBER_URL, xml);
			logger.info("MG getPlayerBalance  result={}",result);
			//创建一个新的字符串
	        StringReader read = new StringReader(result);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            root = doc.getRootElement();
            //得到根元素所有子元素的集合
            Element node = root.getChild("wallets");
            Element childNode =  node.getChild("account-wallet");
            String creditBalance = childNode.getAttributeValue("credit-balance");
            balance=creditBalance;
		}catch (Exception e) {
			logger.error("MG getPlayerBalance error",e);
		}
		return balance;
		
	}
	
	
	//login to API Website
	private  static String  loginWebSite(){
		logger.info("MG loginWebSite  j_username = {} , j_password = {}",MgConstants.P_USM,MgConstants.P_PWD);

		String token=null;
		try {
		    HttpClient httpClients = HttpClients.createDefault();
			HttpPost request = new HttpPost(MgConstants.MG_WEBSITE_URL);
			request.addHeader("X-Requested-With", "X-Api-Client");
			request.addHeader("X-Api-Call", "X-Api-Client");
			Map<String,String>  param=new HashMap<String, String>();
			param.put("j_username", MgConstants.P_USM);
			param.put("j_password", MgConstants.P_PWD);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : param.entrySet()) {
			            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			request.setEntity(entity);
		    HttpResponse response = httpClients.execute(request);
		    String content=EntityUtils.toString(response.getEntity());
			logger.info("MG loginWebSite  content={}",content);
		    JSONObject obj = JSONObject.parseObject(content);
		    token=obj.getString("token");
		} catch (Exception e) {
			logger.error("MG loginWebSite error",e);
		}
		return token;
	}
	
	
	//MG Member Creation
	private boolean mgMemberCreate(String username,String passwd,String token,String cur,String language){
		//测试环境代码
		if(username.contains(MgConstants.MG_PREFIX)){
			username=username.substring(username.indexOf(MgConstants.PARTNERID), username.length());

		}
		
		logger.info("MG mgMemberCreate  username = {} , passwd = {} , token = {}, cur = {}, language = {}" , username, passwd, token, cur, language);
		try {
		    HttpClient httpClients = HttpClients.createDefault();
		    HttpPut request = new HttpPut(MgConstants.MG_MEMCREATION_URL);
			request.addHeader("X-Requested-With", "X-Api-Client");
			request.addHeader("X-Api-Call", "X-Api-Client");
			request.addHeader("X-Api-Auth", token);  
			request.addHeader("Content-Type", "application/json");
			JSONObject casino=new JSONObject();
			casino.put("enable", true);
			JSONObject poker=new JSONObject();
			poker.put("enable", false);
			JSONObject  json=new JSONObject();
			json.put("crId", MgConstants.CRID);
			json.put("crType", MgConstants.CRTYPE);
			json.put("neId",MgConstants.NEID);
			json.put("neType",MgConstants.NETYPE);
			json.put("tarType",MgConstants.TARTYPE);
			json.put("username",username);
			json.put("name",username);
			json.put("password",passwd);
			json.put("confirmPassword",passwd);
			json.put("currency",cur);
			json.put("language",language);
			json.put("email","");
			json.put("casino",casino);
			json.put("poker", poker);
			StringEntity entity = new StringEntity(json.toJSONString());
			request.setEntity(entity);
			HttpResponse response = httpClients.execute(request);
			String result =EntityUtils.toString(response.getEntity());
			logger.info("MG mgMemberCreate  result={}",result);
			JSONObject obj=JSONObject.parseObject(result);
			if(obj.getBoolean("success")){
				return true;
			}
		} catch (Exception e) {
			logger.error("MG mgMemberCreate  error",e);
		}
		return false;
				
		
	} 
	
	//local Member Creation
	private MgApiUserEntity localMemberCreate(ApiInfoEntity entity,String username,String password,String cur){

		try {
			MgApiUserEntity mgApiUserEntity = new MgApiUserEntity();
			mgApiUserEntity.setUsername(username);
			mgApiUserEntity.setPassword(password);
			mgApiUserEntity.setApiInfoId(entity.getId().intValue());
			mgApiUserEntity.setAgentName(entity.getAgent()); 
			mgApiUserEntity.setUserStatus(1);
			mgApiUserEntity.setSiteId(entity.getSiteId());
			mgApiUserEntity.setSiteName(entity.getProjectAgent());
			mgApiUserEntity.setCurrencyType(cur);
			int insert = mgApiUserEntityMapper.insert(mgApiUserEntity); 
			logger.info("本地创建会员username = {}, result = {}", username, insert);
			return mgApiUserEntity;
		}catch(Exception e){
			logger.error("MG localMemberCreate error",e);
		}
		return null;
	}
	
	
	
	//Deposit  & Withdrawal 
	private boolean transaction(String token,String product,String operation,String amount,String txId){
		logger.info("MG transaction  token = {} , product = {} , operation = {}, amount = {}, txId = {}" ,  token, product, operation, amount, txId);

		try {
			Document document;
			Element root;
			root = new Element("mbrapi-changecredit-call");
			document = new Document(root);
			String UtcTime = (String)TimeUtil.getUtcTime();
			root.setAttribute("timestamp", UtcTime+" UTC");//此为测试，正式必须获取UTC时间
			root.setAttribute("apiusername",MgConstants.API_USERNAME);
			root.setAttribute("apipassword",MgConstants.API_PASSWD);
			root.setAttribute("token", token);
			root.setAttribute("product",product);
			root.setAttribute("operation",operation);
			root.setAttribute("amount",amount);
			root.setAttribute("tx-id",txId);
			
			String xml = MGUtil.doc2String(document);
			String result = MGUtil.sendXml(MgConstants.MG_MEMBER_URL, xml);
			logger.info("MG transaction  result={}",result);
			//创建一个新的字符串
	        StringReader read = new StringReader(result);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            root = doc.getRootElement();
            if(MgConstants.MG_STATE_SUCCESS.equals(root.getAttributeValue("status"))){
            	return true;
            }
		}catch (Exception e) {
			logger.error("MG transaction error",e);
		}
		return false;
		
	} 
	//Launch Game
		private String loginGame(String token,String gameId,String bankingUrl,String lobbyUrl,String logoutRedirectUrl,String demoMode,String language){
			logger.info("MG loginGame  token = {} , gameId = {} , bankingUrl = {}, lobbyUrl  = {}, logoutRedirectUrl = {},demoMode = {}, language = {}" ,  token, gameId, bankingUrl, lobbyUrl, logoutRedirectUrl, demoMode, language );
			String launchUrl=null;
			try{
				Document document;
				Element root;
				root = new Element("mbrapi-launchurl-call");
				document = new Document(root);
				String UtcTime = (String)TimeUtil.getUtcTime();
				root.setAttribute("timestamp", UtcTime+" UTC");//此为测试，正式必须获取UTC时间
				root.setAttribute("apiusername",MgConstants.API_USERNAME);
				root.setAttribute("apipassword",MgConstants.API_PASSWD);
				root.setAttribute("token", token);
				root.setAttribute("language",language);
				root.setAttribute("gameId",gameId);
				root.setAttribute("bankingUrl",bankingUrl);
				root.setAttribute("lobbyUrl",lobbyUrl);
				root.setAttribute("logoutRedirectUrl",logoutRedirectUrl);
				root.setAttribute("demoMode",demoMode=="0"?"true":"false");  //试玩根据MgConstants配置  该值为false
				String xml = MGUtil.doc2String(document);
				String result = MGUtil.sendXml(MgConstants.MG_MEMBER_URL, xml);
				logger.info("MG loginGame  result={}",result);
				//创建一个新的字符串
		        StringReader read = new StringReader(result);
		        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		        InputSource source = new InputSource(read);
		        //创建一个新的SAXBuilder
		        SAXBuilder sb = new SAXBuilder();
	            //通过输入源构造一个Document
	            Document doc = sb.build(source);
	            //取的根元素
	            root = doc.getRootElement();
	            //得到根元素所有子元素的集合
	            String  status = root.getAttributeValue("status");
	            if(MgConstants.MG_STATE_SUCCESS.equals(status)){
	            	launchUrl=root.getAttributeValue("launchUrl");
	            }
			}catch(Exception e){
				logger.error("MG loginGame error",e);
			}
			
			return launchUrl;
			
		}
		
		public static String getRealIp() {
			String localip = null;// 本地IP，如果没有配置外网IP则返回它
			String netip = null;// 外网IP
			Enumeration<NetworkInterface> netInterfaces;
			try {
		    netInterfaces = NetworkInterface.getNetworkInterfaces();
		        InetAddress ip = null;
		        boolean finded = false;// 是否找到外网IP
		        while (netInterfaces.hasMoreElements() && !finded) {
		            NetworkInterface ni = netInterfaces.nextElement();
		            Enumeration<InetAddress> address = ni.getInetAddresses();
		            while (address.hasMoreElements()) {
		                ip = address.nextElement();
		                if (!ip.isSiteLocalAddress() 
		                        && !ip.isLoopbackAddress() 
		                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
		                    netip = ip.getHostAddress();
		                    finded = true;
		                    break;
		                } else if (ip.isSiteLocalAddress() 
		                        && !ip.isLoopbackAddress() 
		                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
		                    localip = ip.getHostAddress();
		                }
		            }
		        }
		        if (netip != null && !"".equals(netip)) {
		            return netip;
		        } else {
		            return localip;
		        }
			} catch (Exception e) {
		       logger.info("get ip error",e);
			}
			return null;
		}

}
