package vdb.mydb.typelib.idgen;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.idgen.IdGeneratorContext;
import vdb.mydb.jdbc.JdbcSourceManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.SelectSql;

/*
 * @author bluejoe
 */
public class AutoIncrement extends AbstractIdGenerator
{
	public boolean accepts(Field field) throws Exception
	{
		return "Long".equalsIgnoreCase(field.getTypeName());
	}

	public Serializable generateId(IdGeneratorContext context) throws Exception
	{
		Field field = context.getPrimaryKey().getField();
		Entity table = field.getEntity();
		JdbcSource ss = JdbcSourceManager.getInstance().getJdbcSource(
				table.getDataSet());
		try
		{
			Long max = ss.queryForObject(new SelectSql().setTableName(
					table.getTableName()).setField(field.getColumnName())
					.setOrderBy(field.getColumnName() + " desc"),
					new ResultSetReader<Long>()
					{

						public Long read(ResultSet rs, int row)
								throws SQLException
						{
							return rs.getLong(1);
						}
					});

			return (max + 1);
		}
		catch (Exception e)
		{
			return 1;
		}
	}

}
