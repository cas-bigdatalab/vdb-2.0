package cn.csdb.commons.sql.dialect;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;

public abstract class PreparedStatementAdapter implements PreparedStatement
{

	public void addBatch() throws SQLException
	{
	}

	public void addBatch(String arg0) throws SQLException
	{
	}

	public void cancel() throws SQLException
	{
	}

	public void clearBatch() throws SQLException
	{
	}

	public void clearParameters() throws SQLException
	{
	}

	public void clearWarnings() throws SQLException
	{
	}

	public void close() throws SQLException
	{
	}

	public boolean execute() throws SQLException
	{
		return false;
	}

	public boolean execute(String arg0) throws SQLException
	{
		return false;
	}

	public boolean execute(String arg0, int arg1) throws SQLException
	{
		return false;
	}

	public boolean execute(String arg0, int[] arg1) throws SQLException
	{
		return false;
	}

	public boolean execute(String arg0, String[] arg1) throws SQLException
	{
		return false;
	}

	public int[] executeBatch() throws SQLException
	{
		return null;
	}

	public ResultSet executeQuery() throws SQLException
	{
		return null;
	}

	public ResultSet executeQuery(String arg0) throws SQLException
	{
		return null;
	}

	public int executeUpdate() throws SQLException
	{
		return 0;
	}

	public int executeUpdate(String arg0) throws SQLException
	{
		return 0;
	}

	public int executeUpdate(String arg0, int arg1) throws SQLException
	{
		return 0;
	}

	public int executeUpdate(String arg0, int[] arg1) throws SQLException
	{
		return 0;
	}

	public int executeUpdate(String arg0, String[] arg1) throws SQLException
	{
		return 0;
	}

	public Connection getConnection() throws SQLException
	{
		return null;
	}

	public int getFetchDirection() throws SQLException
	{
		return 0;
	}

	public int getFetchSize() throws SQLException
	{
		return 0;
	}

	public ResultSet getGeneratedKeys() throws SQLException
	{
		return null;
	}

	public int getMaxFieldSize() throws SQLException
	{
		return 0;
	}

	public int getMaxRows() throws SQLException
	{
		return 0;
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return null;
	}

	public boolean getMoreResults() throws SQLException
	{
		return false;
	}

	public boolean getMoreResults(int arg0) throws SQLException
	{
		return false;
	}

	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		return null;
	}

	public int getQueryTimeout() throws SQLException
	{
		return 0;
	}

	public ResultSet getResultSet() throws SQLException
	{
		return null;
	}

	public int getResultSetConcurrency() throws SQLException
	{
		return 0;
	}

	public int getResultSetHoldability() throws SQLException
	{
		return 0;
	}

	public int getResultSetType() throws SQLException
	{
		return 0;
	}

	public int getUpdateCount() throws SQLException
	{
		return 0;
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return null;
	}

	public void setCursorName(String arg0) throws SQLException
	{
	}

	public void setEscapeProcessing(boolean arg0) throws SQLException
	{
	}

	public void setFetchDirection(int arg0) throws SQLException
	{
	}

	public void setFetchSize(int arg0) throws SQLException
	{
	}

	public void setMaxFieldSize(int arg0) throws SQLException
	{
	}

	public void setMaxRows(int arg0) throws SQLException
	{
	}

	public void setQueryTimeout(int arg0) throws SQLException
	{
	}

}