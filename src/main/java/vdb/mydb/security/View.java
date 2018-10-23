package vdb.mydb.security;

import java.util.List;

import vdb.metacat.CatalogObject;

public interface View
{

	public String getName();

	public String getTitle();

	public String getEntity();

	public List<String> getItems();

	public List<CatalogObject> getFields();

	public String getFilter();

	public String getOrderField();

}
