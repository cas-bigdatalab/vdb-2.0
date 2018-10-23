package vdb.report.resstat.dbstats.vo;

import java.util.List;

public class DbIndicator
{
	/**
	 * 数据集ID
	 */
	private String dsid;

	/**
	 * 字节数
	 */
	private long bytes;

	/**
	 * 总记录数
	 */
	private long recordNum;

	/**
	 * 实体个数
	 */
	private int entityNum;

	/**
	 * 关联个数
	 */
	private int relationNum;

	/**
	 * 关联度
	 */
	private double relationRate;

	/**
	 * 字段数
	 */
	private int fieldNum;

	/**
	 * 文本字段数
	 */
	private int textFieldNum;

	/**
	 * 填充率(完整度)
	 */
	private double integrityRate;

	/**
	 * 数据实体统计指标列表
	 */
	private List<EntityIndicator> entityIndicatorList;

	/**
	 * 最新统计时间
	 */
	private String lastStatsTime;

	public String getDsid()
	{
		return dsid;
	}

	public void setDsid(String dsid)
	{
		this.dsid = dsid;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public long getRecordNum()
	{
		return recordNum;
	}

	public void setRecordNum(long recordNum)
	{
		this.recordNum = recordNum;
	}

	public int getEntityNum()
	{
		return entityNum;
	}

	public void setEntityNum(int entityNum)
	{
		this.entityNum = entityNum;
	}

	public int getRelationNum()
	{
		return relationNum;
	}

	public void setRelationNum(int relationNum)
	{
		this.relationNum = relationNum;
	}

	public double getRelationRate()
	{
		return relationRate;
	}

	public void setRelationRate(double relationRate)
	{
		this.relationRate = relationRate;
	}

	public int getFieldNum()
	{
		return fieldNum;
	}

	public void setFieldNum(int fieldNum)
	{
		this.fieldNum = fieldNum;
	}

	public int getTextFieldNum()
	{
		return textFieldNum;
	}

	public void setTextFieldNum(int textFieldNum)
	{
		this.textFieldNum = textFieldNum;
	}

	public double getIntegrityRate()
	{
		return integrityRate;
	}

	public void setIntegrityRate(double integrityRate)
	{
		this.integrityRate = integrityRate;
	}

	public List<EntityIndicator> getEntityIndicatorList()
	{
		return entityIndicatorList;
	}

	public void setEntityIndicatorList(List<EntityIndicator> entityIndicatorList)
	{
		this.entityIndicatorList = entityIndicatorList;
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
