package vdb.report.resstat.dbstats.vo;


/**
 * 字段统计指标类
 * 
 * @author 苏贤明
 * 
 */
public class FieldIndicator
{
	/**
	 * 字段指标所属的实体统计指标
	 */
	private EntityIndicator entityIndicator;

	/**
	 * 字段ID
	 */
	private String id;

	/**
	 * 字段名字
	 */
	private String name;

	/**
	 * 字段标题
	 */
	private String title;

	/**
	 * 字段利用率
	 */
	private double integrityRate;

	/**
	 * 最新统计时间
	 */
	private String lastStatsTime;

	public EntityIndicator getEntityIndicator()
	{
		return entityIndicator;
	}

	public void setEntityIndicator(EntityIndicator entityIndicator)
	{
		this.entityIndicator = entityIndicator;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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

}
