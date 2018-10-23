package vdb.metacat;

//FIXME: Pointer
public interface RelationKey extends CatalogObject
{
	public Cardinality getCardinality();

	public String getColumnName();

	public RelationKey getPeer();

	public Relation getRelation();

	public Entity getTarget();

	public boolean isManyToMany();

	public boolean isManyToOne();

	public boolean isOneToMany();

	public void setCardinality(Cardinality cardinality);

	public void setColumnName(String columnName);

	public void setPeer(RelationKey peer);

	public void setRelation(Relation relation);

	public void setTarget(Entity target);

}
