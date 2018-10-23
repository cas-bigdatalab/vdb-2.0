package vdb.metacat.fs.page;

public class Project
{

	private String name;// 名称

	private String viewType;// 显示类型

	private String condition;// 条件

	private String title;// 显示名称

	private String entityUri;// 实体uri

	private String fieldUri;// 字段uri

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getViewType()
	{
		return viewType;
	}

	public void setViewType(String viewType)
	{
		this.viewType = viewType;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getEntityUri()
	{
		return entityUri;
	}

	public void setEntityUri(String entityUri)
	{
		this.entityUri = entityUri;
	}

	public String getFieldUri()
	{
		return fieldUri;
	}

	public void setFieldUri(String fieldUri)
	{
		this.fieldUri = fieldUri;
	}

}
