package com.ds.transfer.mg.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 只适应于MG接口
 *  格式
 * 项目名称：kg-test   
 * 类名称：MGUtil   
 * 类描述：   
 * 创建人：光光   
 * 创建时间：2016-7-16 下午1:22:47   
 * 修改人：光光   
 * 修改时间：2016-7-16 下午1:22:47   
 * 修改备注：   
 * @version    
 *
 */
public class MGUtil {
	/**
	 * xml的格式必须是<xxxx name1="value1" name2="value2"/>
	 * @param xml
	 * @param name
	 * @return
	 */
	public static String getXmlValue(String xmlStr,String name){
		java.io.Reader in = new StringReader(xmlStr);     
        try {
			Document doc = (new SAXBuilder()).build(in);
			Element root = doc.getRootElement();
			return root.getAttributeValue(name);
		} catch (JDOMException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}       
        
	}
	public static String doc2String(Document doc) throws Exception {     
        Format format = Format.getPrettyFormat();     
        format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题     
        XMLOutputter xmlout = new XMLOutputter(format);     
        ByteArrayOutputStream bo = new ByteArrayOutputStream();     
        xmlout.output(doc, bo);
        
        return bo.toString();     
    }
	public static String sendXml(String url,String param){
		HttpClient httpClients =  HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
	    HttpResponse response=null;
	    try {
			request.addHeader("Content-Type",  ContentType.APPLICATION_XML.toString());
			StringEntity params =new StringEntity(param);
			request.setEntity(params);
			response = httpClients.execute(request);
			return EntityUtils.toString(response.getEntity());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return null;
	}
}
