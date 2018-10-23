/*
 * Created on 2006-11-16
 */
package vdb.mydb.jsp;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import cn.csdb.commons.util.HttpFile;

/*
 * ��servlet���������ļ���֧�ֶϵ���
 * 
 * @author bluejoe
 */
public class FileServlet extends HttpServlet
{
	private String _errorPage;

	@Override
	public void init(ServletConfig arg0) throws ServletException
	{
		super.init(arg0);

		_errorPage = arg0.getInitParameter("errorPage");
		if (_errorPage == null)
			_errorPage = "/console/shared/error.vpage";
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		// url: /files/<dataset uri>/<file id><extension>
		String requri = ((HttpServletRequest) request).getRequestURI();
		String fileType = ((HttpServletRequest) request).getParameter("type");
		String prefix = "/files/";
		if (requri.startsWith(prefix))
		{
			int i1 = prefix.length();
			int i2 = requri.lastIndexOf("/");

			if (i2 > i1)
			{
				String dsuri = requri.substring(i1, i2);
				DataSet ds = VdbManager.getInstance().getCatalog().fromUri(
						dsuri);

				int i3 = requri.lastIndexOf(".");
				if (i3 > i2)
				{
					String fileId = requri.substring(i2 + 1, i3);

					FileManager manager = VdbManager.getInstance()
							.getFileManager();

					try
					{
						FileMetaData fi = manager.getFile(ds, fileId);
						response.setContentType(fi.getContentType());
						String fileName = fi.getTitle() + fi.getExtension();
						if (!fi.getContentType().toLowerCase().startsWith(
								"image/"))
						{
							((HttpServletResponse) response).addHeader(
									"Content-Disposition",
									"attachment; filename="
											+ new String(fileName.getBytes(),
													"iso-8859-1"));
						}
						InputStream is = null;
						if (fileType != null && "localfiles".equals(fileType))
						{
							is = fi.getLocalFileStream();
						}
						else
						{
							is = fi.getFileStream();
						}
						new HttpFile(is, fi.getFileSize()).download(
								(HttpServletRequest) request,
								(HttpServletResponse) response);
						is.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}