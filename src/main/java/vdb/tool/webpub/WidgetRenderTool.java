package vdb.tool.webpub;

import java.io.StringWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import vdb.tool.VelocityAware;

public class WidgetRenderTool implements VelocityAware
{
	private Context _context;

	private VelocityEngine _engine;

	public String renderWidget(String widgetName) throws Exception
	{
		StringWriter sw = new StringWriter();
		_context.put("widgetName", widgetName);
		String path = "/console/webpub/renderWidget.vpage";

		_engine.mergeTemplate(path, "utf-8", _context, sw);
		return sw.toString();
	}

	public void setVelocityContext(Context context)
	{
		_context = context;
	}

	public void setVelocityEngine(VelocityEngine engine)
	{
		_engine = engine;
	}
}