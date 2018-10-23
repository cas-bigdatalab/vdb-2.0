package vdb.mydb.bean;

import java.io.Serializable;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;

public class JdbcRowImpl implements JdbcRowWriter, JdbcRowReader
{
	private AnyBean _bean;

	private Entity _entity;

	private Map<String, Serializable> _map;

	public JdbcRowImpl(Entity entity, AnyBean bean,
			Map<String, Serializable> map)
	{
		_entity = entity;
		_bean = bean;
		_map = map;
	}

	public Serializable getId()
	{
		return get(_entity.getIdentifier().getField().getColumnName());
	}

	public void set(String columnName, Serializable value)
	{
		_map.put(columnName, value);
	}

	public Serializable get(String columnName)
	{
		return _map.get(columnName);
	}

	public AnyBean getBean()
	{
		return _bean;
	}

	public Entity getEntity()
	{
		return _entity;
	}
}
