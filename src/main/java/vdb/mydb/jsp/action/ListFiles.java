package vdb.mydb.jsp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.FileOwner;
import cn.csdb.commons.action.JspAction;

public class ListFiles implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext application) throws Exception
	{
		String id = request.getParameter("id");
		String fid = request.getParameter("fid");
		Field field = (Field) VdbManager.getInstance().getCatalog().fromId(fid);
		FileManager manager = VdbManager.getInstance().getFileManager();
		List<FileMetaData> files = null;

		if (id != null)
		{
			files = manager.getFiles(field.getEntity().getDataSet(),
					new FileOwner(id, field));
		}
		else
		{
			files = new ArrayList<FileMetaData>();
		}

		long bytes = 0;
		String ids = "";

		for (FileMetaData file : files)
		{
			bytes += file.getFileSize();
			ids += file.getId() + ";";
		}

		request.setAttribute("field", field);
		request.setAttribute("ids", ids);
		request.setAttribute("files", files);
		request.setAttribute("bytes", bytes);
	}

}
