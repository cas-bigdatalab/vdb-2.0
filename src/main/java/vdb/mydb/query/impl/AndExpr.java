package vdb.mydb.query.impl;

import vdb.mydb.query.JdbcExpr;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class AndExpr implements JdbcExpr
{
	private JdbcExpr _a;

	private JdbcExpr _b;

	private AnyQueryImpl _query;

	public AndExpr(AnyQueryImpl query, JdbcExpr a, JdbcExpr b)
	{
		_query = query;
		_a = a;
		_b = b;
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
		StringSql sa = _a.toStringSql();
		StringSql sb = _b.toStringSql();
		StringSql stringSql = new StringSql(String.format("(%s) and (%s)", sa
				.getString(), sb.getString()));
		stringSql.addParameters(sa);
		stringSql.addParameters(sb);

		return stringSql;
	}
}
