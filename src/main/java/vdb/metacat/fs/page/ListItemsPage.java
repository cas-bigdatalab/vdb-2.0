package vdb.metacat.fs.page;

import java.util.List;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;

/**
 * 列表页面
 * 
 * @author gzb
 * 
 */
public class ListItemsPage implements Page
{
	private String name;// 页面id(唯一)

	private String title;// 中文名称

	private String style;// 显示样式

	private String entityUri;// 实体uri

	private boolean isDefault;// 是否系统自动生成默认页面

	private String type;// 页面类型
	
	private int pageSize;// 每页显示记录数

	private String grantFilter;// 过滤条件

	private List<OrderField> orderFields;// 排序字段

	private List<ViewField> viewFields;// 显示字段

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getGrantFilter()
	{
		return grantFilter;
	}

	public void setGrantFilter(String grantFilter)
	{
		this.grantFilter = grantFilter;
	}

	public List<OrderField> getOrderFields()
	{
		return orderFields;
	}

	public void setOrderFields(List<OrderField> orderFields)
	{
		this.orderFields = orderFields;
	}

	public List<ViewField> getViewFields()
	{
		return viewFields;
	}

	public void setViewFields(List<ViewField> viewFields)
	{
		this.viewFields = viewFields;
	}

	public String getEntityUri()
	{
		return entityUri;
	}

	public void setEntityUri(String entityUri)
	{
		this.entityUri = entityUri;
	}

	public boolean isDefault()
	{
		return isDefault;
	}

	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	public Entity getEntity()
	{
		return (Entity) VdbManager.getEngine().getCatalog().fromUri(
				this.getEntityUri());
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

}
