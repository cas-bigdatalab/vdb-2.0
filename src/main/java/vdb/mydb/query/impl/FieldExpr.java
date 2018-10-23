package vdb.mydb.query.impl;

import vdb.metacat.Field;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class FieldExpr implements JdbcExpr
{
	private Field _field;

	private String _operatorName;

	private AnyQueryImpl _query;

	private String _sdefXml;

	public FieldExpr(AnyQueryImpl query, Field field, String operatorName,
			String sdefXml)
	{
		_query = query;
		_field = field;
		_sdefXml = sdefXml;
		_operatorName = operatorName;
	}

	@Override
	public String toString()
	{
		try
		{
			return toStringSql().getString();
		}
		catch (Exception e)
		{
			return super.toString();
		}
	}

	public StringSql toStringSql() throws Exception
	{
		SimpleSdef sdef = new SimpleSdef();
		try
		{
			sdef.setXml(_sdefXml);
		}
		catch (Exception e)
		{
			sdef.setValue(_sdefXml);
		}

		return _field.getTypeDriver().buildFilter(_operatorName, sdef,
				_query.isCrossQuery());
	}
}
