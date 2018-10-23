package vdb.report.access.vo;

public class EntityAccessRecord
{
	int counts;

	private String date = "";

	private String id;

	private String title;

	public EntityAccessRecord()
	{
		super();
	}

	public int getCounts()
	{
		return counts;
	}

	public String getDate()
	{
		return date;
	}

	public String getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setCounts(int counts)
	{
		this.counts = counts;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
