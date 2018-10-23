package vdb.mydb.typelib.type;

import org.apache.velocity.context.Context;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbExpr;

public class VdbExprType extends AbstractFieldDriver<VdbExpr>
{
	public VdbExpr createData() throws Exception
	{
		VdbExpr vd = new VdbExpr((String) _field.get("expr"));
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbExpr data)
			throws Exception
	{
	}

	public VdbExpr jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbExpr vd = createData();
		Context vc = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		AnyBean bean = jdbcRowReader.getBean();
		vc.put("bean", bean);
		for (Field field : _field.getEntity().getFields())
		{
			vc.put(field.getName(), bean.get(field));
		}

		vd.setContext(vc);
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbExpr data,
			VdbExpr dataOutOfDate) throws Exception
	{
	}
}