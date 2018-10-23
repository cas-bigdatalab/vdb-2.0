package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoIndex extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(1);
					VdbManager.getEngine().getIndexer().getWriter().update();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
}