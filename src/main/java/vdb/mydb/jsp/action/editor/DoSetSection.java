package vdb.mydb.jsp.action.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoSetSection extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String entityStr = request.getParameter("entity");
		String fieldStr = request.getParameter("field");
		Boolean isSector = null;

		entityStr = java.net.URLDecoder.decode(entityStr, "UTF-8");
		fieldStr = java.net.URLDecoder.decode(fieldStr, "UTF-8");
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpSession session = servletRequest.getSession();

		if (request.getParameter("isSector") != null)
		{
			isSector = request.getParameter("isSector")
					.equalsIgnoreCase("true");
			session.setAttribute(entityStr + fieldStr + "isSector", isSector);
		}

		if (session.getAttribute(entityStr + fieldStr + "isSector") != null)
		{
			isSector = session.getAttribute(entityStr + fieldStr + "isSector")
					.toString().equalsIgnoreCase("true");
		}

		String sections = "";

		if (request.getParameter("sections") != null)
		{
			sections = request.getParameter("sections");
			session.setAttribute(entityStr + fieldStr + "sections", sections);
		}

		if (session.getAttribute(entityStr + fieldStr + "sections") != null)
		{
			sections = session.getAttribute(entityStr + fieldStr + "sections")
					.toString();
		}

		String[] sectionList = sections.split(";");

		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
		Entity entity = (Entity) VdbManager.getInstance().getCatalog().fromUri(
				entityStr);
		Field field = tool.getField(fieldStr);
		Boolean isNum = field.getType().getName().equalsIgnoreCase("Long")
				|| field.getType().getName().equalsIgnoreCase("double");
		Boolean isDate = field.getType().getName().equalsIgnoreCase("Date");

		JdbcSource jds = JdbcSourceManager.getInstance().getJdbcSource(
				entity.getDataSet());
		String fieldName = "";
		fieldName = field.getColumnName();
		String txt = "";

		if (isDate || isNum)
		{
			String sqlStrSta = "select max(" + fieldName + ") as a, min("
					+ fieldName + ") as b from " + entity.getTableName();

			QuerySql sqlSta = new StringSql(sqlStrSta);
			List listSta = jds.queryForObjects(sqlSta);

			if (listSta.size() > 0)
			{
				Map map = (HashMap) listSta.get(0);
				Iterator it = map.entrySet().iterator();
				while (it.hasNext())
				{
					Map.Entry entry = (Map.Entry) it.next();
					if (txt.equals(""))
						txt += entry.getValue();
					else
						txt += "," + entry.getValue();
				}
			}
		}
		String[] txts = txt.split(",");

		request.setAttribute("isSector", isSector);
		request.setAttribute("isDate", isDate);
		request.setAttribute("isNum", isNum);
		request.setAttribute("txts", txts);
		request.setAttribute("sectionList", sectionList);
		request.setAttribute("entityStr", entityStr);
		request.setAttribute("fieldStr", fieldStr);
	}
}
