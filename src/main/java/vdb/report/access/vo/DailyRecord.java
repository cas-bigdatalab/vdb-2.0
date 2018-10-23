package vdb.report.access.vo;

public class DailyRecord
{

	private int count;

	private String date;

	public DailyRecord(String date, int count)
	{
		super();
		this.date = date;
		this.count = count;
	}

	public int getCount()
	{
		return count;
	}

	public String getDate()
	{
		return date;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

}
