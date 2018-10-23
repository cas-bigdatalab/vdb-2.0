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
 */
@SuppressWarnings("unchecked")
public class SysGetEntityListTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：收割指定数据集中的实体信息
	@Test
	public void testSysGetEntityList()
	{
		// 请求方式错误测试
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "sysGetEntityList");
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
			method.setParameter("verb", "sysGetEntityList");// 设置服务的名称
			method.setParameter("uri", "localhost");// 设置数据集的URI，这里传递的错误的URI
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
			method.setParameter("verb", "sysGetEntityList");// 设置服务的名称
			method.setParameter("uri", "localhost.gem");// 设置数据集实体的URI
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

							Element entitiesElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/entities");
							if (entitiesElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								List<Element> entityElementList = (List<Element>) XPath
										.selectNodes(rootElement,
												"/response/body/entities/entity");
								if (entityElementList == null
										&& entityElementList.size() == 0)
									Assert.fail("错误，本地域中至少存在CMS数据集");
								{
									/**
									 * TODO 待修改处 需要根据本地域中的数据集个数进行相应的修改
									 * 也可以在此添加别的判断，如判断本地数据集中某个数据量统计指标是否正确等
									 */
									Assert.assertEquals(7, entityElementList
											.size());
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
