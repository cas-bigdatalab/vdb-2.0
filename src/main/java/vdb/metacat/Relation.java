package vdb.metacat;

//FIXME: RelationDef
public interface Relation extends CatalogObject
{
	public String getAssocTableName();

	public Cardinality getCardinality();

	public DataSet getDataSet();

	public RelationKey getKeyA();

	public RelationKey getKeyB();

	public void setKeyA(RelationKey keyA);

	public void setKeyB(RelationKey keyB);

	String getName();

	public String getTitle();

	boolean isManyToMany();

	boolean isManyToOne();

	boolean isOneToMany();

	void setAssocTableName(String assocTableName);

	void setCardinality(Cardinality card);

	void setDataSet(DataSet dataSet);
}
