package vdb.metacat;

public interface DataSet extends CatalogObject
{
	public void addEntity(Entity entity);

	public void addQuery(Query query);

	public void addRelation(Relation relation);

	public void addView(View view);

	public String getDescription();

	public Entity[] getEntities();

	public Query[] getQueries();

	public Relation[] getRelations();

	public Repository getRepository();

	public String getTitle();

	public View[] getViews();

	public void removeEntity(Entity entity);

	public void removeQuery(Query query);

	public void removeRelation(Relation relation);

	public void removeView(View view);

	public void setTitle(String title);

	public void setUri(String uri);

	public void setRepository(Repository repository);
}
