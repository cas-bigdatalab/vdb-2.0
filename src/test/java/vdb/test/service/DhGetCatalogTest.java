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
public class DhGetCatalogTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：得到配置好的域、数据集以及实体等信息，且呈现出层次关系
	@Test
	public void testDhGetCatalog()
	{
		// 此服务不需要传递任何参数
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "dhGetCatalog");// 设置服务的名称
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

							Element rElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/conceptTree/root");
							if (rElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert.assertEquals("", rElement
										.getAttributeValue("uri"));
								Assert.assertEquals("http://localhost:80",
										rElement.getAttributeValue("url"));
								Assert.assertEquals("cn.csdb", rElement
										.getAttributeValue("name"));

								List<Element> nodeElementList = (List<Element>) XPath
										.selectNodes(rootElement,
												"/response/body/conceptTree/root/node");
								if (nodeElementList != null
										&& nodeElementList.size() != 0)
								{
									for (Element nodeElement : nodeElementList)
									{
										Assert.assertEquals("", nodeElement
												.getAttributeValue("url"));
										Assert
												.assertNotNull(
														"数据集URI不应该为空！",
														nodeElement
																.getAttributeValue("uri"));
										Assert
												.assertNotNull(
														"数据集name不应该为空！",
														nodeElement
																.getAttributeValue("name"));

										List<Element> leafElementList = nodeElement
												.getChildren();
										for (Element leafElement : leafElementList)
										{
											Assert.assertEquals("leaf",
													leafElement.getName());
											Assert
													.assertNotNull(
															"实体URI不应该为空！",
															leafElement
																	.getAttributeValue("uri"));
											Assert
													.assertNotSame(
															"",
															leafElement
																	.getAttributeValue("uri"));

											Assert
													.assertNotNull(
															"实体URL不应该为空！",
															leafElement
																	.getAttributeValue("url"));
											Assert
													.assertNotSame(
															"",
															leafElement
																	.getAttributeValue("url"));
										}
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
