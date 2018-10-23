package vdb.mydb.types;

import vdb.metacat.Domain;

public class DomainViewType extends ViewType
{
	@Override
	public boolean isSuitableFor(Class cls)
	{
		return Domain.class.isAssignableFrom(cls);
	}
}
