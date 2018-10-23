package vdb.mydb.filestat.download;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import vdb.mydb.filestat.tool.FilesTool;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;

public class FileDownLoad implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		String uri = (String) vc.getRequest().getParameter("uri");
		String filePath = (String) vc.getRequest().getParameter("filePath");
		String repositoryName = (String) vc.getRequest().getParameter(
				"repositoryName");
		String fileTitle = (String) vc.getRequest().getParameter("fileTitle");
		String fileExtension = (String) vc.getRequest().getParameter(
				"fileExtension");

		String fileName = "";
		try
		{
			if (fileTitle != null && !fileTitle.trim().equalsIgnoreCase("")
					&& fileExtension != null
					&& !fileExtension.trim().equalsIgnoreCase(""))
			{
				fileName = new String(fileTitle.getBytes("gb2312"), "ISO8859-1")
						+ fileExtension;
			}
			else
			{
				if (filePath.lastIndexOf("/") > 0)
				{
					fileName = new String(filePath.substring(
							filePath.lastIndexOf("/") + 1, filePath.length())
							.getBytes("gb2312"), "ISO8859-1");
				}
				else if (filePath.lastIndexOf("\\") > 0)
				{
					fileName = new String(filePath.substring(
							filePath.lastIndexOf("\\") + 1, filePath.length())
							.getBytes("gb2312"), "ISO8859-1");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// 打开指定文件的流信息
		InputStream is = new FilesTool().getRepository(uri, repositoryName)
				.openStream(filePath);
		BufferedInputStream br = new BufferedInputStream(is);

		// 设置响应头和保存文件名
		HttpServletResponse response = vc.getResponse();
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		// 写出流信息
		int b = 0;
		try
		{
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[10240];
			while ((b = br.read(buf)) != -1)
			{
				out.write(buf, 0, b);
			}
			br.close();
			is.close();
			out.close();
			System.out.println("文件下载完毕.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("下载文件失败!");
		}
	}
}
