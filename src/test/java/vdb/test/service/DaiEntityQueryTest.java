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
public class DaiEntityQueryTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：根据CQL查询语句得到相应的记录
	@Test
	public void testDaiEntityQuery()
	{
		// 请求方式错误测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "daiEntityQuery");
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
			method.setParameter("verb", "daiEntityQuery");// 设置服务的名称
			method.setParameter("uri", "localhost.gem/");// 设置实体的URI
			method.setParameter("cql", "testCql");// CQL的写法应该为：字段URI 操作符 值
													// [and 字段URI 操作符 值]
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
			method.setParameter("verb", "daiEntityQuery");// 设置服务的名称
			method.setParameter("uri", "localhost.gem.indicatorbasicinfo");// 设置实体的URI
			method.setParameter("cql", "localhost.gem.indicatorbasicinfo.id=1");// CQL的写法应该为：字段URI
																				// 操作符
																				// 值
																				// [and
																				// 字段URI
																				// 操作符
																				// 值]
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

							Element itemsElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/record/items");
							if (itemsElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								List<Element> itemElementList = (List<Element>) XPath
										.selectNodes(rootElement,
												"/response/body/record/items/item");
								if (itemElementList != null
										&& itemElementList.size() != 0)
								{
									Assert.assertEquals(1, itemElementList
											.size());
									Assert.assertNotNull("文件名称不应该为空！",
											itemElementList.get(0).getText());
									Assert
											.assertEquals(
													"localhost.gem.indicatorbasicinfo/1",
													itemElementList.get(0)
															.getTextTrim());
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
