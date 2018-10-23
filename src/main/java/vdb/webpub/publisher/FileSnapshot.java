package vdb.webpub.publisher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import vdb.mydb.VdbManager;
import cn.csdb.commons.util.StringUtils;
import cn.csdb.commons.util.TimeUtils;

public class FileSnapshot
{
	public static String digest(File file)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(file);
			while (true)
			{
				byte[] bytes = new byte[20480];
				int read = fis.read(bytes, 0, bytes.length);
				if (read == -1)
					break;
				baos.write(bytes, 0, read);
			}

			fis.close();
			baos.close();

			return StringUtils.md5(baos.toString());
		}
		catch (Exception e)
		{
			return "";
		}
	}

	private String _digest;

	private long _fileLength;

	/**
	 * relative to root path, not webroot
	 */
	private String _filePath;

	private String _lastModified;

	public void delete()
	{
		getFile().delete();
	}

	public String getDigest()
	{
		return _digest;
	}

	private File getFile()
	{
		File file = new File(VdbManager.getEngine().getApplicationRoot(),
				_filePath);
		return file;
	}

	public long getFileLength()
	{
		return _fileLength;
	}

	public String getFilePath()
	{
		return _filePath;
	}

	public String getLastModified()
	{
		return _lastModified;
	}

	public boolean matchesDigest()
	{
		return digest(getFile()).equalsIgnoreCase(_digest);
	}

	public void setDigest(String digest)
	{
		_digest = digest;
	}

	public void setFileLength(long fileLength)
	{
		_fileLength = fileLength;
	}

	public void setFilePath(String filePath)
	{
		_filePath = filePath;
	}

	public void setLastModified(String lastModified)
	{
		_lastModified = lastModified;
	}

	public void snap(String relativeTargetPath) throws IOException
	{
		setFilePath(relativeTargetPath);

		File file = getFile();
		setLastModified(TimeUtils.getTimeString(new Date(file.lastModified())));
		// setFileLength(file.length());
		// setDigest(digest(file));
		setFileLength(0);
		setDigest("@DIGEST");
	}

	@Override
	public String toString()
	{
		return _filePath;
	}
}
