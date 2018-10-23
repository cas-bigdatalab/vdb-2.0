package vdb.metacat.fs.page;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;

/**
 * 排序字段属性
 * 
 * @author gzb
 * 
 */
public class OrderField
{

	private String fieldUri;// 字段uri

	private String order;// 排序方式

	public String getFieldUri()
	{
		return fieldUri;
	}

	public void setFieldUri(String fieldUri)
	{
		this.fieldUri = fieldUri;
	}

	public String getOrder()
	{
		return order;
	}

	public void setOrder(String order)
	{
		this.order = order;
	}

	public Field getField()
	{
		return (Field) VdbManager.getEngine().getCatalog().fromUri(
				this.getFieldUri());
	}

}
