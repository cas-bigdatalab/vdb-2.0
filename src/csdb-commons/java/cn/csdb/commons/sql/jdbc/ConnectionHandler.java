/*
 * Created on 2006-6-19
 */
package cn.csdb.commons.sql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionHandler
{
	void handle(Connection conn) throws SQLException;
}
