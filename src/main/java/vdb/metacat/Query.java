package vdb.metacat;

//FIXME: QueryTemplate
public interface Query extends CatalogObject
{

	public DataSet getDataSet();

	public Entity getEntity();

	String getName();

	String getOql();

	String getSql();

	String getTitle();

	void setDataSet(DataSet dataSet);

	public void setEntity(Entity entity);

	void setName(String name);

	void setOql(String oql);

	void setSql(String sql);

	void setTitle(String title);
}
