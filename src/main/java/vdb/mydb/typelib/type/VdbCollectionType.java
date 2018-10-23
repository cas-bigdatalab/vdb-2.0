package vdb.mydb.typelib.type;

import java.io.Serializable;

import vdb.metacat.Entity;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.impl.CollectionQueryCreator;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbCollection;

public class VdbCollectionType extends AbstractFieldDriver<VdbCollection>
{
	private AnyQuery createQuery(Serializable primaryBeanId) throws Exception
	{
		return new CollectionQueryCreator(_field, primaryBeanId).createQuery();
	}

	public VdbCollection createData() throws Exception
	{
		VdbCollection vd = new VdbCollection();
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbCollection data)
			throws Exception
	{
	}

	public VdbCollection jdbcSelect(JdbcRowReader jdbcRowReader)
			throws Exception
	{
		VdbCollection vd = createData();
		AnyQuery query = createQuery(jdbcRowReader.getId());
		Entity target = _field.getRelationKey().getTarget();
		QueryExecutor qe = new AnyBeanDao(target).execute(query);
		vd.setBeans(qe);

		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbCollection data,
			VdbCollection dataOutOfDate) throws Exception
	{
	}

	public void jdbcDelete(JdbcRowWriter jdbcRowWriter, VdbCollection data)
			throws Exception
	{
		if (_field.isStrongCollection())
		{
			// ͬʱɾ���ӱ�
			Entity target = _field.getRelationKey().getTarget();
			if (data.list() != null)
			{
				for (AnyBean bean : data.list())
				{
					new AnyBeanDao(target).delete(bean);
				}
			}
		}
		else
		{
			// ��j��
			((VdbDataSet) _field.getEntity().getDataSet()).unlinkRecords(_field
					.getRelationKey(), ""
					+ jdbcRowWriter.getBean().getId().getValue());
		}
	}

}