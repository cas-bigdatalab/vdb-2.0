package vdb.test.service;

import java.util.List;

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
@SuppressWarnings("unchecked")
public class DaiGetRecordTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：根据实体的URI和记录的ID查找某条记录的详细信息（对应的索引视图下的详细信息，即只会显示索引视图下的字段列表）
	@Test
	public void testDaiGetRecord()
	{
		// 请求方式错误测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "daiGetRecord");
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
			method.setParameter("verb", "daiGetRecord");// 设置服务的名称
			method.setParameter("uri", "localhost.gem");// 设置实体记录的URI，格式为：实体的URI/记录ID
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
			method.setParameter("verb", "daiGetRecord");// 设置服务的名称
			method.setParameter("uri", "localhost.gem.indicatorbasicinfo/0");// 设置实体记录的URI，格式为：实体的URI/记录ID
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

							// 验证URL的正确性
							Element urlElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/record/url");
							if (urlElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
								Assert
										.assertEquals(
												"http://localhost:80/page/showItem.vpage?id=localhost.gem.indicatorbasicinfo/0",
												urlElement.getTextTrim());

							// 验证字段的正确性
							Element fieldsElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/record/fields");
							if (fieldsElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								List<Element> fieldElementList = (List<Element>) XPath
										.selectNodes(rootElement,
												"/response/body/record/fields/field");
								if (fieldElementList != null
										&& fieldElementList.size() != 0)
								{
									Element fieldUriElement, nameElement, typeElement;
									for (Element fieldElement : fieldElementList)
									{
										// 直接验证URI、name以及TYPE等不太好验证，字段列表容易发生变化
										fieldUriElement = fieldElement
												.getChild("uri");
										nameElement = fieldElement
												.getChild("name");
										typeElement = fieldElement
												.getChild("type");
										Assert.assertNotNull("字段URL不应该为空！",
												fieldUriElement.getText());
										Assert.assertNotNull("字段name不应该为空！",
												nameElement.getText());
										Assert.assertNotNull("字段类型不应该为空！",
												typeElement.getText());
									}
								}
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
