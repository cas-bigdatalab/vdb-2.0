package vdb.mydb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoExcelOutServletSql extends HttpServlet
{
	private String _errorPage;

	@Override
	public void init(ServletConfig arg0) throws ServletException
	{
		super.init(arg0);

		_errorPage = arg0.getInitParameter("errorPage");
		if (_errorPage == null)
			_errorPage = "/console/shared/error.vpage";
	}

	public void yuanExcels(OutputStream os, String whereFilter,
			String exportType, String entityStr, String id, String exportItem)
			throws Exception
	{
		{

			String ttitle = "数据";
			WritableWorkbook book = null;
			String dsuri = entityStr.split("@")[1];
			DataSet dataSet = (DataSet) VdbManager.getInstance().getCatalog()
					.fromUri(dsuri);

			JdbcSource jds = JdbcSourceManager.getInstance().getJdbcSource(
					dataSet);
			QuerySql sql = new StringSql(whereFilter);

			List list = jds.queryForObjects(sql);

			try
			{

				// 打开文件
				book = Workbook.createWorkbook(os);
				// 生成名为"专家信息"的工作表，参数0表示这是第一页
				WritableSheet sheet = book.createSheet(ttitle, 0);
				// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
				// 以及单元格内容
				try
				{
					int j = 0;
					Map map0 = (HashMap) list.get(0);
					Iterator it0 = map0.entrySet().iterator();
					while (it0.hasNext())
					{
						Map.Entry entry = (Map.Entry) it0.next();

						Label label = new Label(j, 0, entry.getKey().toString());
						// 将定义好的单元格添加到工作表中
						sheet.addCell(label);
						j++;

					}

					for (int i = 0; i < list.size(); i++)
					{
						Map map = (HashMap) list.get(i);
						Iterator it = map.entrySet().iterator();
						j = 0;
						while (it.hasNext())
						{
							Map.Entry entry = (Map.Entry) it.next();
							Object value = entry.getValue();
							String txt = "";
							if (value != null)
								txt = value.toString();
							else
								txt = "";

							Label label = new Label(j, (i + 1), txt);
							// 将定义好的单元格添加到工作表中
							sheet.addCell(label);
							j++;
						}
					}
				}
				catch (Exception e)
				{

				}
				// 写入excel数据，并关闭对象
				book.write();
				// book.close();
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			finally
			{
				try
				{
					book.close();
				}
				catch (WriteException e)
				{

					e.printStackTrace();
				}
				catch (IOException e)
				{

					e.printStackTrace();
				}
			}

		}
	}

	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		String whereFilter = request.getParameter("whereFilter") == null ? ""
				: request.getParameter("whereFilter");
		request.setCharacterEncoding("UTF-8");
		String exportType = request.getParameter("exportType");
		String tid = request.getParameter("entity");
		String id = request.getParameter("id");
		String exportItem = request.getParameter("exportItem");

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		String dateString = "";
		if (month < 10)
		{
			String months = "0" + String.valueOf(month);
			if (date < 10)
			{
				String dates = "0" + String.valueOf(date);
				dateString = "" + year + months + dates;
			}
			else
			{
				dateString = "" + year + months + date;
			}
		}
		else
		{
			dateString = "" + year + month + date;
		}
		String showFileName = "EXCEL" + dateString + ".xls";
		// 文件名称的编码

		showFileName = new String(showFileName.getBytes("ISO-8859-1"), "utf-8");

		// 设置页面弹出下载窗口

		response.addHeader("Content-disposition", "attachment; filename=\""
				+ showFileName + "\"");
		response.setContentType("application/unknown;charset=utf-8");
		try
		{
			OutputStream outstream = response.getOutputStream();
			yuanExcels(outstream, whereFilter, exportType, tid, id, exportItem);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

	}

}
