/*
 * �������� 2005-9-14
 */
package cn.csdb.commons.sql.jdbc.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.sql.jdbc.impl.RangedResultSet;
import cn.csdb.commons.util.Parameters;
import cn.csdb.commons.util.SqlUtils;

/*
 * string with parameters
 * 
 * �ܷ�֧�ִ����ֵĲ���
 * 
 * @author bluejoe
 */
public class StringSql implements Sql, QuerySql
{
	private String _bodyString;

	private Parameters _parameters = new Parameters();

	public StringSql()
	{
	}

	public StringSql(StringSql source)
	{
		_bodyString = source._bodyString;
		_parameters.addAll(source.getParameters());
	}

	public StringSql(String string)
	{
		setString(string);
	}

	public StringSql(String string, Serializable... parameters)
	{
		setString(string);

		for (int i = 0; i < parameters.length; i++)
		{
			setParameter(i + 1, parameters[i]);
		}
	}

	public StringSql addParameter(Serializable value)
	{
		_parameters.add(value);
		return this;
	}

	public StringSql addParameters(List<Serializable> parameters)
	{
		_parameters.addAll(parameters);
		return this;
	}

	public StringSql addParameters(StringSql sqlText)
	{
		addParameters(sqlText.getParameters());
		return this;
	}

	public StringSql clearParameters()
	{
		_parameters.clear();
		return this;
	}

	public Object getParameter(int index)
	{
		return _parameters.get(index);
	}

	public List<Serializable> getParameters()
	{
		return _parameters.getAll();
	}

	public String getString()
	{
		return _bodyString;
	}

	public StringSql setParameter(int index, Serializable value)
	{
		_parameters.set(index, value);
		return this;
	}

	public StringSql setString(String string)
	{
		_bodyString = string;
		return this;
	}

	public String toString()
	{
		return _bodyString;
	}

	public void executeUpdate(JdbcSource sqlSource, Connection conn,
			StatementHandler handler) throws SQLException
	{
		PreparedStatement st = null;

		try
		{
			PoolManager.getInstance().getLogger().info("SQL: " + _bodyString);
			SqlUtils.debugParameters(this);

			try
			{
				st = conn.prepareStatement(_bodyString,
						java.sql.Statement.RETURN_GENERATED_KEYS);
			}
			catch (Throwable e)
			{
				st = conn.prepareStatement(_bodyString);
			}

			List ps = getParameters();
			for (int i = 0; i < ps.size(); i++)
			{
				sqlSource.getSqlDialect().updateObject(st, i + 1, ps.get(i));
			}

			st.executeUpdate();
			handler.afterExecute(st);
		}
		finally
		{
			if (st != null)
				st.close();
		}
	}

	public void executeQuery(JdbcSource sqlSource, int beginning, int size,
			Connection conn, ResultSetHandler handler) throws SQLException
	{
		PreparedStatement st = null;
		String sql = _bodyString;
		boolean needsRangedResultSet = false;
		if (isRanged(beginning, size))
		{
			String sfb = sqlSource.getSqlDialect().sqlForBlockQuery(sql,
					beginning, size);
			if (sfb != null)
			{
				sql = sfb;
			}
			else
			{
				needsRangedResultSet = true;
			}
		}

		try
		{
			if (needsRangedResultSet)
			{
				try
				{
					st = conn.prepareStatement(sql,
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
				}
				catch (Throwable e)
				{
					st = conn.prepareStatement(sql);
				}
			}
			else
			{
				st = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
			}

			PoolManager.getInstance().getLogger().info("SQL: " + sql);
			SqlUtils.debugParameters(this);

			List ps = getParameters();
			for (int i = 0; i < ps.size(); i++)
			{
				sqlSource.getSqlDialect().updateObject(st, i + 1, ps.get(i));
			}

			ResultSet rs = st.executeQuery();
			if (needsRangedResultSet)
				rs = new RangedResultSet(rs, beginning, size);

			handler.afterQuery(rs);
			rs.close();
		}
		finally
		{
			if (st != null)
				st.close();
		}
	}

	private boolean isRanged(int beginning, int size)
	{
		if (size != -1)
			return true;

		return beginning != 1;
	}

	public QuerySql getSqlForCount(JdbcSource sqlSource)
	{
		StringSql ns = new StringSql(this);
		ns.setString(sqlSource.getSqlDialect().sqlForCount(this.getString()));

		return ns;
	}

	/**
	 * �������еı����磺"$name"���滻�ɲ���ֵ���磺"bluejoe"��
	 * 
	 * @param variables
	 * @return
	 */
	public StringSql mergeVariables(Map<String, Serializable> variables)
	{
		StringSql sql2 = new StringSql(getString());
		//���滻���е�Parameter
		for (Serializable param : getParameters())
		{
			Serializable param2 = param;
			if (param2 instanceof String)
			{
				String s = (String) param2;
				if (s.startsWith("$"))
				{
					String name = s.substring(1);
					if (variables.containsKey(name))
					{
						param2 = variables.get(name);
					}
				}
			}

			sql2.addParameter(param2);
		}

		return sql2;
	}
}
