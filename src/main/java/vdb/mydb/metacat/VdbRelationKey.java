package vdb.mydb.metacat;

import vdb.metacat.Cardinality;
import vdb.metacat.Entity;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.fs.CatalogObjectImpl;

public class VdbRelationKey extends CatalogObjectImpl implements RelationKey
{
	private Cardinality _cardinality;

	private RelationKey _peer;

	private Relation _relation;

	private Entity _target;

	public Cardinality getCardinality()
	{
		return _cardinality;
	}

	public String getColumnName()
	{
		return get("columnName");
	}

	public RelationKey getPeer()
	{
		return _peer;
	}

	public Relation getRelation()
	{
		return _relation;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public Relation getRelationDefinition()
	{
		return getRelation();
	}

	public Entity getTarget()
	{
		return _target;
	}

	public String getUri()
	{
		return _relation.getUri() + "." + getId();
	}

	public boolean isManyToMany()
	{
		return _cardinality.isManyToMany();
	}

	public boolean isManyToOne()
	{
		return _cardinality.isManyToOne();
	}

	public boolean isOneToMany()
	{
		return _cardinality.isOneToMany();
	}

	public void setCardinality(Cardinality cardinality)
	{
		_cardinality = cardinality;
	}

	public void setColumnName(String columnName)
	{
		set("columnName", columnName);
	}

	public void setPeer(RelationKey peer)
	{
		_peer = peer;
	}

	public void setRelation(Relation relation)
	{
		_relation = relation;
	}

	public void setTarget(Entity target)
	{
		_target = target;
	}
}
