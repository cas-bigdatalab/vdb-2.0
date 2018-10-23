package vdb.metacat;

public interface View extends CatalogObject
{

	void addItem(CatalogObject object);

	void removeItem(CatalogObject object);

	CatalogObject[] getItems();

	public <T> T[] getItems(T[] type);

	String getName();

	CatalogObject getSource();

	void removeItems();

	void setName(String name);

	void setSource(CatalogObject source);

}
