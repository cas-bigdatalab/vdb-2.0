package vdb.mydb.query;

import cn.csdb.commons.sql.jdbc.sql.StringSql;

public interface JdbcExpr
{
	StringSql toStringSql() throws Exception;
}
