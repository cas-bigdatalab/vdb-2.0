package vdb.metacat.fs.io;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import cn.csdb.commons.util.Matcher;
import cn.csdb.commons.util.StringKeyMap;

public class ElementReader
{
	private Element _element;

	public ElementReader(Element e)
	{
		_element = e;
	}

	public int getVersion()
	{
		String version = _element.getChildTextNormalize("version");
		if (version != null)
		{
			int vs[] = { 0, 0, 0 };
			int i = 0;
			StringTokenizer st = new StringTokenizer(version);
			while (st.hasMoreTokens())
			{
				String s = st.nextToken();
				try
				{
					vs[i] = Integer.parseInt(s);
				}
				catch (Throwable e)
				{
				}

				i++;
				if (i == 2)
					break;
			}

			return (vs[0] * 100 + vs[1]) * 100 + vs[2];
		}

		return -1;
	}

	public String readAttribute(String name)
	{
		Element e = _element.getChild(name);
		if (e == null)
			return null;

		return e.getTextNormalize();
	}

	public Map<String, String> readAttributes()
	{
		return readAttributes(new String[0]);
	}

	public Map<String, String> readAttributes(
			Matcher<Element> childElementMatcher)
	{
		Map<String, String> props = new StringKeyMap<String>();

		List<Element> elements = _element.getChildren();
		for (Element element : elements)
		{
			try
			{
				if (element.getChildren().size() > 0)
					continue;

				if (childElementMatcher != null
						&& childElementMatcher.matches(element))
				{
					String nodeName = element.getName();
					props.put(nodeName, element.getTextNormalize());
				}
			}
			catch (Exception e)
			{
				continue;
			}
		}

		return props;
	}

	public Map<String, String> readAttributes(String... ignoredNames)
	{
		return readAttributes(new IgnoredNodeMatcher(ignoredNames));
	}

	public Element selectChild(String path) throws JDOMException
	{
		return (Element) XPath.selectSingleNode(_element, path);
	}

	public List<Element> selectChildren(String path) throws JDOMException
	{
		return XPath.selectNodes(_element, path);
	}
}
