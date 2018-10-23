package vdb.test.service.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class ServiceUtil
{

	// 获得Web服务返回的字符串
	public static String getReturnString(HttpClient client, PostMethod method)
	{

		String returnStr = "";

		try
		{
			client.executeMethod(method);
		}
		catch (HttpException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			returnStr = method.getResponseBodyAsString();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			// 使用完成后要释放链接
			method.releaseConnection();
		}

		return returnStr;
	}

	// 解析字符串为Doc文档
	public static Document getDocFromStr(String xmlString)
	{
		StringReader reader = new StringReader(xmlString);
		InputSource source = new InputSource(reader);
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Document doc = builder.build(source);
			return doc;
		}
		catch (JDOMException jex)
		{
			jex.printStackTrace();
			return null;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}
