package vdb.metacat.fs.page;

import java.util.List;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;

/**
 * 细览页面
 * 
 * @author gzb
 * 
 */
public class ShowItemPage implements Page
{

	private String entityUri;// 实体uri

	private boolean hideCollectionFieldTitle = false;

	private boolean isDefault;// 是否系统自动生成默认页面

	private boolean isDisplay = true;// 字段值为空时是否显示

	private String name;// 页面id(唯一)

	private String style;// 显示样式

	private String title;// 中文名称

	private String type;// 页面类型

	private List<ViewField> viewFields;// 显示字段

	public Entity getEntity()
	{
		return (Entity) VdbManager.getEngine().getCatalog()
				.fromUri(this.getEntityUri());
	}

	public String getEntityUri()
	{
		return entityUri;
	}

	public String getName()
	{
		return name;
	}

	public String getStyle()
	{
		return style;
	}

	public String getTitle()
	{
		return title;
	}

	public String getType()
	{
		return type;
	}

	public List<ViewField> getViewFields()
	{
		return viewFields;
	}

	public boolean isDefault()
	{
		return isDefault;
	}

	public boolean isDisplay()
	{
		return isDisplay;
	}

	public boolean isHideCollectionFieldTitle()
	{
		return hideCollectionFieldTitle;
	}

	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	public void setDisplay(boolean isDisplay)
	{
		this.isDisplay = isDisplay;
	}

	public void setEntityUri(String entityUri)
	{
		this.entityUri = entityUri;
	}

	public void setHideCollectionFieldTitle(boolean hideCollectionFieldTitle)
	{
		this.hideCollectionFieldTitle = hideCollectionFieldTitle;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setViewFields(List<ViewField> viewFields)
	{
		this.viewFields = viewFields;
	}

}
