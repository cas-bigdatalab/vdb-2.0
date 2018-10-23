package vdb.mydb.engine;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.DisposableBean;

import cn.csdb.grid.rpc.RPCServlet;

public class VdbEngineServer implements DisposableBean
{
	class HelloServlet extends HttpServlet
	{
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException
		{
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			String body = "";

			StringBuffer buf = new StringBuffer();

			if (_servlets != null)
			{
				for (Entry<String, HttpServlet> entry : _servlets.entrySet())
				{
					buf.append(String.format("<li>%s-->%s</li>",
							entry.getKey(), entry.getValue()));
				}
			}

			if (_rpcServices != null)
			{
				for (Entry<String, Object> entry : _rpcServices
						.getServicesMapping().entrySet())
				{
					buf.append(String.format("<li>%s-->%s</li>", _rpcServices
							.getUrl(entry.getKey()), entry.getValue()));
				}
			}

			body = buf.toString();
			response.getWriter().println(body);
		}
	}

	Server _jettyServer;

	int _port = 4444;

	Map<String, HttpServlet> _servlets;

	RPCServices _rpcServices;

	public void setRpcServices(RPCServices rpcServices)
	{
		_rpcServices = rpcServices;
	}

	public void addServlets(Context root, RPCServices services)
	{
		for (Entry<String, Object> entry : services.getServicesMapping()
				.entrySet())
		{
			try
			{
				String api = entry.getKey();

				RPCServlet servlet = new RPCServlet();
				Object homeBean = entry.getValue();
				servlet.setHome(homeBean);
				servlet.setHomeAPI(Class.forName(api));

				root.addServlet(new ServletHolder(servlet), services
						.getUrl(api));

				Logger.getLogger(this.getClass()).debug(
						String.format("loaded servlet: %s --> %s", api,
								homeBean));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void start()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				_jettyServer = new Server(_port);
				Context root = new Context(_jettyServer, "/", Context.SESSIONS);
				root.addServlet(new ServletHolder(new HelloServlet()), "/");

				if (_servlets != null)
				{
					for (Entry<String, HttpServlet> entry : _servlets
							.entrySet())
					{
						root.addServlet(new ServletHolder(entry.getValue()),
								entry.getKey());
					}
				}

				if (_rpcServices != null)
				{
					addServlets(root, _rpcServices);
				}

				try
				{
					_jettyServer.start();
					_jettyServer.join();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	public int getPort()
	{
		return _port;
	}

	public void setPort(int port)
	{
		_port = port;
	}

	/**
	 * TODO: enumerate interfaces automatically
	 * 
	 * @param servlets
	 */
	public void setServlets(Map<String, HttpServlet> servlets)
	{
		_servlets = servlets;
	}

	public void destroy() throws Exception
	{
		_jettyServer.stop();
	}
}
