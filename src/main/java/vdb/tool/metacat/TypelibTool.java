package vdb.tool.metacat;

import java.io.File;

import vdb.mydb.VdbManager;
import vdb.mydb.typelib.FieldType;

public class TypelibTool
{
	public boolean existsFile(FieldType type, String fileName)
	{
		return new File(getTypeDir(type), fileName).exists();
	}

	public File getTypeDir(FieldType type)
	{
		return new File(VdbManager.getEngine().getTypeManager().getTypesDir(),
				type.getName());
	}
}
