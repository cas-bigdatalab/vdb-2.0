package vdb.mydb.metacat;

import vdb.metacat.Cardinality;
import vdb.metacat.DataSet;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.fs.CatalogObjectImpl;

public class VdbRelation extends CatalogObjectImpl implements Relation
{
	protected Cardinality _cardinality;

	protected DataSet _dataSet;

	protected RelationKey _keyA;

	protected RelationKey _keyB;

	/**
	 * @deprecated
	 * @return
	 */
	public String getAssociationTableName()
	{
		return getAssocTableName();
	}

	public String getAssocTableName()
	{
		return get("assocTableName");
	}

	public Cardinality getCardinality()
	{
		return _cardinality;
	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbRelation getEx()
	{
		return this;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public RelationKey getForeignKeyA()
	{
		return _keyA;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public RelationKey getForeignKeyB()
	{
		return _keyB;
	}

	public RelationKey getKeyA()
	{
		return _keyA;
	}

	public RelationKey getKeyB()
	{
		return _keyB;
	}

	public String getTitle()
	{
		return _keyA.getTarget().getTitle() + "-"
				+ _keyB.getTarget().getTitle();
	}

	public String getUri()
	{
		return _dataSet.getUri() + "." + getId();
	}

	public boolean isManyToMany()
	{
		return getCardinality() == Cardinality.ManyToMany;
	}

	public boolean isManyToOne()
	{
		return getCardinality() == Cardinality.ManyToOne;
	}

	public boolean isOneToMany()
	{
		return getCardinality() == Cardinality.OneToMany;
	}

	public void setAssocTableName(String assocTableName)
	{
		set("assocTableName", assocTableName);
	}

	public void setCardinality(Cardinality cardinality)
	{
		_cardinality = cardinality;
	}

	public void setDataSet(DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void setKeyA(RelationKey keyA)
	{
		_keyA = keyA;
	}

	public void setKeyB(RelationKey keyB)
	{
		_keyB = keyB;
	}
}
