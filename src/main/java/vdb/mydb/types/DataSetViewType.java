package vdb.mydb.types;

import vdb.metacat.DataSet;

public class DataSetViewType extends ViewType
{

	@Override
	public boolean isSuitableFor(Class cls)
	{
		return DataSet.class.isAssignableFrom(cls);
	}

}
