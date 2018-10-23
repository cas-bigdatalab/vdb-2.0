package vdb.tool.charts;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import vdb.metacat.DataSet;
import vdb.metacat.fs.page.ChartsManager;
import vdb.metacat.fs.page.Project;
import vdb.mydb.VdbManager;
import vdb.tool.VelocityAware;

public class ChartsManagerTool implements VelocityAware
{
	private Context _context;

	private VelocityEngine _engine;

	public ChartsManager getChartsManager()
	{
		return (ChartsManager) VdbManager.getEngine().getApplicationContext()
				.getBean("chartsManager");
	}

	public boolean checkChartName(String name, DataSet ds) throws Exception
	{
		return getChartsManager().checkChartName(name, ds);
	}

	@SuppressWarnings("deprecation")
	public String initSelect(String datasetUri, String name)
			throws ClassNotFoundException, IOException
	{
		if ("isnotdataset".equals(datasetUri))
		{
			return "请选择数据集";
		}
		else
		{
			return this.getChartsManager().initSelect(
					(DataSet) VdbManager.getInstance().getCatalog().fromUri(
							datasetUri), name);
		}
	}

	@SuppressWarnings("deprecation")
	public Project getProjectByName(String name, String datasetUri)
			throws IOException, ClassNotFoundException
	{
		return this.getChartsManager().getProjectByName(
				name,
				(DataSet) VdbManager.getInstance().getCatalog().fromUri(
						datasetUri));
	}

	public String chart(String chartName, String datasetUri)
			throws IOException, ClassNotFoundException
	{
		Project project = getProjectByName(chartName, datasetUri);
		if (project != null)
		{
			StringWriter sw = new StringWriter();
			String path = "/console/shared/";
			if (project.getViewType().equalsIgnoreCase("pie"))
			{
				path = path + "groupStatPie.vpage";

			}
			if (project.getViewType().equalsIgnoreCase("column"))
			{
				path = path + "groupStatColumn.vpage";

			}
			if (project.getViewType().equalsIgnoreCase("line"))
			{
				path = path + "groupStatLine.vpage";

			}
			if (project.getViewType().equalsIgnoreCase("ballon"))
			{
				path = path + "groupStatXy.vpage";
			}

			_context.put("entity", project.getEntityUri());
			_context.put("field", project.getFieldUri());
			_context.put("whereFilter", project.getCondition());
			_engine.mergeTemplate(path, "utf-8", _context, sw);
			return sw.toString();
		}
		return null;

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
