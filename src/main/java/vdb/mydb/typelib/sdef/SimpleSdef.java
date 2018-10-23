package vdb.mydb.typelib.sdef;

import java.io.StringReader;
import java.util.Date;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

//FIXME: use dom4j, instead of jdom, which handle text values improperly

public class SimpleSdef extends SimpleSdefNode implements Sdef
{
	public SimpleSdef()
	{
		super(new Element("sdef"));
	}

	public SimpleSdef(String value)
	{
		this(value, null);
	}

	public SimpleSdef(Date value)
	{
		this("" + value.getTime(), null);
	}

	public SimpleSdef(Number value)
	{
		this("" + value, null);
	}

	public SimpleSdef(String value, String title)
	{
		this();

		setValue(value);
		setTitle(title);
	}

	public String getXml()
	{
		return new XMLOutputter(Format.getPrettyFormat())
				.outputString(_element);
	}

	public void setXml(String xml) throws Exception
	{
		SAXBuilder parser = new SAXBuilder();
		_element = parser.build(new StringReader(xml)).getRootElement();
	}

	private void setChildValue(String nodeName, String nodeValue)
	{
		Element e = _element.getChild(nodeName);
		if (e == null)
		{
			e = new Element(nodeName);
			_element.addContent(e);
		}

		e.setText(nodeValue);
	}

	public String getValue()
	{
		return getChildValue("value");
	}

	public void setValue(String value)
	{
		setChildValue("value", value);
	}

	public String getTitle()
	{
		return getChildValue("title");
	}

	public void setTitle(String title)
	{
		setChildValue("title", title);
	}
}
