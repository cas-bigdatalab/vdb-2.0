package vdb.metacat.fs.page;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;

/**
 * 浏览，列表显示页面字段属性
 * 
 * @author gzb
 * 
 */
public class ViewField
{

	public ViewField(String fieldUri)
	{
		this.fieldUri = fieldUri;
	}

	public ViewField()
	{

	}

	private String fieldUri;// 字段uri

	public String getFieldUri()
	{
		return fieldUri;
	}

	public void setFieldUri(String fieldUri)
	{
		this.fieldUri = fieldUri;
	}

	public Field getField()
	{
		return (Field) VdbManager.getEngine().getCatalog().fromUri(
				this.getFieldUri());
	}

}
