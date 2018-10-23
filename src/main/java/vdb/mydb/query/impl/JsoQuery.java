package vdb.mydb.query.impl;

import java.io.Serializable;
import java.util.Map;

public class JsoQuery
{
	private String _collectionField;

	private String _page;

	private String _entity;

	private String _orderAsc = "desc";

	private String _orderField = null;

	private int _pageIndex = 1;

	private int _pageSize = 20;

	private Serializable _parentBeanId;

	private Map<String, Serializable> _variables = null;

	private JsoExpr _whereFilter;

	public String getCollectionField()
	{
		return _collectionField;
	}

	public String getEntity()
	{
		return _entity;
	}

	public String getOrderAsc()
	{
		return _orderAsc;
	}

	public String getOrderField()
	{
		return _orderField;
	}

	public int getPageIndex()
	{
		return _pageIndex;
	}

	public int getPageSize()
	{
		return _pageSize;
	}

	public Serializable getParentBeanId()
	{
		return _parentBeanId;
	}

	public Map<String, Serializable> getVariables()
	{
		return _variables;
	}

	public JsoExpr getWhereFilter()
	{
		return _whereFilter;
	}

	public void setCollectionField(String collectionField)
	{
		_collectionField = collectionField;
	}

	public void setEntity(String entity)
	{
		_entity = entity;
	}

	public void setOrderAsc(String orderAsc)
	{
		_orderAsc = orderAsc;
	}

	public void setOrderField(String orderField)
	{
		_orderField = orderField;
	}

	public void setPageIndex(int pageIndex)
	{
		_pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize)
	{
		_pageSize = pageSize;
	}

	public void setParentBeanId(Serializable parentBeanId)
	{
		_parentBeanId = parentBeanId;
	}

	public void setVariables(Map<String, Serializable> variables)
	{
		_variables = variables;
	}

	public void setWhereFilter(JsoExpr whereFilter)
	{
		_whereFilter = whereFilter;
	}

	public String getPage()
	{
		return _page;
	}

	public void setPage(String page)
	{
		_page = page;
	}
}