package vdb.report.resstat.filestats.vo;

import java.util.Map;

/**
 * 存储位置的指标信息
 * 
 * @author 苏贤明
 * 
 */
public class RepositoryIndicator
{
	/**
	 * 数据集的ID
	 */
	private String dsid;

	/**
	 * 数据集的URI
	 */
	private String datasetUri;

	/**
	 * 存储位置名称
	 */
	private String repositoryName;

	/**
	 * 存储位置的总大小
	 */
	private long bytes;

	/**
	 * 存储位置的文件个数
	 */
	private long items;

	/**
	 * 目录个数
	 */
	private long directoryNum;

	/**
	 * 最大文件大小
	 */
	private long maxFileBytes;

	/**
	 * 最小文件大小
	 */
	private long minFileBytes;

	/**
	 * 最大目录深度
	 */
	private int maxDirDepth;

	/**
	 * 最新统计时间
	 */
	private String lastStatsTime;

	/**
	 * 存储位置下的文件统计指标
	 */
	private Map<String, FileIndicator> fileIndicatorMap;

	public String getDsid()
	{
		return dsid;
	}

	public void setDsid(String dsid)
	{
		this.dsid = dsid;
	}

	public String getDatasetUri()
	{
		return datasetUri;
	}

	public void setDatasetUri(String datasetUri)
	{
		this.datasetUri = datasetUri;
	}

	public String getRepositoryName()
	{
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName)
	{
		this.repositoryName = repositoryName;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public long getItems()
	{
		return items;
	}

	public void setItems(long items)
	{
		this.items = items;
	}

	public long getDirectoryNum()
	{
		return directoryNum;
	}

	public void setDirectoryNum(long directoryNum)
	{
		this.directoryNum = directoryNum;
	}

	public long getMaxFileBytes()
	{
		return maxFileBytes;
	}

	public void setMaxFileBytes(long maxFileBytes)
	{
		this.maxFileBytes = maxFileBytes;
	}

	public long getMinFileBytes()
	{
		return minFileBytes;
	}

	public void setMinFileBytes(long minFileBytes)
	{
		this.minFileBytes = minFileBytes;
	}

	public int getMaxDirDepth()
	{
		return maxDirDepth;
	}

	public void setMaxDirDepth(int maxDirDepth)
	{
		this.maxDirDepth = maxDirDepth;
	}

	public String getLastStatsTime()
	{
		return lastStatsTime;
	}

	public void setLastStatsTime(String lastStatsTime)
	{
		this.lastStatsTime = lastStatsTime;
	}

	public Map<String, FileIndicator> getFileIndicatorMap()
	{
		return fileIndicatorMap;
	}

	public void setFileIndicatorMap(Map<String, FileIndicator> fileIndicatorMap)
	{
		this.fileIndicatorMap = fileIndicatorMap;
	}

}
