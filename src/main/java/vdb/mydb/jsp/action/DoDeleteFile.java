package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import cn.csdb.commons.action.JspAction;

public class DoDeleteFile implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String id = request.getParameter("id");
		String uri = request.getParameter("dataset");

		try
		{
			FileManager manager = VdbManager.getInstance().getFileManager();
			FileMetaData file = manager.getFile((DataSet) VdbManager
					.getInstance().getCatalog().fromUri(uri), id);
			manager.delete(file);
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
	}
}
