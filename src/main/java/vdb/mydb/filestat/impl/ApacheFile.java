package vdb.mydb.filestat.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class ApacheFile implements RepositoryFile
{

	FileRepository repository;
	boolean isDirectory;
	boolean isFile;
	String filePath;
	String fileName;
	long length;

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		if (this.isFile())
		{
			String postfix = "";
			postfix = getFileExtension();
			String contentType = "";
			contentType = FileUtil.getContentType(postfix);
			return contentType;
		}

		return null;
	}

	public Date getLastModified()
	{
		return null;
	}

	public String getFileName()
	{
		if (this.isFile())
		{
			return fileName;
		}
		return null;
	}

	// 返回文件或文件夹的相对路径
	public String getFilePath()
	{
		String repositoryPath = ((ApacheRepository) this.getRepository())
				.getPath();
		return filePath.substring(filePath.indexOf(repositoryPath)
				+ repositoryPath.length());
	}

	public long length()
	{
		if (this.isFile())
		{
			return length;
		}
		return 0;
	}

	public String getFileExtension()
	{
		if (isFile() && this.getFileName().lastIndexOf(".") != -1)
			return this.getFileName().substring(
					this.getFileName().lastIndexOf("."));

		return null;
	}

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public boolean isFile()
	{
		return isFile;
	}

	public List<RepositoryFile> listFiles()
	{
		String path = filePath;
		List<RepositoryFile> files = new ArrayList<RepositoryFile>();

		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = null;
		ApacheFile af = null;
		try
		{
			path = path.replaceAll(" ", "%20");
			getMethod = new GetMethod(path);
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK)
			{
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				return null;
			}

			String html = new String(getMethod.getResponseBody());
			Parser parser = Parser.createParser(html, "utf-8");
			HtmlPage page = new HtmlPage(parser);

			try
			{
				parser.visitAllNodesWith(page);
			}
			catch (ParserException e)
			{
				e.printStackTrace();
			}

			// 所有的节点
			NodeList nodeList = page.getBody();

			// 建立一个表格节点tableFilter用于过滤table节点
			NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
			// 得到所有过滤后的节点
			nodeList = nodeList.extractAllNodesThatMatch(tableFilter, true);

			URL url;
			HttpURLConnection urlcon;
			TableTag tag;
			TableRow[] rows;
			TableRow tr;
			TableColumn[] td;
			Boolean isFile;
			Boolean isDirectory;
			NodeFilter hrefFilter;
			NodeList nodelist;
			LinkTag link;
			for (int i = 0; i <= nodeList.size(); i++)
			{
				if (nodeList.elementAt(i) instanceof TableTag)
				{
					tag = (TableTag) nodeList.elementAt(i);
					rows = tag.getRows();

					for (int j = 3; j < rows.length; j++)
					{
						tr = (TableRow) rows[j];
						td = tr.getColumns();

						isFile = false;
						isDirectory = false;

						// 判断该行是文件还是目录
						for (int k = 0; k < td.length; k++)
						{
							if (td[k].getChildrenHTML().indexOf(
									"/icons/back.gif") > -1)
								break;
							else if (td[k].getChildrenHTML().indexOf(
									"/icons/folder.gif") > -1)
							{
								isDirectory = true;
								break;
							}
							else if (k == td.length - 1)
							{
								isFile = true;
							}
						}

						hrefFilter = new TagNameFilter("A");

						for (int k = 0; k < td.length; k++)
						{
							nodelist = td[k].getChildren();
							nodelist = nodelist.extractAllNodesThatMatch(
									hrefFilter, true);
							for (int n = 0; n < nodelist.size(); n++)
							{
								link = (LinkTag) nodelist.elementAt(n);

								af = new ApacheFile();
								af.setRepository(this.getRepository());
								af.setDirectory(isDirectory);
								af.setFile(isFile);
								af.setFilePath(path + "/"
										+ link.getStringText());
								if (isFile)
								{
									af.setFileName(link.getStringText());
									url = new URL(path + af.getFilePath());
									urlcon = (HttpURLConnection) url
											.openConnection();
									af.setLength(urlcon.getContentLength());
									urlcon.disconnect();
									files.add(af);
								}
								if (isDirectory)
									files.add(af);

							}
						}
					}
				}
			}
			// 将变量置空
			html = null;
			parser = null;
			page = null;
			nodeList = null;
			tableFilter = null;
			url = null;
			urlcon = null;
			tag = null;
			rows = null;
			tr = null;
			td = null;
			isFile = null;
			isDirectory = null;
			hrefFilter = null;
			nodelist = null;
			link = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			// 将变量置空
			path = null;
			af = null;
			// 释放连接
			if (getMethod != null)
				getMethod.releaseConnection();

			getMethod = null;
			httpClient = null;
		}

		return files;
	}

	public FileRepository getRepository()
	{
		return repository;
	}

	public void setRepository(FileRepository repository)
	{
		this.repository = repository;
	}

	public String getFileTitle()
	{
		return getFileName();
	}

	public void setDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}

	public void setFile(boolean isFile)
	{
		this.isFile = isFile;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public void setLength(long length)
	{
		this.length = length;
	}

}