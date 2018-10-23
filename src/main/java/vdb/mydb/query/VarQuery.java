package vdb.mydb.query;

import java.io.Serializable;

import vdb.metacat.Entity;

//FIXME: VarQueryExpr?
public interface VarQuery extends JdbcExpr
{

	VarQuery bindVariable(String name, Serializable value);

	Entity getEntity();

}