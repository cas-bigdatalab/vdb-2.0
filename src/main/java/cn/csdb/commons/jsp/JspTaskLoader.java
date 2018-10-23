package cn.csdb.commons.jsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import cn.csdb.commons.util.TimeUtils;

/**
 * 该servlet用以执行指定路径的jsp文件。
 * 
 * <pre>
 *       	&lt;servlet servlet-name=&quot;RoutineDaily&quot;
 *                 servlet-class=&quot;cn.csdb.commons.jsp.JspTaskLoader&quot;&gt;
 *       		&lt;run-at&gt;0:00&lt;/run-at&gt;
 *       		&lt;init-param task=&quot;/tasks/daily.jsp&quot;/&gt;
 *       	&lt;/servlet&gt;
 * </pre>
 * 
 * @author bluejoe
 */
public class JspTaskLoader extends HttpServlet
{
	private String _task;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("[" + TimeUtils.getNowString() + "]例行程序"
				+ getServletName() + "开始运行...");

		if (_task != null)
		{
			try
			{
				getServletContext().getRequestDispatcher(_task).include(
						request, response);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		System.out.println("[" + TimeUtils.getNowString() + "]例行程序"
				+ getServletName() + "运行完毕！");
	}

	public void init() throws ServletException
	{
		_task = getInitParameter("task");
	}
}
