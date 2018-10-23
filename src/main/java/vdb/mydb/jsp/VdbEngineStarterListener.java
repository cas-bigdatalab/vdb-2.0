package vdb.mydb.jsp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import vdb.mydb.engine.VdbEngineStarter;

public class VdbEngineStarterListener implements ServletContextListener
{
	private VdbEngineStarter _engineStarter;

	public void contextDestroyed(ServletContextEvent arg0)
	{
		_engineStarter.shutdown();
	}

	public void contextInitialized(ServletContextEvent arg0)
	{
		try
		{
			_engineStarter = new VdbEngineStarter();
			_engineStarter.startEngine(arg0.getServletContext(), arg0
					.getServletContext().getInitParameter("iocStartXml"), arg0
					.getServletContext().getInitParameter("vdbProperties"));
		} catch (Exception e)
		{
			throw new RuntimeException(String.format(
					"failed to start up VdbEngine due to: %s", e.getMessage()),
					e);
		}
	}

}
