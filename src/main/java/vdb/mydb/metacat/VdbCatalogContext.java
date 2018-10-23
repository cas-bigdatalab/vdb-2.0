package vdb.mydb.metacat;

import java.util.Map;

import vdb.metacat.CatalogObject;
import vdb.metacat.ctx.CatalogContext;
import vdb.metacat.fs.RepositoryImpl;
import vdb.metacat.fs.UnknownCatalogObjectType;
import vdb.metacat.idgen.IdGenerator;
import vdb.metacat.meta.ValuesGetter;
import vdb.mydb.engine.VdbEngine;
import vdb.mydb.typelib.FieldType;
import cn.csdb.commons.util.StringKeyMap;

public class VdbCatalogContext implements CatalogContext
{
	private Map<String, Class<?>> _classMap = new StringKeyMap<Class<?>>();

	private Defaults _defaults;

	private VdbEngine _manager;

	public VdbCatalogContext(VdbEngine manager)
	{
		_manager = manager;
		_classMap.put("dataset", VdbDataSet.class);
		_classMap.put("entity", VdbEntity.class);
		_classMap.put("domain", VdbDomain.class);
		_classMap.put("field", VdbField.class);
		_classMap.put("query", VdbQuery.class);
		_classMap.put("relation", VdbRelation.class);
		_classMap.put("relationKey", VdbRelationKey.class);
		_classMap.put("repository", RepositoryImpl.class);
		_classMap.put("identifier", VdbIdentifier.class);
		_classMap.put("view", VdbView.class);
	}

	public <T extends CatalogObject> T create(String typeName) throws Exception
	{
		if (!_classMap.containsKey(typeName))
			throw new UnknownCatalogObjectType(typeName);

		return proxy((T) _classMap.get(typeName).newInstance(), typeName);
	}

	public ValuesGetter<String, String> getDefaults(String className)
	{
		return _defaults.getDefaultValues(className);
	}

	public FieldType getFieldType(String name)
	{
		return _manager.getTypeManager().getFieldType(name);
	}

	public IdGenerator getIdGenerator(String name)
	{
		return _manager.getIdGenerators().getGenerator(name);
	}

	public <T extends CatalogObject> T proxy(T o, String typeName)
	{
		o.meta().setDefaults(getDefaults(typeName));
		return o;
	}

	public void setDefaults(Defaults defaults)
	{
		_defaults = defaults;
	}
}
