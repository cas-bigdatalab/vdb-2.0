package vdb.webpub.background;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetPics extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		final String picType = request.getAttribute("pictype").toString();
		List<String> files = new ArrayList<String>();
		try
		{
			File file = new File(VdbManager.getEngine().getRootPath()
					+ "/userfiles/" + picType);

			for (File f : file.listFiles())
			{
				if (f.getName().endsWith(".jpg")
						|| f.getName().endsWith(".bmp")
						|| f.getName().endsWith(".jpeg")
						|| f.getName().endsWith(".png")
						|| f.getName().endsWith(".gif"))
					files.add(f.getName());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		request.setAttribute("fileNames", files);

	}
}
