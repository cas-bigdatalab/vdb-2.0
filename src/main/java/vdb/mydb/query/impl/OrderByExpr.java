package vdb.mydb.query.impl;

import java.util.List;

import vdb.metacat.Field;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.query.JdbcExpr;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class OrderByExpr implements JdbcExpr
{
	private boolean _asc;

	private List<Field> _field;

	private AnyQueryImpl _query;

	public OrderByExpr(AnyQueryImpl query, List<Field> field, boolean asc)
	{
		_query = query;
		_field = field;
		_asc = asc;
	}

	public OrderByExpr(AnyQueryImpl query, List<Field> field, String asc)
	{
		this(query, field, "asc".equalsIgnoreCase(asc));
	}

	public List<Field> getField()
	{
		return _field;
	}

	public boolean isAsc()
	{
		return _asc;
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
		VdbDataSet dsex = ((VdbDataSet) _field.get(0).getEntity().getDataSet());
		String crossOrder = "";
		String order = "";
		for (int i = 0; i < _field.size(); i++)
		{
			crossOrder += dsex.quote(_field.get(0).getEntity().getTableName())
					+ "." + dsex.quote(_field.get(i).getColumnName()) + ",";
			order += dsex.quote(_field.get(i).getColumnName()) + ",";
		}
		if (order.length() > 0)
		{
			crossOrder = crossOrder.substring(0, order.length() - 1);
			order = order.substring(0, order.length() - 1);
		}
		if (_query.isCrossQuery())
			return new StringSql(String.format("%s %s", crossOrder,
					_asc ? "asc" : "desc"));

		return new StringSql(String.format("%s %s", order, _asc ? "asc"
				: "desc"));
	}
}
