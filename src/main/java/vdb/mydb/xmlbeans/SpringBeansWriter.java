package vdb.mydb.xmlbeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class SpringBeansWriter
{
	private File _xmlFile;

	public SpringBeansWriter(File xmlFile)
	{
		_xmlFile = xmlFile;
	}

	public void write(SpringBeans beans, BeanWritterFactory factory)
			throws Exception
	{
		Document document = new Document();
		Element beansElement = new Element("beans");
		document.addContent(new DocType("beans", "-//SPRING//DTD BEAN 2.0//EN",
				"http://www.springframework.org/dtd/spring-beans-2.0.dtd"));
		document.setRootElement(beansElement);
		writeBeans(beans, factory, beansElement);

		if (!_xmlFile.exists())
			_xmlFile.createNewFile();

		XMLOutputter xo = new XMLOutputter(org.jdom.output.Format
				.getPrettyFormat().setIndent("\t"));
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(_xmlFile), "UTF-8");
		xo.output(document, writer);
		writer.close();
	}

	private void writeBeans(SpringBeans beans, BeanWritterFactory factory,
			Element beansElement) throws NoBeanWritterFoundException
	{
		for (Object bean : beans.getBeans())
		{
			BeanWriterContext bwc = new BeanWriterContext(beans, factory);
			BeanWriter beanWriter = factory.getBeanWritter(bean);

			beanWriter.write(bean, beansElement, bwc);
		}
	}
}
