package vdb.mydb.query;

import java.io.Serializable;
import java.util.List;

import vdb.metacat.Field;
import vdb.mydb.query.impl.OrderByExpr;

//FIXME: should be splitted into two classes: 'JdbcExprBuilder' and 'AnyQueryExpr'

public interface AnyQuery extends VarQuery
{
	public JdbcExpr and(JdbcExpr a, JdbcExpr b);

	public OrderByExpr asc(String uriField) throws Exception;

	public OrderByExpr desc(String uriField) throws Exception;

	public JdbcExpr eq(String uriField, Serializable value) throws Exception;

	public JdbcExpr expr(Field field, String operator, Serializable value)
			throws Exception;

	public JdbcExpr expr(String uriField, String operator, Serializable value)
			throws Exception;

	public JdbcExpr ge(String uriField, Serializable value) throws Exception;

	public JdbcExpr gt(String uriField, Serializable value) throws Exception;

	public JdbcExpr isNull(String uriField) throws Exception;

	public JdbcExpr join(Field collectionField, Serializable pkValue);

	public JdbcExpr le(String uriField, Serializable value) throws Exception;

	public JdbcExpr like(String uriField, Serializable value) throws Exception;

	public JdbcExpr lt(String uriField, Serializable value) throws Exception;

	public JdbcExpr ne(String uriField, Serializable value) throws Exception;

	public JdbcExpr notLike(String uriField, Serializable value)
			throws Exception;

	public JdbcExpr notNull(String uriField) throws Exception;

	public JdbcExpr or(JdbcExpr a, JdbcExpr b);

	public OrderByExpr orderBy() throws Exception;

	public VarQuery orderBy(Field field, String asc);

	public VarQuery orderBy(List<Field> field, String asc);

	public VarQuery orderBy(OrderByExpr expr) throws Exception;

	public VarQuery orderBy(String uriField, String asc) throws Exception;

	public JdbcExpr where();

	public VarQuery where(JdbcExpr filter) throws Exception;
}
