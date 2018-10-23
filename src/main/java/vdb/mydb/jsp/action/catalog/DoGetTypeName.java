package vdb.mydb.jsp.action.catalog;

public class DoGetTypeName
{
	private int count;

	private String name;

	private String tid;

	private String type;

	public DoGetTypeName()
	{

	}

	public int getCount()
	{
		return ++count;
	}

	public String getName()
	{
		return name;
	}

	public String getTid()
	{
		return tid;
	}

	public String getType()
	{
		return type;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setName(String name)
	{
		this.name = name.toLowerCase();
	}

	public void setTid(String tid)
	{
		this.tid = tid;
	}

	public void setType(String type)
	{
		try
		{
			String t = "";
			if (type.equals("SMALLINT") || type.equals("TINYINT")
					|| type.equals("BIGINT") || type.equals("DECIMAL")
					|| type.equals("INTEGER") || type.equals("NUMERIC"))
			{
				t = "Long";
			}
			else if (type.equals("DOUBLE") || type.equals("FLOAT")
					|| type.equals("REAL"))
			{
				t = "Double";
			}
			else if (type.equals("DATE") || type.equals("TIME")
					|| type.equals("TIMESTAMP"))
			{
				t = "Date";
			}
			else if (type.equals("CLOB") || type.equals("BLOB"))
			{
				t = "Files";
			}
			else
			{
				t = "String";
			}
			this.type = t;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
