package vdb.metacat.util;

import vdb.metacat.Cardinality;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Identifier;
import vdb.metacat.Query;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.Repository;
import vdb.metacat.View;
import vdb.metacat.ctx.CatalogContext;

/**
 * tool for catalog building
 * 
 * @author bluejoe
 * 
 */
public class CatalogUtils
{
	public static void createKeys(CatalogContext context, Relation relation)
			throws Exception
	{
		RelationKey keyA = context.create("relationKey");
		RelationKey keyB = context.create("relationKey");
		keyA.setRelation(relation);
		keyB.setRelation(relation);

		keyA.setPeer(keyB);
		keyB.setPeer(keyA);
		relation.setKeyA(keyA);
		relation.setKeyB(keyB);
	}

	public static void createRepository(CatalogContext context, DataSet dataSet)
			throws Exception
	{
		Repository repository = context.create("repository");
		repository.setDataSet(dataSet);
		dataSet.setRepository(repository);
	}

	public static void createIdentifier(CatalogContext context, Entity entity)
			throws Exception
	{
		Identifier identifier = context.create("identifier");
		identifier.setEntity(entity);
		entity.setIdentifier(identifier);
	}

	public static Relation addManyToMany(CatalogContext context,
			DataSet dataSet, String assocTableName, Entity targetA,
			String foreignKeyNameToTargetA, Entity targetB,
			String foreignKeyNameToTargetB) throws Exception
	{
		Relation relation = context.create("relation");
		relation.setDataSet(dataSet);
		dataSet.addRelation(relation);

		relation.setCardinality(Cardinality.ManyToMany);
		relation.setAssocTableName(assocTableName);

		CatalogUtils.createKeys(context, relation);
		RelationKey keyA = relation.getKeyA();
		RelationKey keyB = relation.getKeyB();

		keyA.setTarget(targetA);
		keyA.setColumnName(foreignKeyNameToTargetA);
		keyA.setCardinality(Cardinality.ManyToMany);

		keyB.setTarget(targetB);
		keyB.setColumnName(foreignKeyNameToTargetB);
		keyB.setCardinality(Cardinality.ManyToMany);

		keyA.getTarget().addCollectionKey(keyB);
		keyB.getTarget().addCollectionKey(keyA);

		return relation;
	}

	public static Relation addOneToMany(CatalogContext context,
			DataSet dataSet, Entity slaveEntity, Entity masterEntity,
			String foreignKeyNameInSlaveTable) throws Exception
	{
		Relation relation = context.create("relation");
		relation.setDataSet(dataSet);
		dataSet.addRelation(relation);

		relation.setCardinality(Cardinality.OneToMany);
		CatalogUtils.createKeys(context, relation);

		RelationKey key1, key2;
		key1 = relation.getKeyA();
		key1.setTarget(slaveEntity);
		key1.setCardinality(Cardinality.OneToMany);

		key2 = relation.getKeyB();
		key2.setTarget(masterEntity);
		key2.setColumnName(foreignKeyNameInSlaveTable);
		key2.setCardinality(Cardinality.ManyToOne);

		key1.getTarget().addReferenceKey(key2);
		key2.getTarget().addCollectionKey(key1);

		return relation;
	}

	public static Query addQuery(CatalogContext context, DataSet dataSet,
			String name) throws Exception
	{
		Query query = context.create("query");
		query.setName(name);
		query.setDataSet(dataSet);
		dataSet.addQuery(query);

		return query;
	}

	public static View addView(CatalogContext context, DataSet dataSet,
			String viewTypeName) throws Exception
	{
		View view = context.create("view");
		view.setName(viewTypeName);
		view.setSource(dataSet);
		dataSet.addView(view);

		return view;
	}

}
