package vdb.mydb.typelib.idgen;

import java.io.Serializable;

import vdb.metacat.Field;
import vdb.metacat.idgen.IdGeneratorContext;
import cn.csdb.commons.util.StringUtils;

/*
 * @author bluejoe
 */
public class UidGenerator extends AbstractIdGenerator
{
	public boolean accepts(Field field) throws Exception
	{
		return "Integer".equalsIgnoreCase(field.getTypeName())
				|| "String".equalsIgnoreCase(field.getTypeName());
	}

	public Serializable generateId(IdGeneratorContext context) throws Exception
	{
		Field field = context.getPrimaryKey().getField();

		if ("Long".equalsIgnoreCase(field.getTypeName()))
			return System.currentTimeMillis();

		return StringUtils.getUid();
	}

}
