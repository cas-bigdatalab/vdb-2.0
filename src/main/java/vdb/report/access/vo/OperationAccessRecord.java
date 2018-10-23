package vdb.report.access.vo;

public class OperationAccessRecord
{
	private int count;

	private String operation = "";

	public OperationAccessRecord()
	{
		super();
	}

	public OperationAccessRecord(String operation, int count)
	{
		super();
		this.operation = operation;
		this.count = count;
	}

	public int getCount()
	{
		return count;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setOperation(String operation)
	{
		if (operation.contains("showEntity"))
		{
			this.operation = "查看表单";
		}
		if (operation.contains("showBean"))
		{
			this.operation = "查看单条记录";
		}
		if (operation.contains("queryRequest"))
		{
			this.operation = "查询请求";
		}
		if (operation.contains("insert"))
		{
			this.operation = "增";
		}
		if (operation.contains("delete"))
		{
			this.operation = "删";
		}
		if (operation.contains("update"))
		{
			this.operation = "改";
		}
		if (operation.contains("queryCache"))
		{
			this.operation = "缓存查询";
		}
		if (operation.contains("queryJdbc"))
		{
			this.operation = "物理查询";
		}
	}

}
