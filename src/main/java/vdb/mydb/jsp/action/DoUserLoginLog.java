package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.log.dao.UserLoginLogDao;
import vdb.log.vo.UserLoginLog;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.ui.PagerTool;
import cn.csdb.commons.jsp.BeanPageViewer;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public class DoUserLoginLog extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		try
		{
			UserLoginLogDao dao = new UserLoginLogDao();
			PagerTool tool = new PagerTool();

			String user = request.getParameter("user");
			Pageable pageable = dao.getUserLoginLogs(user);
			int size = pageable.size();

			if (size > 0)
			{
				PageViewer pageViewer = tool.create(pageable, 1, size);
				// 登陆者名称
				String name;
				// 登陆者时间
				String time;
				// 登陆者地址
				String ip;

				String nameTimeIp;
				String usersLogData = "<rows>";

				int i, rowId;
				for (i = 0; i < size; i++)
				{
					rowId = i + 1;
					usersLogData += "<row id='" + rowId + "'>";
					usersLogData += "<cell>" + rowId + "</cell>";
					name = ((UserLoginLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getUser();
					time = ((UserLoginLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getOpTime();

					ip = ((UserLoginLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getIp();
					nameTimeIp = name + " 在 " + time + " 于 " + ip + " 登陆 ";
					usersLogData += "<cell>" + nameTimeIp + "</cell></row>";
				}
				usersLogData += "</rows>";
				request.setAttribute("usersLogData", usersLogData);
			}
			else
			{
				request.setAttribute("usersLogData", "<rows></rows>");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
