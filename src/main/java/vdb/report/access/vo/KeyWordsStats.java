package vdb.report.access.vo;

import java.util.Date;

/**
 * 关键词统计指标
 * 
 * @author 苏贤明
 * 
 */
public class KeyWordsStats
{
	/** 统计截止日期 */
	private Date _byDate;

	/** 数据集URI */
	private String _dataset;

	/** 数据实体ID */
	private String _entity;

	/** 关键词 */
	private String _keyword;

	/** 关键词次数 */
	private int _counts;

	public Date getByDate()
	{
		return _byDate;
	}

	public String getDataset()
	{
		return _dataset;
	}

	public String getEntity()
	{
		return _entity;
	}

	public String getKeyword()
	{
		return _keyword;
	}

	public int getCounts()
	{
		return _counts;
	}

	public void setByDate(Date date)
	{
		_byDate = date;
	}

	public void setDataset(String datasetId)
	{
		_dataset = datasetId;
	}

	public void setEntity(String entityId)
	{
		_entity = entityId;
	}

	public void setKeyword(String keyword)
	{
		_keyword = keyword;
	}

	public void setCounts(int counts)
	{
		_counts = counts;
	}
}
