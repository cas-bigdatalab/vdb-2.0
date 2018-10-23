package cn.csdb.commons.marshal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import cn.csdb.commons.util.TimeUtils;

public class XmlMarshaler
{
	public class XmlElement implements InputElement, OutputElement
	{
		private Element _element;

		public XmlElement(Element element)
		{
			_element = element;
		}

		public List<InputElement> getChildren(String name)
		{
			List<InputElement> es = new ArrayList<InputElement>();
			for (Object ee : _element.getChildren(name))
			{
				es.add(new XmlElement((Element) ee));
			}

			return es;
		}

		public List<InputElement> getChildren()
		{
			List<InputElement> es = new ArrayList<InputElement>();
			for (Object ee : _element.getChildren())
			{
				es.add(new XmlElement((Element) ee));
			}

			return es;
		}

		public String getName()
		{
			return _element.getName();
		}

		public OutputElement addChild(String name)
		{
			Element child = new Element(name);
			_element.addContent(child);

			return new XmlElement(child);
		}

		public void read(Marshalable object)
		{
			object.read(this);
		}

		public OutputElement write(Marshalable object)
		{
			object.write(this);
			return this;
		}

		public InputElement getChild(String name)
		{
			return new XmlElement(_element.getChild(name));
		}

		public String getText()
		{
			return _element.getTextNormalize();
		}

		public void setText(String value)
		{
			_element.setText(value);
		}
	}

	private File _xmlFile;

	public XmlMarshaler(File xmlFile)
	{
		_xmlFile = xmlFile;
	}

	/**
	 * write
	 * 
	 * @param rootName -
	 *            root element name
	 * @param object -
	 *            marshalable object to be written
	 * @throws Exception
	 */
	public void write(String rootName, Marshalable object, String comment)
			throws Exception
	{
		Format format = Format.getPrettyFormat();
		format.setEncoding("gbk");
		format.setIndent("\t");
		XMLOutputter out = new XMLOutputter(format);
		Document document = new Document();
		if (comment != null)
		{
			Comment ce = new Comment(comment);
			document.addContent(ce);
		}
		Element root = new Element(rootName);
		document.setRootElement(root);
		object.write(new XmlElement(root));

		out.output(document, new FileOutputStream(_xmlFile));
	}

	public void write(String rootName, Marshalable object) throws Exception
	{
		write(rootName, object, "created by XmlMarshaler at "
				+ TimeUtils.getNowString() + ".");
	}

	public void read(Marshalable object) throws Exception
	{
		SAXBuilder parser = new SAXBuilder();
		Document document;
		document = parser.build(new FileInputStream(_xmlFile));

		Element root = document.getRootElement();
		object.read(new XmlElement(root));
	}
}
