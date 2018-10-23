package vdb.report.resstat.vo;

import java.util.List;

import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;

/**
 * 数据集统计指标类
 * 
 * @author 苏贤明
 */
public class DatasetIndicator
{
	/**
	 * 数据集ID
	 */
	private String dsid;

	/**
	 * 数据集标题
	 */
	private String title;

	/**
	 * 数据集URI
	 */
	private String uri;

	/**
	 * 数据集对应的DBMS
	 */
	private String dbms;

	/**
	 * 数据集描述
	 */
	private String description;

	/**
	 * 数据库统计指标
	 */
	private DbIndicator dbIndicator;

	/**
	 * 存储位置统计指标列表
	 */
	private List<RepositoryIndicator> repositoryIndicatorList;

	public String getDsid()
	{
		return dsid;
	}

	public void setDsid(String dsid)
	{
		this.dsid = dsid;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public String getDbms()
	{
		return dbms;
	}

	public void setDbms(String dbms)
	{
		this.dbms = dbms;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public DbIndicator getDbIndicator()
	{
		return dbIndicator;
	}

	public void setDbIndicator(DbIndicator dbIndicator)
	{
		this.dbIndicator = dbIndicator;
	}

	public List<RepositoryIndicator> getRepositoryIndicatorList()
	{
		return repositoryIndicatorList;
	}

	public void setRepositoryIndicatorList(
			List<RepositoryIndicator> repositoryIndicatorList)
	{
		this.repositoryIndicatorList = repositoryIndicatorList;
	}

}
