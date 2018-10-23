package vdb.mydb.jsp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.impl.JdbcRow;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoGetGroupCode extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		String gcode = request.getParameter("gcode");
		List objects = new ArrayList();
		objects = jt.queryForObjects(new StringSql(
				"select * from VDB_GROUPS where GROUPCODE like ?", gcode
						+ ".____"));
		String result = "";
		if (objects != null && objects.size() > 0)
		{
			String gcodeEnd = ((JdbcRow) objects.get(objects.size() - 1)).get(
					"GROUPCODE").toString();
			gcodeEnd = gcodeEnd.substring(gcodeEnd.length() - 4, gcodeEnd
					.length());

			// 找到第一个不为0的位置
			int pos = 0;
			for (int i = 0; i < gcodeEnd.length(); i++)
			{
				if (gcodeEnd.charAt(i) == '0')
				{
					pos = i;
				}
				else
				{
					break;
				}

			}

			int val = Integer.parseInt(gcodeEnd.substring(pos + 1)) + 1;
			result = "" + val;
			int len = result.length();
			for (int i = 0; i < 4 - len; i++)
			{
				result = "0" + result;
			}
			result = "." + result;
		}
		else
		{
			result = ".0001";
		}
		request.setAttribute("result", result);
	}
}
