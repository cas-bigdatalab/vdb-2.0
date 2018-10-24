/*
 * 创建日期 2005-12-14
 */
package cn.csdb.commons.orm;

import java.util.List;

import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/*
 * @author bluejoe
 */
public interface Query<T> extends Pageable<T>
{
	public List<T> list() throws Exception;

	public T single() throws Exception;

	public Query<T> reset();

	public Query<T> setSql(QuerySql fullSql);

	public Query<T> setFilter(StringSql filter);

	public Query<T> setOrderBy(String orderBy);

	public Query<T> setTableName(String... tableNames);

	public Query<T> setField(String... fields);
}