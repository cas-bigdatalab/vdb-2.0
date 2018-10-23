package vdb.mydb.query;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.query.impl.CollectionQueryCreator;
import vdb.mydb.query.impl.EntityQueryCreator;
import vdb.mydb.query.impl.JsoQuery;

public class QueryCreatorManager
{
	private static QueryCreatorManager _instance = new QueryCreatorManager();

	public static QueryCreatorManager getInstance()
	{
		return _instance;
	}

	private QueryCreatorManager()
	{
	}

	public QueryCreator getQueryCreator(JsoQuery jsoQuery)
	{
		// 如果jsoQuery.getEntity()得到的并不是Entity的URI，即通过FromUri方法得不到Entity，则此方法会报异常
		if (jsoQuery.getEntity() != null)
		{
			return new EntityQueryCreator((Entity) VdbManager.getEngine()
					.getCatalog().fromUri(jsoQuery.getEntity()));
		}

		return new CollectionQueryCreator((Field) VdbManager.getEngine()
				.getCatalog().fromUri(jsoQuery.getCollectionField()), jsoQuery
				.getParentBeanId());
	}
}
