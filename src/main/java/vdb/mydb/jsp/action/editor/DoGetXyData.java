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
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.impl.JsoQuery;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoGetXyData extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String entityStr = request.getParameter("entity") == null ? request
				.getAttribute("entity").toString() : request
				.getParameter("entity");
		String fieldStr = request.getParameter("field") == null ? request
				.getAttribute("field").toString() : request
				.getParameter("field");
		String whereFilter = request.getParameter("whereFilter") == null ? request
				.getAttribute("whereFilter").toString()
				: request.getParameter("whereFilter");
		
		entityStr = java.net.URLDecoder.decode(entityStr, "UTF-8");
		fieldStr = java.net.URLDecoder.decode(fieldStr, "UTF-8");
				
		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();

		JdbcExpr je = null;

		if (whereFilter != null && whereFilter.trim().length() > 0)
		{
			JsoQuery jsoQuery = tool.parseJsoQuery(whereFilter);
			AnyQuery query = tool.createQuery(jsoQuery);
			tool.mergeQuery(query, jsoQuery);
			je = query.where();
		}

		Boolean isSector = null;

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpSession session = servletRequest.getSession();

		String sections = "";

		if (session.getAttribute(entityStr + fieldStr + "sections") != null)
		{
			sections = session.getAttribute(entityStr + fieldStr + "sections")
					.toString();
		}

		int count = 0;

		Entity entity = (Entity) VdbManager.getInstance().getCatalog().fromUri(
				entityStr);
		Field field = tool.getField(fieldStr);
		Boolean isNum = field.getType().getName().equalsIgnoreCase("Long")
				|| field.getType().getName().equalsIgnoreCase("double");

		JdbcSource jds = JdbcSourceManager.getInstance().getJdbcSource(
				entity.getDataSet());
		String sqlCount = "select count(*) as c from " + entity.getTableName();
		if (null != je)
		{
			sqlCount = sqlCount + " where " + je.toStringSql().toString();
		}
		StringSql qsql = new StringSql(sqlCount);
		if (null != je)
		{
			qsql.addParameters(je.toStringSql().getParameters());
		}
		List listCount = jds.queryForObjects(qsql);
		count += Integer.parseInt(((HashMap) listCount.get(0)).get("c")
				.toString());
		String fieldName = "";
		fieldName = field.getColumnName();

		String titleFieldName = "";
		titleFieldName = field.getEntity().getTitleField().getColumnName();

		String pieData = null;

		pieData = "<chart>";
		pieData += "<graphs><graph gid='0'>";
		String sqlStr = "";
		// String sqlStr = "select "+fieldName+", count(*) as c from
		// "+entity.getTableName()+" group by "+fieldName;
		sqlStr = "select " + fieldName + "," + titleFieldName + " from "
				+ entity.getTableName();
		if (null != je)
		{
			sqlStr = sqlStr + " where " + je.toStringSql().toString();
		}
		StringSql sql = new StringSql(sqlStr);
		if (null != je)
		{
			sql.addParameters(je.toStringSql().getParameters());
		}
		List list = jds.queryForObjects(sql);
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (HashMap) list.get(i);
			pieData += "<point x='" + (i + 1) + "' y='" + map.get(fieldName)
					+ "' value='" + map.get(titleFieldName) + "'/>";
		}

		pieData += "</graph></graphs>";
		pieData += "</chart>";

		String txt = "";

		if (isNum)
		{
			String sqlStrSta = "select sum(" + fieldName + ") as a, avg("
					+ fieldName + ") as b, max(" + fieldName + ") as c, min("
					+ fieldName + ") as d from " + entity.getTableName();
			// String sqlStrSta = "select sum(" + fieldName + "), avg("
			// + fieldName + "), max(" + fieldName + "), min(" + fieldName
			// + ") from " + entity.getTableName();

			if (null != je)
			{
				sqlStrSta = sqlStrSta + " where " + je.toStringSql().toString();
			}

			StringSql sqlSta = new StringSql(sqlStrSta);

			if (null != je)
			{
				sqlSta.addParameters(je.toStringSql().getParameters());
			}

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

		request.setAttribute("pieData", pieData);
		request.setAttribute("count", count);
		request.setAttribute("txts", txts);
		request.setAttribute("sections", sections);
		request.setAttribute("entityStr", entityStr);
		request.setAttribute("fieldStr", fieldStr);
	}
}
