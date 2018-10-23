/*
 * Created on 2006-11-16
 */
package vdb.mydb.jsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;

/*
 * ƥ��<��ͼ��>.view�������ͼ�ļ�������ʾ ��servelt�������ָ��view������ģ��ҳ��
 * 
 * @author bluejoe
 */
public class ActionServlet extends HttpServlet
{
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		response.setContentType("text/html; charset="
				+ VdbManager.getInstance().getResponseEncoding());

		// eg. url="query.view?tid=1234&id=abc"
		String url = httpRequest.getServletPath();
		String actionName = null;
		int i = url.indexOf(".action");
		if (i > 0)
		{
			int m = url.lastIndexOf("/", i);
			if (m >= 0)
			{
				actionName = url.substring(m + 1, i);
				String className = "vdb.mydb.jsp.action."
						+ Character.toUpperCase(actionName.charAt(0))
						+ actionName.substring(1);
				JspAction action;
				try
				{
					action = (JspAction) Class.forName(className).newInstance();
					action.doAction(request, response, VdbManager.getEngine()
							.getServletContext());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
