package vdb.mydb.query.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.VarQuery;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.StringKeyMap;

public class AnyQueryImpl implements AnyQuery
{
	private Entity _entity;

	private JdbcExpr _filter;

	private OrderByExpr _orderBy;

	private SelectExpr _select;

	private Map<String, Serializable> _variables = new StringKeyMap<Serializable>();

	public AnyQueryImpl(Entity entity)
	{
		_entity = entity;
		_select = new SelectExpr(this, entity);
	}

	public JdbcExpr and(JdbcExpr a, JdbcExpr b)
	{
		if (a == null)
			return b;

		if (b == null)
			return a;

		return new AndExpr(this, a, b);
	}

	public OrderByExpr asc(String uriField) throws Exception
	{
		List<Field> field = new ArrayList<Field>();
		String[] uriFields = uriField.split(",");
		for (int i = 0; i < uriFields.length; i++)
		{
			field.add(getFieldFromName(uriFields[i]));
		}
		return new OrderByExpr(this, field, true);
	}

	public VarQuery bindVariable(String name, Serializable value)
	{
		_variables.put(name, value);
		return this;
	}

	public OrderByExpr desc(String uriField) throws Exception
	{
		List<Field> field = new ArrayList<Field>();
		String[] uriFields = uriField.split(",");
		for (int i = 0; i < uriFields.length; i++)
		{
			field.add(getFieldFromName(uriFields[i]));
		}
		return new OrderByExpr(this, field, false);
	}

	public JdbcExpr eq(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "eq", value);
	}

	public JdbcExpr expr(Field field, String operator, Serializable value)
	{
		return new FieldExpr(this, field, operator, (String) value);
	}

	public JdbcExpr expr(String uriField, String operator, Serializable value)
			throws Exception
	{
		return expr(getFieldFromName(uriField), operator, value);
	}

	public JdbcExpr ge(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "ge", value);
	}

	public Entity getEntity()
	{
		return _entity;
	}

	private Field getFieldFromName(String uriField) throws Exception
	{
		Field field = _entity.getField(uriField);
		if (field == null)
			field = (Field) VdbManager.getEngine().getCatalog().fromUri(
					uriField);

		if (field == null)
			throw new Exception(String.format("failed to find field: %s",
					uriField));
		return field;
	}

	public JdbcExpr gt(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "gt", value);
	}

	public boolean isCrossQuery()
	{
		return _select.isCrossQuery();
	}

	public JdbcExpr isNull(String uriField) throws Exception
	{
		return expr(uriField, "isNull", null);
	}

	public JdbcExpr join(Field collectionField, Serializable pkValue)
	{
		if (collectionField.isStrongCollection())
		{
			return new ManyToOneExpr(_entity, collectionField, pkValue);
		}
		else
		{
			_select.addTable(collectionField.getRelationKey().getRelation()
					.getAssocTableName());
			return new ManyToManyExpr(_entity, collectionField, pkValue);
		}
	}

	public JdbcExpr le(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "le", value);
	}

	public JdbcExpr like(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "like", value);
	}

	public JdbcExpr lt(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "lt", value);
	}

	public JdbcExpr in(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "in", value);
	}

	public JdbcExpr ne(String uriField, Serializable value) throws Exception
	{
		return expr(uriField, "ne", value);
	}

	public JdbcExpr notLike(String uriField, Serializable value)
			throws Exception
	{
		return expr(uriField, "notLike", value);
	}

	public JdbcExpr notNull(String uriField) throws Exception
	{
		return expr(uriField, "notNull", null);
	}

	public JdbcExpr or(JdbcExpr a, JdbcExpr b)
	{
		if (a == null)
			return b;

		if (b == null)
			return a;

		return new OrExpr(this, a, b);
	}

	public OrderByExpr orderBy()
	{
		return _orderBy;
	}

	public VarQuery orderBy(Field field, String asc)
	{
		List<Field> fields = new ArrayList<Field>();
		fields.add(field);
		_orderBy = new OrderByExpr(this, fields, asc);
		return this;
	}

	public VarQuery orderBy(OrderByExpr expr) throws Exception
	{
		_orderBy = expr;
		return this;
	}

	public VarQuery orderBy(String uriField, String asc) throws Exception
	{
		List<Field> field = new ArrayList<Field>();
		String[] uriFields = uriField.split(",");
		for (int i = 0; i < uriFields.length; i++)
		{
			field.add(getFieldFromName(uriFields[i]));
		}
		_orderBy = new OrderByExpr(this, field, asc);
		return this;
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
		StringSql select = _select.toStringSql();
		StringSql sql1 = select;

		if (_filter != null)
		{
			StringSql filter = _filter.toStringSql();
			sql1.setString(sql1.getString() + " where " + filter.getString());
			sql1.addParameters(filter);
		}

		if (_orderBy != null && _orderBy.getField().size() > 0
				&& _orderBy.getField().get(0) != null)
		{
			try
			{
				StringSql orderBy = _orderBy.toStringSql();
				sql1.setString(sql1.getString() + " order by "
						+ orderBy.getString());
				sql1.addParameters(orderBy);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		StringSql sql2 = new StringSql(sql1.getString());
		for (Serializable param : sql1.getParameters())
		{
			Serializable param2 = param;
			if (param2 instanceof String)
			{
				String s = (String) param2;
				if (s.startsWith("$"))
				{
					String name = s.substring(1);
					if (_variables.containsKey(name))
					{
						param2 = _variables.get(name);
					}
				}
			}

			sql2.addParameter(param2);
		}

		return sql2;
	}

	public JdbcExpr where()
	{
		return _filter;
	}

	public VarQuery where(JdbcExpr filter) throws Exception
	{
		_filter = filter;
		return this;
	}

	public VarQuery orderBy(List<Field> field, String asc)
	{
		_orderBy = new OrderByExpr(this, field, asc);
		return this;
	}
}
