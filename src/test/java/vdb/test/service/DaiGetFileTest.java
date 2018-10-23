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
 * Web服务的单元测试<br>
 * 
 * @author 苏贤明
 * 
 */
public class DaiGetFileTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：获得指定URI的文件信息
	@Test
	public void testDaiGetFile()
	{
		// 请求方式错误测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "daiGetFile");
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
							Element codeElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/head/code");
							if (codeElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
								Assert.assertEquals("400", codeElement
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

		// 传递错误参数的测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "daiGetFile");// 设置服务的名称
			method.setParameter("uri", "localhost.gem/");// 设置文件的URI
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
							Element codeElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/head/code");
							if (codeElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
								Assert.assertEquals("500", codeElement
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

		// 传递正确参数的测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "daiGetFile");// 设置服务的名称
			method.setParameter("uri",
					"localhost.gem/1f628d0728b44b260128b44ed815000b");// 设置文件的URI：数据集URI/文件ID
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
							Element codeElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/head/code");
							if (codeElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
								Assert.assertEquals("200", codeElement
										.getTextTrim());

							// 测试：文件名称不能为空
							Element nameElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/file/name");
							if (nameElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert.assertNotNull("文件名称不应该为空！", nameElement
										.getText());
							}

							// 测试：文件大小不能为空
							Element sizeElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/file/size");
							if (sizeElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert.assertNotNull("文件大小不应该为空", sizeElement
										.getText());
							}

							// 测试：URL地址的正确性
							Element urlElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/file/url");
							if (urlElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert
										.assertEquals(
												"http://localhost:80/files/localhost.gem/1f628d0728b44b260128b44ed815000b.png",
												urlElement.getTextTrim());
							}

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
}
