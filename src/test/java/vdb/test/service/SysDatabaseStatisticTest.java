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
public class SysDatabaseStatisticTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：收割科学数据库中各个数据集的数量量信息
	@Test
	public void testSysDatabaseStatistic()
	{
		// 此服务不需要传递任何参数
		// 注释掉一种情况，即没有任何数据的情况
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "sysDatabaseStatistic");// 设置服务的名称
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

							Element databaseStElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/databaseStatistic");
							if (databaseStElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								List<Element> databaseElementList = (List<Element>) XPath
										.selectNodes(rootElement,
												"/response/body/databaseStatistic/database");
								if (databaseElementList == null
										&& databaseElementList.size() == 0)
									Assert.fail("错误，本地域中至少存在CMS数据集");
								{
									/**
									 * TODO 待修改处 需要根据本地域中的数据集个数进行相应的修改
									 * 也可以在此添加别的判断，如判断本地数据集中某个数据量统计指标是否正确等
									 */
									Assert.assertEquals(3, databaseElementList
											.size());
									Assert.assertEquals("cms",
											databaseElementList.get(0)
													.getChild("uri")
													.getTextTrim());
									Assert.assertEquals("localhost.gem",
											databaseElementList.get(1)
													.getChild("uri")
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
