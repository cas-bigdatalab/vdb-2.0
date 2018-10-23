package vdb.mydb.jsp.action.editor;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.impl.FileMetaDataImpl;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.util.StringUtils;

public class DoUploadFile extends ServletActionProxy
{
	private static final String BROWSE_FILE = "BROWSE_FILE";

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext application) throws Exception
	{

		boolean isMultipart = FileUpload
				.isMultipartContent((HttpServletRequest) request);

		if (isMultipart)
		{
			DefaultFileItemFactory factory = new DefaultFileItemFactory();
			factory.setSizeThreshold(4096);
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List /* FileItem */items = upload
					.parseRequest((HttpServletRequest) request);

			// Process the uploaded items
			Iterator iter = items.iterator();
			while (iter.hasNext())
			{
				FileItem item = (FileItem) iter.next();
				if (item.isFormField())
				{
					// processFormField
				}
				else
				{
					// processUploadedFile
					String fieldName = item.getFieldName();
					String fileName = item.getName();
					int i2 = fileName.lastIndexOf("\\");
					if (i2 > -1)
						fileName = fileName.substring(i2 + 1);

					String fileid = StringUtils.getGuid();

					String entity = request.getParameter("entity");
					if (entity.indexOf("?") > 0)
						entity = entity.substring(0, entity.indexOf("?"));

					String filename = fileName;// filepath.substring(filepath.lastIndexOf("/")+1);

					long length = item.getSize();// Long.parseLong(filedata.substring(filedata.indexOf("(")+1,filedata.indexOf(")")));//tmpFile.length();

					FileManager manager = VdbManager.getInstance()
							.getFileManager();
					vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
					Entity en = tool.getEntity(entity);

					FileMetaData fi = manager.createNewFile(en.getDataSet());

					String contentType = item.getContentType();// "image/gif";
					int index = filename.lastIndexOf(".");
					String title = filename.substring(0, index);// amelist[1];//fpt.getFileExtension();
					String ext = filename.substring(index);// namelist[0];//fpt.getFileTitle();

					String relativeFilePath = MessageFormat.format(
							"{0,date,yyyy-MM}", new Date());
					File filesDir = new File(new File(VdbManager.getInstance()
							.getDataSetRoot(en.getDataSet()), "files"),
							relativeFilePath);
					filesDir.mkdirs();

					relativeFilePath += "/" + fileid;
					File newFile = new File(filesDir, fileid);

					fi.setTitle(title);
					fi.setFileSize(length);
					fi.setContentType(contentType);
					fi.setId(fileid);
					fi.setExtension(ext);
					((FileMetaDataImpl) fi).setFilePath(relativeFilePath);
					manager.insert(fi);

					item.write(newFile);

					((HttpServletRequest) request).getSession().setAttribute(
							"FileUpload.Progress."
									+ ((HttpServletRequest) request)
											.getSession().getId(), "-1");
					String para = "'" + fi.getDataSet().getUri() + "','"
							+ fi.getId() + "','" + fi.getTitle() + "','"
							+ fi.getServletPath() + "','" + fi.getExtension()
							+ "'," + fi.getFileSize() + "," + 1 + ","
							+ fi.getImageWidth() + "," + fi.getImageHeight();
					String html = "<script>parent.dofile(" + para
							+ ");</script>";

					request.setAttribute("html", html);
				}

			}

		}
	}
}
