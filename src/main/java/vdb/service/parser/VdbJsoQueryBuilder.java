package vdb.service.parser;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class VdbJsoQueryBuilder
{
	private String orderAsc = "desc";

	private String orderField = null;

	private int pageIndex = 1;

	private int pageSize = 20;

	private Map<String, Serializable> variables = new LinkedHashMap<String, Serializable>();

	private VdbQueryNode whereFilter = null;

	private String entity = null;

	private String collectionField = null;

	private Serializable parentBeanId = null;

	public VdbJsoQueryBuilder()
	{
		super();
	}

	public VdbJsoQueryBuilder(String orderAsc, String orderField,
			int pageIndex, int pageSize, Map<String, Serializable> variables,
			VdbQueryNode whereFilter, String entity, String collectionField,
			Serializable parentBeanId)
	{
		super();
		this.orderAsc = orderAsc;
		this.orderField = orderField;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.variables = variables;
		this.whereFilter = whereFilter;
		this.entity = entity;
		this.collectionField = collectionField;
		this.parentBeanId = parentBeanId;
	}

	public String getCollectionField()
	{
		return collectionField;
	}

	public void setCollectionField(String collectionField)
	{
		this.collectionField = collectionField;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

	public String getOrderAsc()
	{
		return orderAsc;
	}

	public void setOrderAsc(String orderAsc)
	{
		this.orderAsc = orderAsc;
	}

	public String getOrderField()
	{
		return orderField;
	}

	public void setOrderField(String orderField)
	{
		this.orderField = orderField;
	}

	public int getPageIndex()
	{
		return pageIndex;
	}

	public void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public Serializable getParentBeanId()
	{
		return parentBeanId;
	}

	public void setParentBeanId(Serializable parentBeanId)
	{
		this.parentBeanId = parentBeanId;
	}

	public Map<String, Serializable> getVariables()
	{
		return variables;
	}

	public void setVariables(Map<String, Serializable> variables)
	{
		this.variables = variables;
	}

	public VdbQueryNode getWhereFilter()
	{
		return whereFilter;
	}

	public void setWhereFilter(VdbQueryNode whereFilter)
	{
		this.whereFilter = whereFilter;
	}

	public String toString()
	{
		return JSONObject.fromObject(this).toString()
				.replaceAll("\"\"", "null");
	}
}
