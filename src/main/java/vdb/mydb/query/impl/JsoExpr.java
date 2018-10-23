package vdb.mydb.query.impl;

import java.io.Serializable;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;

public class JsoExpr
{
	private JsoExpr _a;

	private JsoExpr _b;

	private String _field;

	private String _operator;

	private Serializable _value;

	public JsoExpr getA()
	{
		return _a;
	}

	public JsoExpr getB()
	{
		return _b;
	}

	public String getField()
	{
		return _field;
	}

	public String getOperator()
	{
		return _operator;
	}

	public Serializable getValue()
	{
		return _value;
	}

	public void setA(JsoExpr a)
	{
		_a = a;
	}

	public void setB(JsoExpr b)
	{
		_b = b;
	}

	public void setField(String field)
	{
		_field = field;
	}

	public void setOperator(String operator)
	{
		_operator = operator;
	}

	public void setValue(Serializable value)
	{
		_value = value;
	}

	public JdbcExpr toJdbcExpr(AnyQuery query) throws Exception
	{
		if (_a == null && _b == null)
		{
			// 必须输入字段URI，否则这里的field为null
			Field field = (Field) VdbManager.getEngine().getCatalog().fromUri(
					_field);

			return query.expr(field, _operator, _value);
		}

		// 这里类似于递归调用
		if ("and".equalsIgnoreCase(_operator))
			return query.and(_a.toJdbcExpr(query), _b.toJdbcExpr(query));

		if ("or".equalsIgnoreCase(_operator))
			return query.or(_a.toJdbcExpr(query), _b.toJdbcExpr(query));

		return null;
	}

	@Override
	public String toString()
	{
		if (_a == null && _b == null)
		{
			return "(" + _field + " " + _operator + " " + _value + ")";
		}

		if ("and".equalsIgnoreCase(_operator))
			return "(" + _a.toString() + " and " + _b.toString() + ")";

		if ("or".equalsIgnoreCase(_operator))
			return "(" + _a.toString() + " or " + _b.toString() + ")";

		return null;
	}
}
