package vdb.mydb.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class WidgetDAO
{
	public Widget loadWidget(File widgetDir) throws Exception
	{
		Widget widget = new Widget();

		File metaXml = new File(widgetDir, "meta.xml");
		XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource(
				metaXml));
		widget = (Widget) factory.getBean(factory.getBeanDefinitionNames()[0]);
		widget.setWidgetDir(widgetDir);

		// load properties
		Properties prop = new Properties();

		File f = new File(widgetDir, "params.properties");
		if (f.exists())
		{
			InputStream in = new FileInputStream(f);
			prop.load(in);
			in.close();
		}

		widget.setProperties(prop);
		return widget;
	}

	/**
	 * save properties
	 * 
	 * @param widget
	 * @throws Exception
	 */
	public void saveWidget(Widget widget) throws Exception
	{
		File propFile = new File(widget.getWidgetDir(), "params.properties");
		if (!propFile.exists())
		{
			propFile.createNewFile();
		}

		OutputStream out = new FileOutputStream(propFile);
		Properties prop = new Properties();
		prop.putAll(widget.getProperties());
		prop.store(out, null);
		out.close();
	}
}
