package vdb.metacat;

/*
 * a large cache for objects
 * 
 * @author bluejoe
 */
//FIXME: CatalogCache
public interface Catalog
{
	public void cacheIn(CatalogObject domain);

	public void cacheOut(CatalogObject object);

	public void clear();

	public <T extends CatalogObject> T fromId(String id);

	public <T extends CatalogObject> T fromUri(String uri);
}
