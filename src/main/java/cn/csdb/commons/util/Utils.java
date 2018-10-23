/*
 * Created on 2003-6-16
 * 
 */
package cn.csdb.commons.util;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bluejoe
 */
public class Utils
{
	static Map _contentTypes = new HashMap();

	static
	{
		_contentTypes.put(".html", "text/html");
		_contentTypes.put(".aif", "audio/x-aiff");
		_contentTypes.put(".aiff", "audio/x-aiff");
		_contentTypes.put(".aifc", "audio/x-aiff");
		_contentTypes.put(".ai", "application/postscript");
		_contentTypes.put(".au", "audio/basic");
		_contentTypes.put(".asc", "text/plain");
		_contentTypes.put(".asf", "video/x-ms-asf");
		_contentTypes.put(".asx", "video/x-ms-asf");
		_contentTypes.put(".avi", "video/x-msvideo");

		_contentTypes.put(".bin", "application/octet-stream");
		_contentTypes.put(".bcpio", "application/x-bcpio");
		_contentTypes.put(".bmp", "image/bmp");

		_contentTypes.put(".class", "application/octet-stream");
		_contentTypes.put(".cpt", "application/mac-compactpro");
		_contentTypes.put(".css", "text/css");
		_contentTypes.put(".cpio", "application/x-cpio");
		_contentTypes.put(".csh", "application/x-csh");
		_contentTypes.put(".cdf", "application/x-netcdf");

		_contentTypes.put(".dms", "application/octet-stream");
		_contentTypes.put(".doc", "application/msword");
		_contentTypes.put(".dcr", "application/x-director");
		_contentTypes.put(".dir", "application/x-director");
		_contentTypes.put(".dxr", "application/x-director");
		_contentTypes.put(".dvi", "application/x-dvi");

		_contentTypes.put(".exe", "application/octet-stream");
		_contentTypes.put(".eps", "application/postscript");
		_contentTypes.put(".etx", "text/x-setext");

		_contentTypes.put(".gtar", "application/x-gtar");
		_contentTypes.put(".gif", "image/gif");
		_contentTypes.put(".gz", "application/octet-stream");

		_contentTypes.put(".hdml", "text/x-hdml");
		_contentTypes.put(".hqx", "application/mac-binhex40");
		_contentTypes.put(".html", "text/html");
		_contentTypes.put(".htm", "text/html");
		_contentTypes.put(".hdf", "application/x-hdf");

		_contentTypes.put(".ief", "image/ief");
		_contentTypes.put(".ice", "x-conference/x-cooltalk");

		_contentTypes.put(".jar", "application/java-archive");
		_contentTypes.put(".jnlp", "application/x-java-jnlp-file");
		_contentTypes.put(".jpeg", "image/jpeg");
		_contentTypes.put(".jpg", "image/jpeg");
		_contentTypes.put(".jpe", "image/jpeg");
		_contentTypes.put(".js", "application/x-javascript");

		_contentTypes.put(".kar", "audio/midi");

		_contentTypes.put(".latex", "application/x-latex");
		_contentTypes.put(".lha", "application/octet-stream");
		_contentTypes.put(".lhz", "application/octet-stream");

		_contentTypes.put(".mid", "audio/midi");
		_contentTypes.put(".mpeg", "video/mpeg");
		_contentTypes.put(".mpg", "video/mpeg");
		_contentTypes.put(".mpe", "video/mpeg");
		_contentTypes.put(".mov", "video/quicktime");
		_contentTypes.put(".movie", "video/x-sgi-movie");
		_contentTypes.put(".mpga", "audio/mpeg");
		_contentTypes.put(".mp2", "audio/mpeg");
		_contentTypes.put(".mp3", "audio/mpeg");
		_contentTypes.put(".man", "application/x-troff-man");
		_contentTypes.put(".me", "application/x-troff-me");
		_contentTypes.put(".ms", "application/x-troff-ms");

		_contentTypes.put(".nc", "application/x-netcdf");

		_contentTypes.put(".oda", "application/oda");

		_contentTypes.put(".pdf", "application/pdf");
		_contentTypes.put(".ps", "application/postscript");
		_contentTypes.put(".ppt", "application/vnd.ms-powerpoint");
		_contentTypes.put(".png", "image/png");
		_contentTypes.put(".pgn", "application/x-chess-pgn");
		_contentTypes.put(".pnm", "image/x-portable-anymap");
		_contentTypes.put(".pbm", "image/x-portable-bitmap");
		_contentTypes.put(".pgm", "image/x-portable-graymap");
		_contentTypes.put(".ppm", "image/x-portable-pixmap");

		_contentTypes.put(".qt", "video/quicktime");

		_contentTypes.put(".rtf", "application/rtf");
		_contentTypes.put(".ram", "audio/x-pn-realaudio");
		_contentTypes.put(".rm", "audio/x-pn-realaudio");
		_contentTypes.put(".rpm", "audio/x-pn-realaudio-plugin");
		_contentTypes.put(".ra", "audio/x-realaudio");
		_contentTypes.put(".ras", "image/x-cmu-raster");
		_contentTypes.put(".rgb", "image/x-rgb");
		_contentTypes.put(".rtx", "text/richtext");
		_contentTypes.put(".rtf", "text/rtf");

		_contentTypes.put(".smi", "application/smil");
		_contentTypes.put(".smil", "application/smil");
		_contentTypes.put(".sml", "application/smil");
		_contentTypes.put(".skp", "application/x-koan");
		_contentTypes.put(".skd", "application/x-koan");
		_contentTypes.put(".skt", "application/x-koan");
		_contentTypes.put(".skm", "application/x-koan");
		_contentTypes.put(".src", "application/x-wais-source");
		_contentTypes.put(".sh", "application/x-sh");
		_contentTypes.put(".shar", "application/x-shar");
		_contentTypes.put(".swf", "application/x-shockwave-flash");
		_contentTypes.put(".sit", "application/x-stuffit");
		_contentTypes.put(".spl", "application/x-futuresplash");
		_contentTypes.put(".sv4cpio", "application/x-sv4cpio");
		_contentTypes.put(".sv4crc", "application/x-sv4crc");
		_contentTypes.put(".snd", "audio/basic");
		_contentTypes.put(".sgml", "text/sgml");
		_contentTypes.put(".sgm", "text/sgml");

		_contentTypes.put(".t", "application/x-troff");
		_contentTypes.put(".tar", "application/x-tar");
		_contentTypes.put(".tcl", "application/x-tcl");
		_contentTypes.put(".tex", "application/x-tex");
		_contentTypes.put(".texi", "application/x-texinfo");
		_contentTypes.put(".texinfo", "application/x-texinfo");
		_contentTypes.put(".tgz", "application/octet-stream");
		_contentTypes.put(".tiff", "image/tiff");
		_contentTypes.put(".tif", "image/tiff");
		_contentTypes.put(".torrent", "application/x-bittorrent");
		_contentTypes.put(".tr", "application/x-troff");
		_contentTypes.put(".troff", "application/x-troff");
		_contentTypes.put(".tsv", "text/tab-separated-values");
		_contentTypes.put(".txt", "text/plain");

		_contentTypes.put(".ustar", "application/x-ustar");

		_contentTypes.put(".vcd", "application/x-cdlink");
		_contentTypes.put(".vrml", "model/vrml");

		_contentTypes.put(".wav", "audio/x-wav");
		_contentTypes.put(".wax", "audio/x-ms-wax");
		_contentTypes.put(".wrl", "model/vrml");
		_contentTypes.put(".wma", "audio/x-ms-wma");
		_contentTypes.put(".wml", "text/vnd.wap.wml");
		_contentTypes.put(".wmls", "text/vnd.wap.wmlscript");
		_contentTypes.put(".wmlc", "application/vnd.wap.wmlc");
		_contentTypes.put(".wmlsc", "application/vnd.wap.wmlscript");
		_contentTypes.put(".wbmp", "image/vnd.wap.wbmp");

		_contentTypes.put(".xls", "application/vnd.ms-excel");
		_contentTypes.put(".xbm", "image/x-xbitmap");
		_contentTypes.put(".xpm", "image/x-xpixmax");
		_contentTypes.put(".xwd", "image/x-xwindowdump");
		_contentTypes.put(".xml", "text/xml");

		_contentTypes.put(".zip", "application/zip");
		_contentTypes.put(".z", "application/octet-stream");

	}

