package cn.csdb.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.csdb.commons.beans.BeanEditor;
import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.catalog.JdbcColumn;
import cn.csdb.commons.sql.catalog.JdbcTable;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class BeanMappingAdapter implements OrMapping
{
	private BeanMapping _mapping;

	private JdbcSource _jdbcSource;

	private JdbcTable _table;

	private BeanEditor _editor;

	public BeanMappingAdapter(BeanMapping mapping) throws Exception
	{
		super();
		_mapping = mapping;
		_jdbcSource = JdbcManager.getInstance().getJdbcSource(
				_mapping.getDataSource());
		_table = _jdbcSource.getJdbcCatalog().getDatabase().getTable(
				_mapping.getTableName());
		_editor = BeanEditor.getBeanEditor(_mapping.getBeanClass());
	}

	public DataSource getDataSource() throws Exception
	{
		return _mapping.getDataSource();
	}

	public String getTableName()
	{
		return _mapping.getTableName();
	}

	public void bean2Map(Object bean, Map<String, Serializable> map,
			boolean onInsert) throws Exception
	{
		List<JdbcColumn> fields = _table.getColumns();
		List names = new ArrayList<String>();

		for (JdbcColumn field : fields)
		{
			names.add(field.getColumnName());
		}

		_editor.doBatchGet(bean, map, names);
	}

	public Object map2Bean(Map<String, Serializable> map) throws Exception
	{
		Object bean = _mapping.createBean();
		_editor.doBatchSet(bean, map);

		return bean;
	}

	public StringSql getIdFilter(Serializable id)
	{
		String pk = _mapping.getPrimaryKey();
		return new StringSql(_jdbcSource.getQuotedIdentifier(pk) + "=?", id);
	}

	public Serializable getPrimaryKeyValue(Object bean) throws Exception
	{
		return (Serializable) _editor.doGet(bean, _mapping.getPrimaryKey());
	}

	public void setGeneratedKey(Object bean, String key, Serializable value)
			throws Exception
	{
		_editor.doSet(bean, key, value);
	}
}
