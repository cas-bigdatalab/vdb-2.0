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
public class DefaultTest
{

	private static final String SERVICE_URL = "http://localhost/service";

	// 测试服务
	// 服务功能：缺省服务，用来显示VDB2.0提供的所有服务的名称、服务器启动时间、联系人的名字、电话及邮箱等信息
	@Test
	public void testDefault()
	{
		// 此服务不需要传递任何参数
		{
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(SERVICE_URL);
			method.setParameter("verb", "default");// 设置服务的名称
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

							// 测试四个属性：版本属性、语言属性、支持服务动词属性、服务器启动时间属性
							Element versionElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/sysInfo/version");
							Element languageElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/sysInfo/language");
							Element supportedVerbsElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/sysInfo/supportedVerbs");
							Element startupTimeElement = (Element) XPath
									.selectSingleNode(rootElement,
											"/response/body/sysInfo/startupTime");

							if (versionElement == null
									|| languageElement == null
									|| supportedVerbsElement == null
									|| startupTimeElement == null)
								Assert.fail("请求返回的字符串不符合指定的XML文档格式");
							else
							{
								Assert.assertEquals("1.0", versionElement
										.getTextTrim());
								Assert.assertEquals("VisualDB 2.0",
										languageElement.getTextTrim());

								String verbs = "dhGetDatabaseList,dhGetDatabaseSchema,dhGetIndexes,dhGetIndexModifications,"
										+ "daiGetRecord,uiGetRecord,daiQuery,daiEntityQuery,uiQuery,daiGetFile,dhGetCatalog,"
										+ "(default),sysDatabaseStatistic,sysGetEntityList,sysStatus";
								Assert.assertEquals(verbs,
										supportedVerbsElement.getTextTrim());
								Assert.assertNotNull("实体URI和记录ID不应该为空！",
										startupTimeElement.getText());

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
