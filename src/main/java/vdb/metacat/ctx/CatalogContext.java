package vdb.metacat.ctx;

import vdb.metacat.CatalogObject;
import vdb.metacat.idgen.IdGenerator;
import vdb.metacat.meta.ValuesGetter;
import vdb.mydb.typelib.FieldType;

public interface CatalogContext
{
	<T extends CatalogObject> T create(String typeName) throws Exception;

	ValuesGetter<String, String> getDefaults(String typeName);

	FieldType getFieldType(String name);

	IdGenerator getIdGenerator(String name);
}
