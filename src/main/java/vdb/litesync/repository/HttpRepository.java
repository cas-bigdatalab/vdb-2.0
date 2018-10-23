package vdb.litesync.repository;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpRepository implements SyncRepository
{
	private String _entryPathPattern;

	private String _baseUrl;

	private String _versionFilePathPattern;

	public InputStream getVersionFileStream(String path)
			throws InvalidPathException
	{
		String url = _versionFilePathPattern.replaceAll("\\$path", path);
		return getHttpStream(url);
	}

	private InputStream getHttpStream(String path) throws InvalidPathException
	{
		if (!path.startsWith("/"))
		{
			throw new InvalidPathException(path);
		}

		HttpClient httpClient = new HttpClient();
		try
		{
			GetMethod getMethod = new GetMethod(String.format("%s%s", _baseUrl,
					path));
			httpClient.executeMethod(getMethod);

			if (getMethod.getStatusCode() != HttpStatus.SC_OK)
			{
				return null;
			}

			return getMethod.getResponseBodyAsStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public InputStream getEntryInputStream(String path)
			throws InvalidPathException
	{
		String url = _entryPathPattern.replaceAll("\\$path", path);
		return getHttpStream(url);
	}

	public InputStream getSummaryInputStream() throws IOException
	{
		return getVersionFileStream("local.summary");
	}

	public String getBaseUrl()
	{
		return _baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		_baseUrl = baseUrl;
		if (_baseUrl.endsWith("/"))
		{
			_baseUrl = _baseUrl.substring(0, _baseUrl.length() - 1);
		}
	}

	public String getEntryPathPattern()
	{
		return _entryPathPattern;
	}

	public void setEntryPathPattern(String entryPathPattern)
	{
		_entryPathPattern = entryPathPattern;
	}

	public String getVersionFilePathPattern()
	{
		return _versionFilePathPattern;
	}

	public void setVersionFilePathPattern(String versionFilePathPattern)
	{
		_versionFilePathPattern = versionFilePathPattern;
	}

	public boolean isAvaliable()
	{
		HttpClient httpClient = new HttpClient();
		try
		{
			GetMethod getMethod = new GetMethod(_baseUrl);
			httpClient.executeMethod(getMethod);
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}
}
