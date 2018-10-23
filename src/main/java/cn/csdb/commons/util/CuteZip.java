package cn.csdb.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩（解压）类。
 */

public class CuteZip
{
	public final String _tempDirectoryName = "_cutezip_temporary_directory_";

	public final String _tempZipName = "_cutezip_temporary_file_.zip";

	/**
	 * 进行解析文件夹和压缩工作
	 * 
	 * @param:String srcFileName 源文件 e:\\xxxx\\xxxx.xxx or e:\\xxxx 文件或者文件夹
	 *               String comment 个性化说明
	 * @return:int 压缩文件的个数(空文件夹算1)
	 * @since: 2004-03
	 * @version 1.0
	 */
	private int doZipDir(ZipOutputStream out, String srcFileName,
			String entryPath, String comment)
	{
		int fileNum = 0;
		srcFileName = srcFileName.replace('\\', '/');
		entryPath = entryPath.replace('\\', '/');
		if (srcFileName.lastIndexOf("/") == -1)
			return 0;

		try
		{
			// 实例化源文件
			File file = new File(srcFileName);
			if (!file.exists() && !file.isDirectory())
				return fileNum;

			String[] fileName = file.list(); // 这个不需要中文转换了,使用了本地编码：）
			if (fileName.length == 0) // 空文件夹
			{
				// 为读出的数据创建一个zip条目表
				ZipEntry entry = new ZipEntry(new String((entryPath + "/")
						.getBytes("ISO_8859_1"), "GBK"));
				// 将zip条目列表写入输出流
				out.putNextEntry(entry);
				if (comment != null && !comment.equalsIgnoreCase(""))
					out.setComment(comment);
				out.closeEntry();
			}
			else
			{
				for (int i = 0; i < fileName.length; i++)
				{
					File f1 = new File(srcFileName, fileName[i]);
					// 如果是文件，进行压缩
					if (f1.isFile())
					{
						fileNum++;
						// 加文件路径，否则找不到文件!!
						String tempName = srcFileName + "/" + fileName[i];
						// 设置文件输入流
						FileInputStream in = new FileInputStream(tempName);
						BufferedInputStream orign = new BufferedInputStream(in,
								5120);
						// 为读出的数据创建一个zip条目表
						ZipEntry entry = new ZipEntry(new String((entryPath
								+ "/" + fileName[i]).getBytes("ISO_8859_1"),
								"GBK"));
						// 将zip条目列表写入输出流
						out.putNextEntry(entry);
						if (comment != null && !comment.equalsIgnoreCase(""))
							out.setComment(comment);
						int bufferSize = 0;
						byte[] buffer = new byte[5120];
						while ((bufferSize = orign.read(buffer)) != -1)
						{
							out.write(buffer, 0, bufferSize);
						}
						orign.close();
						out.closeEntry();
					}
					// 如果是子目录进行递归调用！
					else
					{
						String sub = f1.getAbsolutePath();
						fileNum += doZipDir(out, sub, entryPath + "/"
								+ fileName[i], comment);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileNum;
	}

	/**
	 * 进行解析文件和压缩工作
	 * 
	 * @param:String srcFileName 源文件 e:\\xxxx or e:\\xxxx 必须是文件 String comment
	 *               个性化说明
	 * @return:int 压缩文件的个数
	 * @since: 2004-03
	 * @version 1.0
	 */
	private boolean doZipFile(ZipOutputStream out, String srcFileName,
			String comment)
	{
		if (out == null)
			return false;
		srcFileName = srcFileName.replace('\\', '/');
		if (srcFileName.lastIndexOf("/") == -1)
			return false;

		boolean ret = true;
		try
		{
			// 设置文件输入流
			FileInputStream in = new FileInputStream(srcFileName);
			BufferedInputStream orign = new BufferedInputStream(in, 5120);
			// 为读出的数据创建一个zip条目表
			String entryName = srcFileName.substring(srcFileName
					.lastIndexOf("/") + 1);
			ZipEntry entry = new ZipEntry(new String((entryName + "/")
					.getBytes("GBK"), "ISO_8859_1"));
			// 将zip条目列表写入输出流
			out.putNextEntry(entry);
			if (comment != null && !comment.equalsIgnoreCase(""))
				out.setComment(comment);
			int bufferSize = 0;
			byte[] buffer = new byte[5120];
			while ((bufferSize = orign.read(buffer)) != -1)
			{
				out.write(buffer, 0, bufferSize);
			}
			orign.close();
			out.closeEntry();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	/**
	 * 解压缩对应的压缩文件
	 * 
	 * @param:String filePath 压缩文件的绝对路径,String nickPath 解压后文件的绝对路径目录
	 * @return:void
	 * @since: 2004-03
	 * @version 1.0
	 */
	public void unzip(String filePath, String nickPath)
	{
		filePath = filePath.replace('\\', '/');
		nickPath = nickPath.replace('\\', '/'); // 统一处理\\和/方式
		if (filePath.indexOf("/") == -1 || nickPath.indexOf("/") == -1)
		{
			return;
		}

		if (filePath.charAt(filePath.length() - 1) == '/')
		{
			filePath = filePath.substring(0, filePath.length() - 1);
		}

		if (nickPath.charAt(nickPath.length() - 1) == '/')
		{
			nickPath = nickPath.substring(0, nickPath.length() - 1);
		}

		File fl = new File(filePath);
		if (!fl.exists())
		{
			return;
		}

		try
		{
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(fl);
			Enumeration e = zipfile.entries();
			File outFile = new File(nickPath);
			outFile.mkdir();
			while (e.hasMoreElements())
			{
				entry = (ZipEntry) e.nextElement();
				String tempFileName = nickPath
						+ System.getProperty("file.separator")
						+ entry.getName();
				File tempFile = new File(tempFileName);
				if (entry.isDirectory())
				{
					tempFile.mkdir();
					continue;
				}
				else
				{
					File aa = new File(tempFile.getParent());
					aa.mkdirs();
				}

				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[5120];
				FileOutputStream fos = new FileOutputStream(tempFile);
				dest = new BufferedOutputStream(fos, 5120);
				while ((count = is.read(data, 0, 5120)) != -1)
				{
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 将源文件(文件夹)压缩成名字为destFileName的压缩包
	 * 
	 * @param:String srcFileName 源文件 e:\\xxxx\\xxxx.xxx or e:\\xxxx,String
	 *               destFileName 目的压缩包名 e:\\xxx.rar or e:\\xxx.zip(如果想用winrar
	 *               或者 zip打开,请直接使用.rar或者.zip,最好不要使用现有的文件后缀eg:.txt之类)
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String srcFileName, String destFileName)
	{
		return zip(srcFileName, destFileName, null);
	}

	/**
	 * 将源文件(文件夹)压缩成名字为destFileName的压缩包
	 * 
	 * @param:String srcFileName 源文件 e:\\xxxx\\xxxx.xxx or e:\\xxxx,String
	 *               destFileName 目的压缩包名 e:\\xxx.rar or e:\\xxx.zip(如果想用winrar
	 *               或者 zip打开,请直接使用.rar或者.zip,最好不要使用现有的文件后缀eg:.txt之类) String
	 *               comment 个性化说明
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String srcFileName, String destFileName, String comment)
	{
		boolean ret = true;
		int fileNum = 0;
		try
		{
			if (srcFileName == null || srcFileName.equalsIgnoreCase("")
					|| destFileName == null
					|| destFileName.equalsIgnoreCase(""))
				return false;
			srcFileName = srcFileName.replace('\\', '/'); // 统一处理\\和/方式
			if (srcFileName.charAt(srcFileName.length() - 1) == '/')
				srcFileName = srcFileName
						.substring(0, srcFileName.length() - 1);

			if (destFileName.charAt(destFileName.length() - 1) == '/')
				destFileName = destFileName.substring(0,
						destFileName.length() - 1);

			File file = new File(srcFileName); // 判断源文件是否存在,不存在则不产生目标文件
			if (!file.exists())
				return false;
			FileOutputStream dest = new FileOutputStream(destFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			if (file.isDirectory())
				fileNum = doZipDir(out, srcFileName, srcFileName
						.substring(srcFileName.lastIndexOf("/") + 1), comment);
			else if (file.isFile())
			{
				if (doZipFile(out, srcFileName, comment))
					fileNum++;
			}

			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	/**
	 * 压缩同时可以添加一个文件,并将文件放到指定压缩src等同的一级目录
	 * 
	 * @param:String priSrc 主压缩源 String addSrc 附加压缩文件(如果不存在就不附加) String dest目标
	 *               String comment 个性化标语
	 * @return:boolean
	 * @since: 2004-03
	 * @version 1.0
	 */
	public boolean zip(String priSrc, String addSrc, String destPath,
			String comment)
	{
		if (priSrc.equalsIgnoreCase(destPath))
			return false;
		if (addSrc.equalsIgnoreCase(destPath))
			return false;
		priSrc = priSrc.replace('\\', '/');
		addSrc = addSrc.replace('\\', '/'); // 统一处理\\和/方式
		String priName = "";
		String addName = "";
		boolean isFile = false;
		try
		{
			File pri = new File(priSrc);
			boolean priFile = pri.isFile();
			boolean isPriExists = pri.exists();
			if (!isPriExists)
				return false;
			priName = pri.getName();
			pri = new File(addSrc);
			addName = pri.getName();
			boolean isAddExists = true;
			if (pri.exists())
			{
				isAddExists = true;
				if (pri.isDirectory())
				{
					isFile = false;
				}
				else if (pri.isFile())
				{
					isFile = true;
				}
			}
			else
			{
				isAddExists = false;
			}

			FileOutputStream dest = new FileOutputStream(destPath);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			if (priFile)
				doZipFile(out, priSrc, comment);
			else
				doZipDir(out, priSrc, priName, comment);
			if (isPriExists && isAddExists)
			{
				if (isFile)
					doZipFile(out, addSrc, comment);
				else
					doZipDir(out, addSrc, addName, comment);
			}
			if (isPriExists)
				out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}