package vdb.mydb.query;

import vdb.metacat.Field;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public interface QueryFromSql extends VarQuery
{
	public QueryFromSql setSql(StringSql sql);

	public void orderBy(Field field, String orderAsc);
}