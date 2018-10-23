/*
 * Created on 2006-8-28
 */
package vdb.mydb.jsp.action.editor;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.impl.FileMetaDataImpl;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.util.StringUtils;

public class LocalFilesMetaSave extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String filedata = request.getParameter("value");
		if (filedata.startsWith("\\") || filedata.startsWith("/"))
			filedata = filedata.substring(1);

		FileManager manager = VdbManager.getEngine().getFileManager();
		vdb.mydb.metacat.VdbDataSet ds = VdbManager.getEngine().getCatalog()
				.fromId(request.getParameter("dsid"));
		FileMetaData fi = manager.createNewFile(ds);

		String filepath = "";
		String filename = "";
		long length = 0;
		String contentType = "";
		String title = "";
		String ext = "";
		String path = "";

		if (filedata.indexOf(".") >= 0)
		{
			filepath = filedata.substring(0, filedata.indexOf("("));
			filename = filepath.substring(filepath.lastIndexOf("/") + 1);
			length = Long.parseLong(filedata.substring(
					filedata.indexOf("(") + 1, filedata.indexOf(")")));
			contentType = "";
			title = filename.substring(0, filename.indexOf("."));
			ext = filename.substring(filename.indexOf("."));
			path = filedata.substring(0, filedata.indexOf("(")).replace(
					"/files", "");
		}
		else
		{
			contentType = "path";
			path = filedata;
		}


		String id = StringUtils.getGuid();//
		fi.setTitle(title);
		fi.setFileSize(length);
		fi.setContentType(contentType);
		fi.setId(id);
		fi.setExtension(ext);
		((FileMetaDataImpl) fi).setFilePath(path);

		manager.insert(fi);
		String para = "'" + fi.getDataSet().getUri() + "','" + fi.getId()
				+ "','" + fi.getTitle() + "','" + fi.getServletPath() + "','"
				+ fi.getExtension() + "'," + fi.getFileSize() + "," + 1 + ","
				+ fi.getImageWidth() + "," + fi.getImageHeight();

		response
				.getWriter()
				.println(
						"<script>parent.dofile("
								+ para
								+ ");window.location.href = '/console/editor/jquery/addLocalFile.jsp?dsid="
								+ request.getParameter("dsid") + "';</script>");
		response.getWriter().flush();
	}
}
