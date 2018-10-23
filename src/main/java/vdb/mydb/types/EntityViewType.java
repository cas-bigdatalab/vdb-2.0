package vdb.mydb.types;

import vdb.metacat.Entity;

public class EntityViewType extends ViewType
{

	@Override
	public boolean isSuitableFor(Class cls)
	{
		return Entity.class.isAssignableFrom(cls);
	}

}
