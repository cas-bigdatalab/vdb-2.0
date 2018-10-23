package vdb.mydb.jsp.action.editor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.util.XmlUtil;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.da.DataAccessTool;

public class DoGetTableDataSql extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext)
	{

		String entityStr = request.getParameter("entity");
		int curPage = Integer.parseInt(request.getParameter("start"));
		int pageSize = Integer.parseInt(request.getParameter("size"));
		int start = (curPage - 1) * pageSize + 1;
		String whereFilter = null;
		String html = "";
		int recordNum = 0;
		if (request.getParameter("whereFilter") != null)
			try
			{
				whereFilter = java.net.URLDecoder.decode(request
						.getParameter("whereFilter"), "UTF-8");
			}
			catch (UnsupportedEncodingException e1)
			{

				e1.printStackTrace();
			}

		String attachTable = "no";
		DataAccessTool dataAccessTool = new DataAccessTool();

		try
		{
			String dsuri = entityStr.split("@")[1];
			if (whereFilter != null && whereFilter.indexOf(";") > 0)
				whereFilter = whereFilter.split(";")[0];
			recordNum = dataAccessTool.recordsSize(dsuri, whereFilter);
			XmlUtil xmlutil = new XmlUtil((HttpServletRequest) request,
					(HttpServletResponse) response);
			int n = (start + pageSize - 1) < recordNum ? pageSize : (recordNum
					- start + 1);

			List<Map<String, Serializable>> list = dataAccessTool.execute(
					dsuri, whereFilter, start, n);
			String txt = "";
			int i = 0;
			int j = 1;

			if (list.size() == 0)
			{
				String col_1 = "<column id='1' sort='txt' align='left' width='250' type='ro'>Empty</column>";
				xmlutil.setXmlHead(col_1);
				xmlutil.setId("1");
				xmlutil.setXmlData("没有查找到相应的记录!");
				// DataSet ds = dataAccessTool.
			}
			else
			{
				for (Map<String, Serializable> map : list)
				{
					xmlutil.setId(String.valueOf(i));
					Iterator it = map.entrySet().iterator();

					while (it.hasNext())
					{
						Map.Entry entry = (Map.Entry) it.next();
						if (i == 0)
						{
							String temp = "<column id='"
									+ j
									+ "' sort='txt' align='center' width='100' type='ro'>"
									+ entry.getKey() + "</column>";
							xmlutil.setXmlHead(temp);
							j++;
						}

						String str = "";
						if (entry.getValue() != null)
							str = entry.getValue().toString();
						str = str.replaceAll("&", "&amp;");
						str = str.replaceAll(">", "&amp;gt;");
						str = str.replaceAll("<", "&amp;lt;");
						str = str.replaceAll("'", "&amp;apos;");
						str = str.replaceAll("\"", "&amp;quot;");
						xmlutil.setXmlData(str);
					}
					xmlutil.setXmlData(String.valueOf(++i));
				}
			}
			html = xmlutil.getXmlFile();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			XmlUtil xmlutil = new XmlUtil((HttpServletRequest) request,
					(HttpServletResponse) response);

			if (e instanceof SQLException)
			{
				SQLException sqlException = (SQLException) e;
				String col_1 = "<column id='1' sort='txt' align='left' width='250' type='ro'>ErrorCode</column>";
				xmlutil.setXmlHead(col_1);
				String col_2 = "<column id='2' sort='txt' align='left' width='250' type='ro'>Message</column>";
				xmlutil.setXmlHead(col_2);
				String col_3 = "<column id='3' sort='txt' align='left' width='250' type='ro'>SQLState</column>";
				xmlutil.setXmlHead(col_3);

				xmlutil.setId("1");
				xmlutil.setXmlData(sqlException.getErrorCode());
				xmlutil.setXmlData(sqlException.getMessage());
				xmlutil.setXmlData(sqlException.getSQLState());

				html = xmlutil.getXmlFile();
			}
			else
			{
				String col_1 = "<column id='1' sort='txt' align='left' width='250' type='ro'>message</column>";
				xmlutil.setXmlHead(col_1);
				xmlutil.setId("1");
				xmlutil.setXmlData("sorry!");
				html = xmlutil.getXmlFile();
			}
		}
		finally
		{
			int pageNum = (recordNum % pageSize == 0) ? (recordNum / pageSize)
					: (recordNum / pageSize) + 1;
			html += "@CNIC@" + recordNum + "@CNIC@" + pageNum + "@CNIC@"
					+ attachTable;
			request.setAttribute("data", html);
		}

	}
}
