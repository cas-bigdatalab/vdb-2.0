/*
 * Created on 2007-3-29
 */
package cn.csdb.commons.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import cn.csdb.commons.sql.catalog.JdbcColumn;
import cn.csdb.commons.sql.catalog.JdbcTable;

/**
 * ΪBean�ṩMap�ӿ�
 * 
 * @author bluejoe
 */
public class JdbcBeanMap implements Map
{
	class SQLBeanMeta
	{
		private BeanEditor _editor;

		private List _fieldNames;

		public SQLBeanMeta(Class beanClass, JdbcTable table) throws Exception
		{
			List fields = table.getColumns();

			_fieldNames = new Vector();

			for (int i = 0; i < fields.size(); i++)
			{
				_fieldNames.add(((JdbcColumn) fields.get(i)).getColumnName()
						.toUpperCase());
			}

			_editor = BeanEditor.getBeanEditor(beanClass);
		}

		public BeanEditor getBeanEditor()
		{
			return _editor;
		}

		public List getFieldNames()
		{
			return _fieldNames;
		}
	}

	private static Map _allMeta = new HashMap();

	private Object _bean;

	private BeanEditor _editor;

	private List _fieldNames;

	private SQLBeanMeta _meta;

	public JdbcBeanMap(Object bean, JdbcTable table) throws Exception
	{
		_bean = bean;
		Class bc = bean.getClass();
		if (_allMeta.containsKey(bc))
		{
			_meta = (SQLBeanMeta) _allMeta.get(bc);
		}
		else
		{
			_meta = new SQLBeanMeta(bc, table);
			_allMeta.put(bc, _meta);
		}

		_fieldNames = _meta.getFieldNames();
		_editor = _meta.getBeanEditor();
	}

	public void clear()
	{
		for (int i = 0; i < _fieldNames.size(); i++)
		{
			this.put(_fieldNames.get(i), null);
		}
	}

	public boolean containsKey(Object arg0)
	{
		return _fieldNames.contains(arg0.toString().toUpperCase());
	}

	public boolean containsValue(Object arg0)
	{
		return false;
	}

	public Set entrySet()
	{
		throw new UnsupportedOperationException();
	}

	public Object get(Object arg0)
	{
		try
		{
			return _editor.getGetter((String) arg0).doGet(_bean);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public boolean isEmpty()
	{
		return false;
	}

	public Set keySet()
	{
		throw new UnsupportedOperationException();
	}

	public Object put(Object arg0, Object arg1)
	{
		if (arg0 == null)
			return null;

		if (!_fieldNames.contains(arg0.toString().toUpperCase()))
			return null;

		Object o = get(arg0);
		try
		{
			_editor.getSetter((String) arg0).doSet(_bean, arg1);
		}
		catch (Exception e)
		{
		}

		return o;
	}

	public void putAll(Map arg0)
	{
		for (int i = 0; i < _fieldNames.size(); i++)
		{
			String key = (String) _fieldNames.get(i);
			this.put(key, arg0.get(key));
		}
	}

	public Object remove(Object arg0)
	{
		return put(arg0, null);
	}

	public int size()
	{
		return _fieldNames.size();
	}

	public Collection values()
	{
		throw new UnsupportedOperationException();
	}
}
