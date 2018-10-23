package vdb.mydb.filestat.impl;

import java.text.DecimalFormat;

public class FileUtil
{
	public static String formatFileSize(long fileSize)
	{
		// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024)
		{
			fileSizeString = df.format((double) fileSize) + "B";
		}
		else if (fileSize < 1048576)
		{
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		}
		else if (fileSize < 1073741824)
		{
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		}
		else
		{
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static String getContentType(String postfix)
	{
		String contentType = "";
		if (postfix != null)
		{
			postfix = postfix.toUpperCase();
			if (postfix.equals(".XLS") || postfix.equals(".XLT")
					|| postfix.equals(".XLW") || postfix.equals(".CSV"))
			{
				contentType = "application/vnd.ms-excel";
			}
			else if (postfix.equals(".DOC"))
			{
				contentType = "application/msword";
			}
			else if (postfix.equals(".RTF"))
			{
				contentType = "application/rtf";
			}
			else if (postfix.equals(".TEXT") || postfix.equals(".TXT"))
			{
				contentType = "text/plain";
			}
			else if (postfix.equals(".XML"))
			{
				contentType = "application/xml";
			}
			else if (postfix.equals(".BMP"))
			{
				contentType = "image/bmp";
			}
			else if (postfix.equals(".JPG") || postfix.equals(".JPEG"))
			{
				contentType = "image/jpeg";
			}
			else if (postfix.equals(".GIF"))
			{
				contentType = "image/gif";
			}
			else if (postfix.equals(".AVI"))
			{
				contentType = "video/x-msvideo";
			}
			else if (postfix.equals(".MP3"))
			{
				contentType = "audio/mpeg";
			}
			else if (postfix.equals(".MPA") || postfix.equals(".MPE")
					|| postfix.equals(".MPEG") || postfix.equals(".MPG"))
			{
				contentType = "video/mpeg";
			}
			else if (postfix.equals(".PPT") || postfix.equals(".PPS"))
			{
				contentType = "application/vnd.ms-powerpoint";
			}
			else if (postfix.equals(".PDF"))
			{
				contentType = "application/pdf";
			}
			else if (postfix.equals(".ZIP") || postfix.equals(".RAR"))
			{
				contentType = "application/zip";
			}
		}
		return contentType;
	}
}
