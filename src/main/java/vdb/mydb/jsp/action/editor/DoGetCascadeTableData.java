package vdb.mydb.jsp.action.editor;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.da.DataAccessTool;

public class DoGetCascadeTableData extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext)
	{

		String dsuri = request.getParameter("dsuri");
		String tablename = request.getParameter("tablename");
		String key = request.getParameter("key");
		String id = "-1";

		if ("0".equals(key))
			id = "0";
		else if (key.lastIndexOf(",") > 0)
		{
			id = key.substring(key.lastIndexOf(",") + 1);
		}
		String whereFilter = "select id, name from " + tablename
				+ " where pid=" + id;
		StringBuffer data = new StringBuffer("");

		try
		{
			if (whereFilter.indexOf(";") > 0)
				whereFilter = whereFilter.split(";")[0];

			DataAccessTool dataAccessTool = new DataAccessTool();
			List<Map<String, Serializable>> list = dataAccessTool.execute(
					dsuri, whereFilter);

			if (list.size() == 0)
			{

			}
			else
			{

				for (Map<String, Serializable> map : list)
				{
					Iterator it = map.entrySet().iterator();
					String _name = null;
					String _value = null;

					while (it.hasNext())
					{
						Map.Entry entry = (Map.Entry) it.next();
						if ("name".equalsIgnoreCase(entry.getKey().toString()))
							_name = entry.getValue().toString();
						if ("id".equalsIgnoreCase(entry.getKey().toString()))
							_value = entry.getValue().toString();
					}
					if (null != _name && null != _value)
						data.append("'" + _value + "':'" + _name + "',");
				}
				if (data.toString().length() != 0
						&& data.lastIndexOf(",") == data.length() - 1)
					data.deleteCharAt(data.length() - 1);

				if (data.toString().length() != 0)
					data = new StringBuffer("{" + data.toString() + "}");
			}
		}// try-end
		catch (Exception e)
		{
			e.printStackTrace();
			data.append("{'ERROR': 'SQL exception'}");
		}
		finally
		{
			request.setAttribute("data", data);
			response.setContentType("application/x-json; charset=UTF-8");
		}

	}
}