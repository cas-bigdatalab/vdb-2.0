/*
 * Created on 2006-11-16
 */
package vdb.mydb.jsp;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import vdb.mydb.VdbManager;
import vdb.mydb.util.ServletUtil;

/*
 * @author bluejoe
 */
public class VspRenderServlet extends HttpServlet
{
	private String _errorPage = "/console/shared/error.vpage";

	@Override
	public void init(ServletConfig arg0) throws ServletException
	{
		super.init(arg0);

		_errorPage = arg0.getInitParameter("errorPage");
		if (_errorPage == null)
			_errorPage = "/console/shared/error.vpage";
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		response.setContentType("text/html; charset="
				+ VdbManager.getInstance().getResponseEncoding());

		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext(httpRequest, httpResponse);

		final String url = URLDecoder.decode(ServletUtil
				.getRequestPath(httpRequest), VdbManager.getInstance()
				.getResponseEncoding());

		try
		{
			VdbManager.getInstance().getVelocityEngine().layout(url, ctx,
					response.getWriter());
		}
		catch (Throwable t)
		{
			try
			{
				ctx.put("url", url);
				ctx.put("exception", t);
				VdbManager.getInstance().getVelocityEngine().layout(_errorPage,
						ctx, response.getWriter());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public String getErrorPage()
	{
		return _errorPage;
	}

	public void setErrorPage(String errorPage)
	{
		_errorPage = errorPage;
	}
}
