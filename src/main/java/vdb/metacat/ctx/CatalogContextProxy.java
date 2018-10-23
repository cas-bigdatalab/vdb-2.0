package vdb.metacat.ctx;

import vdb.metacat.CatalogObject;
import vdb.metacat.idgen.IdGenerator;
import vdb.metacat.meta.BasicValuesGetter;
import vdb.metacat.meta.ValuesGetter;
import vdb.mydb.typelib.FieldType;

public class CatalogContextProxy implements CatalogContext
{
	private CatalogContext _context;

	public CatalogContextProxy(CatalogContext context)
	{
		super();
		_context = context;
	}

	public <T extends CatalogObject> T create(String typeName) throws Exception
	{
		return (T) proxy(_context.create(typeName), typeName);
	}

	public ValuesGetter<String, String> getDefaults(String typeName)
	{
		return _context.getDefaults(typeName);
	}

	public FieldType getFieldType(String name)
	{
		return _context.getFieldType(name);
	}

	public IdGenerator getIdGenerator(String name)
	{
		return _context.getIdGenerator(name);
	}

	private <T extends CatalogObject> T proxy(T o, String typeName)
	{
		ValuesGetter<String, String> defaultValues = _context
				.getDefaults(typeName);
		if (defaultValues == null)
		{
			defaultValues = new BasicValuesGetter<String, String>();
		}

		o.meta().setDefaults(defaultValues);
		return o;
	}
}
