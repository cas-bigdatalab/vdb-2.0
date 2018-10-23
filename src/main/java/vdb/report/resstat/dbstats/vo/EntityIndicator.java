package vdb.report.resstat.dbstats.vo;

import java.util.List;

/**
 * 实体（与数据表对应）统计指标类
 * 
 * @author 苏贤明
 */
public class EntityIndicator
{
	/**
	 * 实体指标所属的数据库统计指标类
	 */
	private DbIndicator dbIndicator;

	/**
	 * 实体ID
	 */
	private String id;

	/**
	 * 实体URI
	 */
	private String uri;

	/**
	 * 实体记录数
	 */
	private long recordNum;

	/**
	 * 实体字节数
	 */
	private long bytes;

	/**
	 * 实体标题
	 */
	private String title;

	/**
	 * 对应的表名
	 */
	private String tableName;

	/**
	 * 实体填充率
	 */
	private double integrityRate;

	/**
	 * 最后统计时间
	 */
	private String lastStatsTime;

	/**
	 * 实体的所有字段统计指标列表
	 */
	private List<FieldIndicator> fieldIndicatorList;

	public DbIndicator getDbIndicator()
	{
		return dbIndicator;
	}

	public void setDbIndicator(DbIndicator dbIndicator)
	{
		this.dbIndicator = dbIndicator;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public long getRecordNum()
	{
		return recordNum;
	}

	public void setRecordNum(long recordNum)
	{
		this.recordNum = recordNum;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public double getIntegrityRate()
	{
		return integrityRate;
	}

	public void setIntegrityRate(double integrityRate)
	{
		this.integrityRate = integrityRate;
	}

	public String getLastStatsTime()
	{
		return lastStatsTime;
	}

	public void setLastStatsTime(String lastStatsTime)
	{
		this.lastStatsTime = lastStatsTime;
	}

	public List<FieldIndicator> getFieldIndicatorList()
	{
		return fieldIndicatorList;
	}

	public void setFieldIndicatorList(List<FieldIndicator> fieldIndicatorList)
	{
		this.fieldIndicatorList = fieldIndicatorList;
	}
}
