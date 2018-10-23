package vdb.metacat.fs.io;

import java.io.File;
import java.io.FileOutputStream;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlWriter
{
	private String _comment;

	private File _xml;

	public XmlWriter(File xml)
	{
		_xml = xml;
	}

	public void setComment(String comment)
	{
		_comment = comment;
	}

	public void write(Element rootElement) throws Exception
	{
		_xml.getParentFile().mkdirs();

		Format format = Format.getPrettyFormat();
		format.setEncoding("gbk");
		format.setIndent("\t");
		XMLOutputter out = new XMLOutputter(format);
		Document document = new Document();
		if (_comment != null)
		{
			Comment ce = new Comment(_comment);
			document.addContent(ce);
		}

		document.setRootElement(rootElement);
		FileOutputStream fos = new FileOutputStream(_xml);
		out.output(document, fos);
		fos.close();
	}
}
