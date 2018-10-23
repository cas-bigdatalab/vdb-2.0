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
 * Web服务的单元测试<br>
 * 
 * @author 苏贤明
 * 
 */
@SuppressWarnings("unchecked")
public class DhGetDatabaseSchemaTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：获得指定数据集的Schema信息，即Schema文件内容的子集
	@Test
	public void testDhGetDatabaseSchema()
	{
		// 请求方式错误测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "dhGetDatabaseSchema");
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
			method.setParameter("verb", "dhGetDatabaseSchema");// 设置服务的名称
			method.setParameter("uri", "localhost");// 设置数据集的URI，这里传递进去的是错误的URI
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
			method.setParameter("verb", "dhGetDatabaseSchema");// 设置服务的名称
			method.setParameter("uri", "localhost.gem");// 设置数据集的URI
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

							Element databaseElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/schema/database");
							if (databaseElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert.assertEquals("localhost.gem",
										databaseElement.getChild("uri")
												.getTextTrim());
								Assert.assertEquals("localhost.gem",
										databaseElement.getChild("name")
												.getTextTrim());
								Assert.assertEquals("全球经济监测项目", databaseElement
										.getChild("title").getTextTrim());

								List<Element> entityElementList = databaseElement
										.getChildren("entity");
								Assert.assertNotNull(entityElementList);
								Assert
										.assertEquals(7, entityElementList
												.size());

								List<Element> viewElementList = databaseElement
										.getChildren("view");
								Assert.assertNotNull(viewElementList);
								Assert.assertEquals(4, viewElementList.size());

								// 可以对部分信息进行详细比对
								Assert.assertEquals(
										"localhost.gem.indicatorbasicinfo",
										entityElementList.get(0)
												.getChild("uri").getTextTrim());
								Assert
										.assertEquals(
												"localhost.gem.indicatordataintegrationinfo",
												entityElementList.get(1)
														.getChild("uri")
														.getTextTrim());
								Assert.assertEquals(
										"localhost.gem.indicatordatatypes",
										entityElementList.get(2)
												.getChild("uri").getTextTrim());
								Assert.assertEquals(
										"localhost.gem.indicatorgroup",
										entityElementList.get(3)
												.getChild("uri").getTextTrim());
								Assert.assertEquals(
										"localhost.gem.indicatorquarterbase",
										entityElementList.get(4)
												.getChild("uri").getTextTrim());
								Assert.assertEquals(
										"localhost.gem.providerinfo",
										entityElementList.get(5)
												.getChild("uri").getTextTrim());
								Assert.assertEquals("localhost.gem.regioninfo",
										entityElementList.get(6)
												.getChild("uri").getTextTrim());

								Assert.assertEquals("listEntities",
										viewElementList.get(0).getChild("name")
												.getTextTrim());
								Assert.assertEquals(7, viewElementList.get(0)
										.getChildren("item").size());

								Assert.assertEquals("editEntities",
										viewElementList.get(1).getChild("name")
												.getTextTrim());
								Assert.assertEquals(7, viewElementList.get(1)
										.getChildren("item").size());

								Assert.assertEquals("queryEntity",
										viewElementList.get(2).getChild("name")
												.getTextTrim());
								Assert.assertEquals(8, viewElementList.get(2)
										.getChildren("item").size());

								Assert.assertEquals("indexEntity",
										viewElementList.get(3).getChild("name")
												.getTextTrim());
								Assert.assertEquals(5, viewElementList.get(3)
										.getChildren("item").size());
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
