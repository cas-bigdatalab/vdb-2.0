package vdb.mydb.jsp.action;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import cn.csdb.commons.action.JspAction;

public class DoExportXml implements JspAction
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String dsid = request.getParameter("dsid");
		DataSet ds = VdbManager.getInstance().getCatalog().fromId(dsid);

		response.setContentType("text/plain");
		((HttpServletResponse) response).addHeader("Content-Disposition",
				"attachment; filename=" + ds.getUri() + ".xml");

		FileInputStream is = new FileInputStream((((VdbDataSet) ds)
				.getSchemaXml()));

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		while (true)
		{
			byte bs[] = new byte[1024];
			int count = is.read(bs);
			os.write(bs, 0, count);

			if (count < 1024)
				break;
		}

		// �滻��ȥ��id
		String s = os.toString("gbk");
		// s = s.replaceAll("<id>.*</id>\\s*", "");
		// s = s.replaceAll("<lastModified>.*</lastModified>\\s*", "");
		// response.getOutputStream().print(s);
		response.getWriter().print(s);
		is.close();
	}
}
