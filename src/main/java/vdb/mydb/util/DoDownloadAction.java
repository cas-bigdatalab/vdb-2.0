package vdb.mydb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDownloadAction extends ServletActionProxy
{
	public static String getAttachName(String file_name)
	{
		if (file_name == null)
			return "";
		file_name = file_name.trim();
		int iPos = 0;
		iPos = file_name.lastIndexOf("\\");
		if (iPos > -1)
		{
			file_name = file_name.substring(iPos + 1);
		}
		iPos = file_name.lastIndexOf("/");
		if (iPos > -1)
		{
			file_name = file_name.substring(iPos + 1);
		}
		iPos = file_name.lastIndexOf(File.separator);
		if (iPos > -1)
		{
			file_name = file_name.substring(iPos + 1);
		}
		return file_name;
	}

	public static String togbkString(String s)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c >= 0 && c <= 255)
			{
				sb.append(c);
			}
			else
			{
				byte[] b;
				try
				{
					b = Character.toString(c).getBytes("utf-8");
				}
				catch (Exception ex)
				{
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++)
				{
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		String s_gbk = sb.toString();
		sb.delete(0, sb.length());
		sb.setLength(0);
		sb = null;
		return s_gbk;
	}

	public void doAction(ServletRequest req, ServletResponse resp,
			ServletContext arg2) throws Exception
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		String file_name = request.getParameter("file");

		if (file_name.substring(file_name.lastIndexOf("."), file_name.length())
				.contains("("))
		{
			file_name = file_name.substring(0, file_name.lastIndexOf("("));
		}

		String entityUri = request.getParameter("entityUri");
		Catalog catalog = VdbManager.getInstance().getCatalog();

		Entity entity = catalog.fromUri(entityUri);
		if (entity == null)
		{

			response.sendError(404);
			return;
		}

		String filePath = entity.getDataSet().getRepository()
				.getLocalFilePath();
		filePath = filePath.replace("$dsRoot", ((VdbDataSet) entity
				.getDataSet()).getDataSetXml().getParentFile()
				.getCanonicalPath());
		filePath = filePath.replace("\\", "/");
		String allPath = filePath + file_name;
		if (file_name == null)
			file_name = "";
		file_name = file_name.trim();
		String attch_name = "";
		try
		{
			attch_name = getAttachName(allPath);
			attch_name = togbkString(attch_name);

			response.reset();
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition",
					"attachment;   filename=\"" + attch_name + "\"");

			File file = new File(allPath);
			if (!file.exists())
			{
				response.sendError(404);
				return;
			}
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			int len;
			long p = 0;
			long l = file.length();
			if (l > 0)
			{
				// ���߿ͻ�������ϵ�����߳�l������, ��Ӧ�ĸ�ʽ��: Accept-Ranges: bytes
				response.setHeader("Accept-Ranges", "bytes");

				// ����ǵ�һ����,��û�жϵ���,״̬��Ĭ�ϵ� 200,������ʽ����, ��Ӧ�ĸ�ʽ��:
				// HTTP/1.1
				// 200
				String range = request.getHeader("Range");
				if (range != null) // �ͻ�����������ص��ļ���Ŀ�ʼ�ֽ�
				{
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
					range = range.toLowerCase().replace("bytes=", "");
					if (range.indexOf("-") >= 0)
					{
						range = range.substring(0, range.indexOf("-"));
					}
					p = Long.parseLong(range);
				}

				if (p > 0)
				{
					// ���Ǵ��ʼ����, ��Ӧ�ĸ�ʽ��:
					// Content-Range: bytes [�ļ���Ŀ�ʼ�ֽ�]-[�ļ����ܴ�С -
					// 1]/[�ļ����ܴ�С]
					String contentRange = new StringBuffer("bytes ").append(
							Long.valueOf(p).toString()).append("-").append(
							Long.valueOf(l - 1).toString()).append("/").append(
							Long.valueOf(l).toString()).toString();
					response.setHeader("Content-Range", contentRange);
					is.skip(p);
				}

				// ��Ӧ�ĸ�ʽ��: Content-Length: [�ļ����ܴ�С] -
				// [�ͻ�����������ص��ļ���Ŀ�ʼ�ֽ�]
				response.setContentLength((int) (l - p));// �ļ���С

				byte[] buff = new byte[1024 * 1024];// 1mb
				OutputStream os = new BufferedOutputStream(response
						.getOutputStream());

				while ((len = is.read(buff)) != -1)
				{
					os.write(buff, 0, len);
				}
				os.flush();
				response.flushBuffer();
				os.close();
			}
			is.close();
		}
		catch (Exception e)
		{
			response.reset();
			response.sendError(404);
			return;
		}

	}

}
