package vdb.mydb.sample;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.mydb.vtl.impl.VtlContextImpl;

public class ViewSourceAction implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		VtlContextImpl hvc = (VtlContextImpl) vc;
		String page = vc.getParameter("page");
		File file = new File(hvc.getServletContext().getRealPath(page));
		StringWriter sw = new StringWriter();
		FileReader fr = new FileReader(file);
		while (true)
		{
			int b = fr.read();
			if (b < 0)
				break;

			sw.write(b);
		}

		vc.put("source", sw.toString());
		fr.close();
	}
}
