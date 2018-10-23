package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vdb.metacat.RelationKey;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.bean.BeanRef;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbRef;

public class VdbRefType extends AbstractFieldDriver<VdbRef>
{

	public int getOptionsSize()
	{
		try
		{
			AnyBeanDao dao = new AnyBeanDao(_field.getRelationKey().getTarget());
			AnyQuery query = dao.createQuery();
			return dao.execute(query).size();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public List<AnyBean> getOptions()
	{
		try
		{
			AnyBeanDao dao = new AnyBeanDao(_field.getRelationKey().getTarget());
			AnyQuery query = dao.createQuery();
			return dao.execute(query).list();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new ArrayList<AnyBean>();
	}

	public VdbRef createData() throws Exception
	{
		VdbRef vd = new VdbRef();
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbRef data)
			throws Exception
	{
		try
		{
			jdbcRowWriter.set(_field.getColumnName(), data.getRefBean().getId()
					.getValue());
		}
		catch (Exception e)
		{
			jdbcRowWriter.set(_field.getColumnName(), null);
		}
	}

	public VdbRef jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbRef vd = createData();
		RelationKey role = _field.getRelationKey();
		Serializable jo = jdbcRowReader.get(role.getColumnName());
		BeanRef ref = new BeanRef(role.getTarget(), jo);
		vd.setBeanRef(ref);

		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbRef data,
			VdbRef dataOutOfDate) throws Exception
	{
		try
		{
			jdbcRowWriter.set(_field.getColumnName(), data.getRefBean().getId()
					.getValue());
		}
		catch (Exception e)
		{
			jdbcRowWriter.set(_field.getColumnName(), null);
		}
	}
}