package vdb.test.service;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.junit.Assert;
import org.junit.Test;

import vdb.test.service.util.ServiceUtil;

/**
 * Web服务单元测试
 * 
 * @author 苏贤明
 * 
 */
public class UnknownVerbTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试不可识别的服务名
	@Test
	public void testUnknownVerb()
	{
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(SERVICE_URL);
		method.setParameter("verb", "testWrongVerb");

		String returnStr = ServiceUtil.getReturnString(client, method);
		if (returnStr.equals(""))
			Assert.fail("执行getReturnString()方法时出现异常，请确保方法的正确性");
		else
		{
			Document doc = ServiceUtil.getDocFromStr(returnStr);
			if (doc == null)
				Assert.fail("执行getDocFromStr()方法时出现异常，请求返回的字符串不符合XML文档格式");
			else
			{
				Element rootElement = doc.getRootElement();
				if (rootElement == null
						|| !rootElement.getName().equals("response"))
					Assert.fail("请求返回的字符串不符合指定的XML文档格式");
				else
				{
					try
					{
						Element codeElement = (Element) XPath.selectSingleNode(
								rootElement, "/response/head/code");
						if (codeElement == null)
							Assert.fail("请求返回的字符串不符合指定的XML文档格式");
						else
							Assert.assertEquals("402", codeElement
									.getTextTrim());
					}
					catch (JDOMException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}
