package vdb.mydb.bean;

import java.io.Serializable;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.VdbFieldDriver;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.util.StringKeyMap;

public class AnyBeanImpl implements AnyBean
{
	private Entity _entity;

	private Map<String, Field> _fields = new StringKeyMap<Field>();

	private Map<String, VdbData> _map = new StringKeyMap<VdbData>();

	protected AnyBeanImpl()
	{
		super();
	}

	public AnyBeanImpl(Entity entity) throws Exception
	{
		_entity = entity;
		((VdbEntity) _entity).getFields(_fields);
	}

	public VdbData get(Field field) throws Exception
	{
		return get(field.getName());
	}

	public VdbData get(String fieldName) throws Exception
	{
		if (_entity.getField(fieldName) == null)
			return null;

		if (!_map.containsKey(fieldName))
		{
			Field attr = _entity.getField(fieldName);
			VdbData data = attr.getTypeDriver().createData();
			_map.put(fieldName, data);
		}

		return _map.get(fieldName);
	}

	public Entity getEntity()
	{
		return _entity;
	}

	public Map<String, Field> getFields()
	{
		return _fields;
	}

	public VdbData getId() throws Exception
	{
		return get(_entity.getIdentifier().getField());
	}

	public VdbData getTitle() throws Exception
	{
		return get(_entity.getTitleField());
	}

	public void set(Field field, VdbData value)
	{
		_map.put(field.getName(), value);
	}

	public void setId(Serializable id) throws Exception
	{
		Field pk = _entity.getIdentifier().getField();
		VdbFieldDriver<VdbData> driver = pk.getTypeDriver();
		VdbData data = driver.createData();
		data.setAsSdef(new SimpleSdef("" + id));
		set(pk, data);
	}

	public ItemID getUri() throws Exception
	{
		return new VdbItemID(_entity.getUri(), getId().getValue());
	}

	@Deprecated
	public ItemID getItemId() throws Exception
	{
		return getUri();
	}
}
