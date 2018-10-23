package vdb.webpub.background;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetBackgroundInfo extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		final String picType = request.getAttribute("pictype").toString();

		Properties prop = new Properties();
		try
		{
			File propFile = new File(VdbManager.getEngine().getRootPath()
					+ "/console/webpub/WEB-INF/backgroundConf", picType
					+ ".properties");
			InputStream in = new FileInputStream(propFile);
			prop.load(in);
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		request.setAttribute("props", prop);

	}
}
