package vdb.mydb.widget;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import vdb.mydb.VdbManager;
import vdb.mydb.xmlbeans.BeanWriter;
import vdb.mydb.xmlbeans.BeanWritterFactory;
import vdb.mydb.xmlbeans.SpringBeans;
import vdb.mydb.xmlbeans.SpringBeansWriter;
import cn.csdb.commons.util.ListMap;

public class WidgetFactory implements InitializingBean
{
	File _widgetDir;

	ListMap<String, Widget> _widgets;

	public File getWidgetDir()
	{
		return _widgetDir;
	}

	public void setWidgetDir(File widgetDir)
	{
		_widgetDir = widgetDir;
	}

	public Widget getWidget(String name)
	{
		return _widgets.map().get(name);
	}

	public void loadWidgets() throws Exception
	{
		List<Widget> widgets = new ArrayList<Widget>();

		for (File widgetDir : _widgetDir.listFiles())
		{
			String name = widgetDir.getName();
			if (name.startsWith("#"))
				continue;

			if (name.startsWith("_"))
				continue;

			if (name.startsWith("."))
				continue;

			File metaXml = new File(widgetDir, "meta.xml");
			if (metaXml.exists())
			{
				Widget widget = new WidgetDAO().loadWidget(widgetDir);
				widget.setName(name);
				widgets.add(widget);

				Logger.getLogger(this.getClass()).debug(
						String.format("loading widget: %s(%s)", name, widget
								.getTitle()));
			}
		}

		Collections.sort(widgets, new Comparator<Widget>()
		{
			public int compare(Widget o1, Widget o2)
			{
				return (Collator.getInstance(Locale.CHINA)).compare(o1
						.getTitle(), o2.getTitle());
			}
		});

		_widgets = new ListMap<String, Widget>();
		for (Widget widget : widgets)
		{
			_widgets.add(widget.getName(), widget);
		}
	}

	public void afterPropertiesSet() throws Exception
	{
		loadWidgets();
	}

	public List<Widget> getWidgets()
	{
		return _widgets.list();
	}

	public void removeWidget(String widgetName)
	{
		_widgets.remove(widgetName);
	}

	public void copyWidget(Widget widget) throws Exception
	{
		saveWidget(widget);
		loadWidgets();
	}

	public void saveWidget(Widget widget) throws Exception
	{
		File xmlFile = new File(VdbManager.getEngine().getServletContext()
				.getRealPath("/console/webpub/WEB-INF/widgets")
				+ "/" + widget.getName() + "/meta.xml");

		SpringBeans beans = new SpringBeans();
		beans.addBean(widget);
		BeanWritterFactory factory = new BeanWritterFactory();
		factory.registerBeanWriter(Widget.class, new BeanWriter("title"));
		new SpringBeansWriter(xmlFile).write(beans, factory);
	}
}
