package vdb.mydb.jsp.action.editor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.fs.page.Chart;
import vdb.metacat.fs.page.ChartsManager;
import vdb.metacat.fs.page.Project;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class SaveChart extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,

	ServletContext servletContext) throws Exception
	{
		String entityUri = request.getParameter("entityUri");
		Project project = new Project();
		project.setName(request.getParameter("name"));
		project.setTitle(request.getParameter("title"));
		project.setEntityUri(entityUri);
		project.setFieldUri(request.getParameter("fieldUri"));
		project.setCondition(request.getParameter("whereFilter"));
		project.setViewType(request.getParameter("viewType"));

		Entity entity = VdbManager.getEngine().getCatalog().fromUri(entityUri);
		DataSet ds = entity.getDataSet();
		ChartsManager cm = new ChartsManager();
		// 加载charts.xml文件
		Chart chart = cm.load(ds);
		List<Project> projects = new ArrayList<Project>();
		// 如果存在
		if (chart != null)
		{
			projects = chart.getProjects();
		}
		else
		{
			// 如果不存在
			chart = new Chart();
		}
		int index = -1;
		for (int i = 0; i < projects.size(); i++)
		{
			Project p = projects.get(i);
			if (p.getName().equalsIgnoreCase(project.getName()))
				index = i;
		}
		if (index != -1)
			projects.remove(index);

		projects.add(project);
		chart.setProjects(projects);

		cm.saveAs(chart, ds);
	}
}
