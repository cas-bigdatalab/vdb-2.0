package vdb.tool.webpub;

import java.io.File;

import vdb.mydb.VdbManager;
import vdb.mydb.widget.LayoutPatternManager;
import vdb.mydb.widget.Widget;
import vdb.mydb.widget.WidgetFactory;
import vdb.mydb.widget.WidgetLayoutDAO;

public class WidgetTool
{
	public LayoutPatternManager getLayoutPatternManager()
	{
		return (LayoutPatternManager) VdbManager.getEngine()
				.getApplicationContext().getBean("layoutPatternManager");
	}

	public WidgetLayoutDAO getWidgetLayoutDAO()
	{
		return (WidgetLayoutDAO) VdbManager.getEngine().getApplicationContext()
				.getBean("widgetLayoutDAO");
	}

	public WidgetFactory getWidgetFactory()
	{
		return (WidgetFactory) VdbManager.getEngine().getApplicationContext()
				.getBean("widgetFactory");
	}

	public File getWidgetDir(Widget widget)
	{
		return new File(VdbManager.getEngine().getServletContext().getRealPath(
				String.format("/console/webpub/WEB-INF/widgets/%s", widget
						.getName())));
	}

	public boolean existsFile(Widget widget, String fileName)
	{
		return new File(getWidgetDir(widget), fileName).exists();
	}
}