package vdb.report.resstat.filestats.vo;

/**
 * 文件类型统计指标类
 * 
 * @author 苏贤明
 * 
 */
public class FileIndicator
{
	/**
	 * 文件所属存储位置指标
	 */
	private RepositoryIndicator repositoryIndicator;

	/**
	 * 文件类型
	 */
	private String contentType;

	/**
	 * 文件后缀名
	 */
	private String fileExtension;

	/**
	 * 文件个数
	 */
	private long items;

	/**
	 * 文件字节数
	 */
	private long bytes;

	/**
	 * 最后统计时间
	 */
	private String lastStatsTime;

	public RepositoryIndicator getRepositoryIndicator()
	{
		return repositoryIndicator;
	}

	public void setRepositoryIndicator(RepositoryIndicator repositoryIndicator)
	{
		this.repositoryIndicator = repositoryIndicator;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	public long getItems()
	{
		return items;
	}

	public void setItems(long items)
	{
		this.items = items;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public String getLastStatsTime()
	{
		return lastStatsTime;
	}

	public void setLastStatsTime(String lastStatsTime)
	{
		this.lastStatsTime = lastStatsTime;
	}
}
