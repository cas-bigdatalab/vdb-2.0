package vdb.mydb.typelib.sdef;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.xpath.XPath;

public class SimpleSdefNode implements SdefNode
{
	Element _element;

	public SimpleSdefNode(Element e)
	{
		_element = e;
	}

	public String getNodeValue()
	{
		return _element.getTextNormalize();
	}

	public SdefNode getChild(String nodeName)
	{
		Element e = _element.getChild(nodeName);
		if (e == null)
		{
			return null;
		}

		return new SimpleSdefNode(e);
	}

	public List<SdefNode> getChildren(String nodeName)
	{
		List<SdefNode> nodes = new ArrayList<SdefNode>();
		for (Element e : (List<Element>) _element.getChildren(nodeName))
		{
			nodes.add(new SimpleSdefNode(e));
		}

		return nodes;
	}

	public SdefNode addChild(String nodeName)
	{
		Element e = new Element(nodeName);
		_element.addContent(e);

		return new SimpleSdefNode(e);
	}

	public void addChild(String nodeName, String nodeValue)
	{
		addChild(nodeName).setNodeValue(nodeValue);
	}

	public void setNodeValue(String nodeValue)
	{
		_element.setText(nodeValue);
	}

	public String getChildValue(String nodeName)
	{
		Element e = _element.getChild(nodeName);
		if (e != null)
			return e.getTextNormalize();

		return null;
	}

	public SdefNode selectSingleNode(String path)
	{
		try
		{
			XPath xpath = XPath.newInstance(path);
			Element e = (Element) xpath.selectSingleNode(_element);
			if (e == null)
			{
				return null;
			}

			return new SimpleSdefNode(e);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public List<SdefNode> selectNodes(String path)
	{
		List<SdefNode> nodes = new ArrayList<SdefNode>();
		try
		{
			XPath xpath = XPath.newInstance(path);
			for (Element e : (List<Element>) xpath.selectNodes(_element))
			{
				nodes.add(new SimpleSdefNode(e));
			}
		}
		catch (Exception e)
		{
		}

		return nodes;
	}
}
