package vdb.webpub.background;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoSaveBackground extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String picType = request.getParameter("picType");
		String picRes = request.getParameter("picRes");
		String fileName = request.getParameter("fileName");
		String colorPicker = request.getParameter("colorPicker");
		
		String colorPickerAll = request.getParameter("colorPickerAll");

		Properties propsWrite = new Properties();
		propsWrite.setProperty("picRes", picRes);
		
		if (null != fileName)
		{
			propsWrite.setProperty("fileName", fileName);
		}
		
		if (null != colorPicker)
		{
			propsWrite.setProperty("colorPicker", colorPicker);
		}
		
		if (null != colorPickerAll)
		{
			propsWrite.setProperty("colorPickerAll", colorPickerAll);
		}

		File propFile = new File(VdbManager.getEngine().getRootPath()
				+ "/console/webpub/WEB-INF/backgroundConf", picType
				+ ".properties");
		if (!propFile.exists())
		{
			propFile.createNewFile();
		}

		OutputStream out = new FileOutputStream(propFile);
		propsWrite.store(out, null);
		out.close();

	}
}
