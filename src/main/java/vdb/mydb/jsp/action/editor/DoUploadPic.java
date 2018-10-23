package vdb.mydb.jsp.action.editor;

import java.io.File;
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

import vdb.mydb.VdbManager;
import vdb.mydb.files.impl.FileMetaDataImpl;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUploadPic extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext application) throws Exception
	{
		String picType = request.getAttribute("pictype").toString();
		if (picType.indexOf("?") > 0)
			picType = picType.substring(0, picType.indexOf("?"));
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
				if (!item.isFormField())
				{
					// processUploadedFile
					String fieldName = item.getFieldName();
					String fileName = item.getName();
					int i = fileName.lastIndexOf("\\");
					if (i > -1)
						fileName = fileName.substring(i + 1);

					File file = new File(VdbManager.getEngine().getRootPath()
							+ "/userfiles/" + picType + "/" + fileName);
					item.write(file);
					((HttpServletRequest) request).getSession().setAttribute(
							"FileUpload.Progress."
									+ ((HttpServletRequest) request)
											.getSession().getId(), "-1");
					request.setAttribute("path", fileName);
					request.setAttribute("type", picType);
				}

			}

		}
	}
}
