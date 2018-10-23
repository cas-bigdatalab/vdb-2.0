package vdb.metacat.fs.io;

import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Element;

import vdb.metacat.meta.MetaData;
import cn.csdb.commons.util.StringKeyMap;

public class ElementWriter
{
	private Element _element;

	public ElementWriter(Element e)
	{
		_element = e;
	}

	public Element appendChild(String name)
	{
		Element child = new Element(name);
		_element.addContent(child);

		return child;
	}

	public void writeAttribute(String name, String value)
	{
		if (value == null)
			return;

		Element child = new Element(name);
		child.setText(value);
		_element.addContent(child);
	}

	public void writeAttributes(MetaData meta)
	{
		// 写入属性
		meta.getId();
		meta.getLastModified();

		Map<String, String> props = meta.map();

		for (Entry<String, String> me : props.entrySet())
		{
			String value = me.getValue();
			if (value != null)
			{
				String name = me.getKey();
				writeAttribute(name, value);
			}
		}
	}

	public void writeAttributes(MetaData meta, String... ignoredNames)
	{
		// 写入属性
		meta.getId();
		meta.getLastModified();

		Map<String, String> props = meta.map();
		Map<String, String> blackList = new StringKeyMap<String>();
		for (String ignoreName : ignoredNames)
		{
			blackList.put(ignoreName, null);
		}

		for (Entry<String, String> me : props.entrySet())
		{
			String value = me.getValue();
			if (value != null)
			{
				String name = me.getKey();
				if (blackList.containsKey(name))
					continue;

				writeAttribute(name, value);
			}
		}
	}
}