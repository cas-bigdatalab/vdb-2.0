package vdb.mydb.widget;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import vdb.mydb.VdbManager;
import vdb.mydb.xmlbeans.BeanWriter;
import vdb.mydb.xmlbeans.BeanWritterFactory;
import vdb.mydb.xmlbeans.SpringBeans;
import vdb.mydb.xmlbeans.SpringBeansWriter;
import cn.csdb.commons.util.FileUtils;

public class WidgetLayoutDAO
{
	LayoutPatternManager _layoutPatternManager;

	private void assertFileExists(String fileName) throws IOException
	{
		File file = new File(fileName);
		if (!file.exists())
		{
			FileUtils
					.copy(
							new File(
									VdbManager
											.getEngine()
											.getServletContext()
											.getRealPath(
													"/console/webpub/WEB-INF/profile/example.xml")),
							file);
		}
	}

	public LayoutPatternManager getLayoutPatternManager()
	{
		return _layoutPatternManager;
	}

	public WidgetLayout loadWidgetLayout(String name) throws Exception
	{
		String fileName = VdbManager
				.getEngine()
				.getServletContext()
				.getRealPath("/console/webpub/WEB-INF/profile/" + name + ".xml");

		assertFileExists(fileName);
		XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource(
				fileName));
		WidgetLayout widgetLayout = (WidgetLayout) factory
				.getBean("vdb.mydb.widget.WidgetLayout");

		// set layout
		widgetLayout.setLayoutPattern(_layoutPatternManager
				.getLayoutPattern(widgetLayout.getLayout()));

		return widgetLayout;
	}

	public void saveWidgetLayout(WidgetLayout widgetLayout) throws Exception
	{
		File xmlFile = new File(VdbManager.getEngine().getServletContext()
				.getRealPath("/console/webpub/WEB-INF/profile")
				+ "/" + widgetLayout.getName() + ".xml");

		SpringBeans beans = new SpringBeans();
		beans.addBean(widgetLayout);

		BeanWritterFactory factory = new BeanWritterFactory();
		factory.registerBeanWriter(WidgetLayout.class, new BeanWriter("layout",
				"columns"));

		new SpringBeansWriter(xmlFile).write(beans, factory);
	}

	public void setLayoutPatternManager(
			LayoutPatternManager layoutPatternManager)
	{
		_layoutPatternManager = layoutPatternManager;
	}
}
