package vdb.report.access.vo;

public class AreaCountRecord
{

	private String area;

	private int count;

	public AreaCountRecord(String area, int count)
	{

		this.area = area;
		this.count = count;

	}

	public String getArea()
	{
		return area;
	}

	public int getCount()
	{
		return count;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

}
