/*
 * 创建日期 2005-5-9
 */
package cn.csdb.commons.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * @author bluejoe
 */
public class JDomUtils
{

	/**
	 * 递归获取dom节点
	 */
	public static Element getRecursiveNode(Document document,
			String abstractPath)
	{
		int depth = 0;
		// 分解路径
		StringTokenizer st = new StringTokenizer(abstractPath, "/");
		Element e = null;

		while (st.hasMoreTokens())
		{
			String nodeName = st.nextToken();
			if (nodeName.length() == 0)
				continue;

			// 根结点
			if (depth == 0)
			{
				if (!document.hasRootElement())
				{
					e = new Element(nodeName);
					document.setRootElement(e);
				}
				else
				{
					e = document.getRootElement();
				}
			}
			else
			{
				Element pe = e;
				e = pe.getChild(nodeName);
				// 判断该节点是否存在
				if (e == null)
				{
					e = new Element(nodeName);
					pe.addContent(e);
				}
			}

			depth++;
		}

		return e;
	}

	public static Map loadProperties(Element pe)
	{
		return loadProperties(pe, 0);
	}

	/**
	 * 将所有属性和元素值导入Map
	 * 
	 * @param pe
	 * @param caseSwitch
	 *            大小写模式：0-保留大小写 1-全小写 2-全大写
	 * @return
	 */
	public static Map loadProperties(Element pe, int caseSwitch)
	{
		Map props = new Properties();

		// 属性
		List attributes = pe.getAttributes();
		for (int i = 0; i < attributes.size(); i++)
		{
			try
			{
				Attribute attribute = (Attribute) attributes.get(i);
				props.put(caseSwitch == 0 ? attribute.getName()
						: (caseSwitch == 1 ? attribute.getName().toLowerCase()
								: attribute.getName().toUpperCase()), attribute
						.getValue());
			}
			catch (Exception e)
			{
				continue;
			}
		}

		// 元素
		List elements = pe.getChildren();
		for (int i = 0; i < elements.size(); i++)
		{
			try
			{
				Element element = (Element) elements.get(i);

				if (element.getAttributes().size() > 0)
					continue;

				if (element.getChildren().size() > 0)
					continue;

				String nodeName = element.getName();

				props.put(caseSwitch == 0 ? nodeName
						: (caseSwitch == 1 ? nodeName.toLowerCase() : nodeName
								.toUpperCase()), element.getTextNormalize());
			}
			catch (Exception e)
			{
				continue;
			}
		}

		return props;
	}
}
