package vdb.metacat.fs.page;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;

/**
 * 新增,编辑,修改页面字段属性
 * 
 * @author gzb
 * 
 */
public class EditField
{

	public EditField()
	{

	}

	public EditField(String fieldUri, String defaultValue, boolean isReadonly)
	{
		super();
		this.fieldUri = fieldUri;
		this.defaultValue = defaultValue;
		this.isReadonly = isReadonly;
	}

	private String fieldUri;// 字段uri

	private String defaultValue;// 字段默认值

	private boolean isReadonly;// 是否只读

	public String getFieldUri()
	{
		return fieldUri;
	}

	public void setFieldUri(String fieldUri)
	{
		this.fieldUri = fieldUri;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public boolean isReadonly()
	{
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly)
	{
		this.isReadonly = isReadonly;
	}

	public Field getField()
	{
		return (Field) VdbManager.getEngine().getCatalog().fromUri(
				this.getFieldUri());
	}

}
