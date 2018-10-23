package vdb.metacat;

import vdb.metacat.meta.MetaData;

public interface CatalogObject
{
	public String get(String name);

	public String getId();

	public String getUri();

	public MetaData meta();

	void set(String name, String value);
}