package vdb.mydb.typelib.idgen;

import java.io.Serializable;

import vdb.metacat.Field;
import vdb.metacat.idgen.IdGeneratorContext;
import cn.csdb.commons.util.StringUtils;

/*
 * @author bluejoe
 */
public class GuidGenerator extends AbstractIdGenerator
{
	public boolean accepts(Field field) throws Exception
	{
		return "String".equalsIgnoreCase(field.getTypeName());
	}

	public Serializable generateId(IdGeneratorContext context) throws Exception
	{
		return StringUtils.getGuid();
	}

}
