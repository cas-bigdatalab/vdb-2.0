package vdb.mydb.jsp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import vdb.mydb.engine.VdbEngineStarter;

/**
 * @deprecated use VdbEngineStarterListener instead!
 * @author bluejoe
 *
 */
public class VdbEngineStarterServlet extends HttpServlet
{
	private ServletContext _servletContext;

	private VdbEngineStarter _engineStarter;

	@Override
	public void destroy()
	{
		super.destroy();
		_engineStarter.shutdown();
	}

	public void init(ServletConfig serveltConfig) throws ServletException
	{
		Logger.getLogger(this.getClass()).warn(
				String.format("%s is deprecated, use %s instead!", this
						.getClass().getName(), VdbEngineStarterListener.class
						.getName()));
		
		_servletContext = serveltConfig.getServletContext();

		try
		{
			_engineStarter = new VdbEngineStarter();
			_engineStarter.startEngine(serveltConfig);
		} catch (Exception e)
		{
			throw new ServletException(String.format(
					"failed to start up VdbEngine due to: %s", e.getMessage()),
					e);
		}
	}
}