	/**
	 * 根据后缀名判断文件的ContentType
	 * 
	 * @param extension
	 * @return
	 */
	public static String getContentType(String extension)
	{
		String defaultType = "application/octet-stream";
		if (extension.length() < 1)
		{
			return defaultType;
		}

		extension = extension.toLowerCase();
		String contentType = (String) _contentTypes.get(extension.substring(1));
		if (contentType != null)
			return contentType;

		return defaultType;
	}

	/**
	 * @deprecated
	 */
	public static String getDigestPassword(String userid, String password)
			throws NoSuchAlgorithmException
	{
		return StringUtils.getDigestPassword(userid, password);
	}

	/**
	 * 将数字转换成以,分割的文本
	 * 
	 * @param number
	 */
	public static String getDottedNumber(long number)
	{
		return MessageFormat.format("{0,number,###,###}",
				new Object[] { new Long(number) });
	}

	/**
	 * 获取文件/文件夹大小
	 * 
	 * @param filePath
	 */
	public static long getFileSize(String filePath)
	{
		long size = 0;
		File file = new File(filePath);
		if (file.isFile())
			return file.length();

		if (file.isDirectory())
		{
			String[] files = file.list();
			for (int i = 0; i < files.length; i++)
			{
				size += getFileSize(filePath + "/" + files[i]);
			}
		}

		return size;
	}
}
