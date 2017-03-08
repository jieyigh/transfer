package com.ds.transfer.mg.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class JDomDemo {

    /**   
     * 字符串转换为DOCUMENT   
     *    
     * @param xmlStr 字符串   
     * @return doc JDOM的Document   
     * @throws Exception   
     */    
    public static Document string2Doc(String xmlStr) throws Exception {     
        java.io.Reader in = new StringReader(xmlStr);     
        Document doc = (new SAXBuilder()).build(in);            
        return doc;     
    }     
    
    /**   
     * Document转换为字符串   
     *    
     * @param xmlFilePath XML文件路径   
     * @return xmlStr 字符串   
     * @throws Exception   
     */    
    public static String doc2String(Document doc) throws Exception {     
        Format format = Format.getPrettyFormat();     
        format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题     
        XMLOutputter xmlout = new XMLOutputter(format);     
        ByteArrayOutputStream bo = new ByteArrayOutputStream();     
        xmlout.output(doc, bo);
        
        return bo.toString();     
    }   

	public static void main(String[] args) throws Exception {
		String xml = "<mbrapi-login-call " + 
                "timestamp=\"2012-11-26 12:28:22.965 UTC\" " + 
                "apiusername=\"apiadmin\" "+ 
                "apipassword=\"apipassword\" " + 
                "username=\"rrrrr\" "+  
                "password=\"ttttt\" "+  
                "ipaddress=\"192.168.77.239\"" + 
            "/>"; 
		Document d = string2Doc(xml);
		String str = doc2String(d);
	//	System.out.println(str);
		
		System.out.println(doc2String(createXml()));
		
	}

	
	public  static Document createXml() {
		Document document;
		Element root;
		root = new Element("mbrapi-login-call");
		document = new Document(root);
		root.setAttribute("apiusername", "ddddd");
		root.setAttribute("apipassword", "ddddd");
		
//		
//		Element employee = new Element("employee");
//		root.addContent(employee);
//		Element name = new Element("name");
//		name.setText("ddvip");
//		employee.addContent(name);
//		Element sex = new Element("sex");
//		sex.setText("m");
//		employee.addContent(sex);
//		Element age = new Element("age");
//		age.setText("23");
//		employee.addContent(age);
		return document;

	} 
}
