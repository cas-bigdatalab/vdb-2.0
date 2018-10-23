package vdb.mydb.widget.vsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.mydb.widget.Widget;
import vdb.tool.webpub.WidgetTool;

public class DoCopyWidget implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		HttpServletRequest request = vc.getRequest();

		// String enName = request.getParameter("enName");
		// String chName = request.getParameter("chName");
		String widget = request.getParameter("widget");

		String enName = request.getParameter("enName");
		String chName = request.getParameter("chName");
		String sPath = VdbManager.getInstance().getServletContext()
				.getRealPath("/console/webpub/WEB-INF/widgets/" + widget);
		String dPath = VdbManager.getInstance().getServletContext()
				.getRealPath("/console/webpub/WEB-INF/widgets/" + enName);
		File file = new File(sPath);
		if (!file.exists())
		{
			request.setAttribute("message", "原始组件不存在");
			return;
		}
		if (enName == null || enName.trim().length() <= 0
				|| "null".endsWith(enName.trim()))
		{
			request.setAttribute("message", "新组件名输入错误");
			return;
		}
		new File(dPath).mkdir();
		copyDir(sPath, dPath);

		Widget widgetObj = new Widget();
		widgetObj.setName(enName);
		widgetObj.setTitle(chName);
		widgetObj.setAsyc(false);
		widgetObj.setReadOnly(false);

		new WidgetTool().getWidgetFactory().copyWidget(widgetObj);

		request.setAttribute("message", "复制成功");
	}

	private void copyFile(String source, String dest)
	{
		try
		{
			File in = new File(source);
			File out = new File(dest);
			FileInputStream inFile = new FileInputStream(in);
			FileOutputStream outFile = new FileOutputStream(out);
			byte[] buffer = new byte[1024];
			int i = 0;
			while ((i = inFile.read(buffer)) != -1)
			{
				outFile.write(buffer, 0, i);
			}
			inFile.close();
			outFile.close();
		}
		catch (Exception e)
		{

		}
	}

	private void copyDir(String source, String dest)
	{
		String source1;
		String dest1;

		File[] file = (new File(source)).listFiles();
		for (int i = 0; i < file.length; i++)
		{
			if (file[i].isFile() && !file[i].getName().startsWith("."))
			{
				source1 = source + "/" + file[i].getName();
				dest1 = dest + "/" + file[i].getName();
				copyFile(source1, dest1);
			}
		}
		for (int i = 0; i < file.length; i++)
		{
			if (file[i].isDirectory() && !file[i].getName().startsWith("."))
			{
				source1 = source + "/" + file[i].getName();
				dest1 = dest + "/" + file[i].getName();
				File dest2 = new File(dest1);
				dest2.mkdir();
				copyDir(source1, dest1);
			}
		}
	}

}
