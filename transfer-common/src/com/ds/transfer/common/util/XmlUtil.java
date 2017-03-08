package com.ds.transfer.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dom4j + xpath 解析 xml
 * 
 * @author jackson
 *
 */
public class XmlUtil {

	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	private SAXReader reader;

	private Document document;

	/**
	 *  sample : /result/@info <===> <result info="">
	 */
	@SuppressWarnings("unchecked")
	public List<Attribute> getAttribute(String xpath) {
		if (reader == null) {
			throw new NullPointerException("SAXReader");
		}
		return document.selectNodes(xpath);
	}

	/**
	 * sample : /response/errcode <===> <response><errcode>12321</errcode></response> 
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getSelectNodes(String xpath) {
		if (reader == null) {
			throw new NullPointerException("SAXReader");
		}
		return document.selectNodes(xpath);
	}

	public XmlUtil(String xml) {
		reader = new SAXReader();
		try {
			document = reader.read(new BufferedInputStream(new ByteArrayInputStream(xml.trim().getBytes())));
		} catch (DocumentException e) {
			logger.info("解析xml异常 : ", e);
		}
	}

}
