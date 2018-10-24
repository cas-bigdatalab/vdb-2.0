package cn.csdb.commons.sql.jdbc.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class RangedResultSet implements ResultSet
{
	private ResultSet _rs;

	private int _queryBeginning;

	private int _querySize;

	private int _queryCount;

	public RangedResultSet(ResultSet rs, int beginning, int size)
	{
		_rs = rs;

		_queryBeginning = beginning;
		_querySize = size;
		_queryCount = 0;
	}

	public boolean absolute(int row) throws SQLException
	{
		return _rs.absolute(row);
	}

	public void afterLast() throws SQLException
	{
		_rs.afterLast();
	}

	public void beforeFirst() throws SQLException
	{
		_rs.beforeFirst();
	}

	public void cancelRowUpdates() throws SQLException
	{
		_rs.cancelRowUpdates();
	}

	public void clearWarnings() throws SQLException
	{
		_rs.clearWarnings();
	}

	public void close() throws SQLException
	{
		_rs.close();
	}

	public void deleteRow() throws SQLException
	{
		_rs.deleteRow();
	}

	public int findColumn(String columnName) throws SQLException
	{
		return _rs.findColumn(columnName);
	}

	public boolean first() throws SQLException
	{
		return _rs.first();
	}

	public Array getArray(int i) throws SQLException
	{
		return _rs.getArray(i);
	}

	public Array getArray(String colName) throws SQLException
	{
		return _rs.getArray(colName);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException
	{
		return _rs.getAsciiStream(columnIndex);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException
	{
		return _rs.getAsciiStream(columnName);
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException
	{
		return _rs.getBigDecimal(columnIndex, scale);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException
	{
		return _rs.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException
	{
		return _rs.getBigDecimal(columnName, scale);
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException
	{
		return _rs.getBigDecimal(columnName);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException
	{
		return _rs.getBinaryStream(columnIndex);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException
	{
		return _rs.getBinaryStream(columnName);
	}

	public Blob getBlob(int i) throws SQLException
	{
		return _rs.getBlob(i);
	}

	public Blob getBlob(String colName) throws SQLException
	{
		return _rs.getBlob(colName);
	}

	public boolean getBoolean(int columnIndex) throws SQLException
	{
		return _rs.getBoolean(columnIndex);
	}

	public boolean getBoolean(String columnName) throws SQLException
	{
		return _rs.getBoolean(columnName);
	}

	public byte getByte(int columnIndex) throws SQLException
	{
		return _rs.getByte(columnIndex);
	}

	public byte getByte(String columnName) throws SQLException
	{
		return _rs.getByte(columnName);
	}

	public byte[] getBytes(int columnIndex) throws SQLException
	{
		return _rs.getBytes(columnIndex);
	}

	public byte[] getBytes(String columnName) throws SQLException
	{
		return _rs.getBytes(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException
	{
		return _rs.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnName) throws SQLException
	{
		return _rs.getCharacterStream(columnName);
	}

	public Clob getClob(int i) throws SQLException
	{
		return _rs.getClob(i);
	}

	public Clob getClob(String colName) throws SQLException
	{
		return _rs.getClob(colName);
	}

	public int getConcurrency() throws SQLException
	{
		return _rs.getConcurrency();
	}

	public String getCursorName() throws SQLException
	{
		return _rs.getCursorName();
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException
	{
		return _rs.getDate(columnIndex, cal);
	}

	public Date getDate(int columnIndex) throws SQLException
	{
		return _rs.getDate(columnIndex);
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException
	{
		return _rs.getDate(columnName, cal);
	}

	public Date getDate(String columnName) throws SQLException
	{
		return _rs.getDate(columnName);
	}

	public double getDouble(int columnIndex) throws SQLException
	{
		return _rs.getDouble(columnIndex);
	}

	public double getDouble(String columnName) throws SQLException
	{
		return _rs.getDouble(columnName);
	}

	public int getFetchDirection() throws SQLException
	{
		return _rs.getFetchDirection();
	}

	public int getFetchSize() throws SQLException
	{
		return _rs.getFetchSize();
	}

	public float getFloat(int columnIndex) throws SQLException
	{
		return _rs.getFloat(columnIndex);
	}

	public float getFloat(String columnName) throws SQLException
	{
		return _rs.getFloat(columnName);
	}

	public int getInt(int columnIndex) throws SQLException
	{
		return _rs.getInt(columnIndex);
	}

	public int getInt(String columnName) throws SQLException
	{
		return _rs.getInt(columnName);
	}

	public long getLong(int columnIndex) throws SQLException
	{
		return _rs.getLong(columnIndex);
	}

	public long getLong(String columnName) throws SQLException
	{
		return _rs.getLong(columnName);
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return _rs.getMetaData();
	}

	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException
	{
		return _rs.getObject(arg0, arg1);
	}

	public Object getObject(int columnIndex) throws SQLException
	{
		return _rs.getObject(columnIndex);
	}

	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException
	{
		return _rs.getObject(arg0, arg1);
	}

	public Object getObject(String columnName) throws SQLException
	{
		return _rs.getObject(columnName);
	}

	public Ref getRef(int i) throws SQLException
	{
		return _rs.getRef(i);
	}

	public Ref getRef(String colName) throws SQLException
	{
		return _rs.getRef(colName);
	}

	public int getRow() throws SQLException
	{
		return _rs.getRow();
	}

	public short getShort(int columnIndex) throws SQLException
	{
		return _rs.getShort(columnIndex);
	}

	public short getShort(String columnName) throws SQLException
	{
		return _rs.getShort(columnName);
	}

	public Statement getStatement() throws SQLException
	{
		return _rs.getStatement();
	}

	public String getString(int columnIndex) throws SQLException
	{
		return _rs.getString(columnIndex);
	}

	public String getString(String columnName) throws SQLException
	{
		return _rs.getString(columnName);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException
	{
		return _rs.getTime(columnIndex, cal);
	}

	public Time getTime(int columnIndex) throws SQLException
	{
		return _rs.getTime(columnIndex);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException
	{
		return _rs.getTime(columnName, cal);
	}

	public Time getTime(String columnName) throws SQLException
	{
		return _rs.getTime(columnName);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException
	{
		return _rs.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException
	{
		return _rs.getTimestamp(columnIndex);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException
	{
		return _rs.getTimestamp(columnName, cal);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException
	{
		return _rs.getTimestamp(columnName);
	}

	public int getType() throws SQLException
	{
		return _rs.getType();
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException
	{
		return _rs.getUnicodeStream(columnIndex);
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException
	{
		return _rs.getUnicodeStream(columnName);
	}

	public URL getURL(int columnIndex) throws SQLException
	{
		return _rs.getURL(columnIndex);
	}

	public URL getURL(String columnName) throws SQLException
	{
		return _rs.getURL(columnName);
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return _rs.getWarnings();
	}

	public void insertRow() throws SQLException
	{
		_rs.insertRow();
	}

	public boolean isAfterLast() throws SQLException
	{
		return _rs.isAfterLast();
	}

	public boolean isBeforeFirst() throws SQLException
	{
		return _rs.isBeforeFirst();
	}

	public boolean isFirst() throws SQLException
	{
		return _rs.isFirst();
	}

	public boolean isLast() throws SQLException
	{
		return _rs.isLast();
	}

	public boolean last() throws SQLException
	{
		return _rs.last();
	}

	public void moveToCurrentRow() throws SQLException
	{
		_rs.moveToCurrentRow();
	}

	public void moveToInsertRow() throws SQLException
	{
		_rs.moveToInsertRow();
	}

	public boolean next() throws SQLException
	{
		if (_queryCount == _querySize)
		{
			return false;
		}

		// first call
		if (_queryCount == 0)
		{
			if (!_rs.absolute(_queryBeginning))
			{
				return false;
			}

			_queryCount++;
			return true;
		}

		if (!_rs.next())
		{
			return false;
		}

		_queryCount++;
		return true;
	}

	public boolean previous() throws SQLException
	{
		return _rs.previous();
	}

	public void refreshRow() throws SQLException
	{
		_rs.refreshRow();
	}

	public boolean relative(int rows) throws SQLException
	{
		return _rs.relative(rows);
	}

	public boolean rowDeleted() throws SQLException
	{
		return _rs.rowDeleted();
	}

	public boolean rowInserted() throws SQLException
	{
		return _rs.rowInserted();
	}

	public boolean rowUpdated() throws SQLException
	{
		return _rs.rowUpdated();
	}

	public void setFetchDirection(int direction) throws SQLException
	{
		_rs.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException
	{
		_rs.setFetchSize(rows);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException
	{
		_rs.updateArray(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException
	{
		_rs.updateArray(columnName, x);
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		return null;
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return null;
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {

	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {

	}

	public int getHoldability() throws SQLException {
		return 0;
	}

	public boolean isClosed() throws SQLException {
		return false;
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {

	}

	public void updateNString(String columnLabel, String nString) throws SQLException {

	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {

	}

	public NClob getNClob(int columnIndex) throws SQLException {
		return null;
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		return null;
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return null;
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return null;
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {

	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {

	}

	public String getNString(int columnIndex) throws SQLException {
		return null;
	}

	public String getNString(String columnLabel) throws SQLException {
		return null;
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return null;
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return null;
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {

	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {

	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {

	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {

	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return null;
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		return null;
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException
	{
		_rs.updateAsciiStream(columnIndex, x, length);
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException
	{
		_rs.updateAsciiStream(columnName, x, length);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException
	{
		_rs.updateBigDecimal(columnIndex, x);
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException
	{
		_rs.updateBigDecimal(columnName, x);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException
	{
		_rs.updateBinaryStream(columnIndex, x, length);
	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException
	{
		_rs.updateBinaryStream(columnName, x, length);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException
	{
		_rs.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException
	{
		_rs.updateBlob(columnName, x);
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException
	{
		_rs.updateBoolean(columnIndex, x);
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException
	{
		_rs.updateBoolean(columnName, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException
	{
		_rs.updateByte(columnIndex, x);
	}

	public void updateByte(String columnName, byte x) throws SQLException
	{
		_rs.updateByte(columnName, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException
	{
		_rs.updateBytes(columnIndex, x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException
	{
		_rs.updateBytes(columnName, x);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException
	{
		_rs.updateCharacterStream(columnIndex, x, length);
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException
	{
		_rs.updateCharacterStream(columnName, reader, length);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException
	{
		_rs.updateClob(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException
	{
		_rs.updateClob(columnName, x);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException
	{
		_rs.updateDate(columnIndex, x);
	}

	public void updateDate(String columnName, Date x) throws SQLException
	{
		_rs.updateDate(columnName, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException
	{
		_rs.updateDouble(columnIndex, x);
	}

	public void updateDouble(String columnName, double x) throws SQLException
	{
		_rs.updateDouble(columnName, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException
	{
		_rs.updateFloat(columnIndex, x);
	}

	public void updateFloat(String columnName, float x) throws SQLException
	{
		_rs.updateFloat(columnName, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException
	{
		_rs.updateInt(columnIndex, x);
	}

	public void updateInt(String columnName, int x) throws SQLException
	{
		_rs.updateInt(columnName, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException
	{
		_rs.updateLong(columnIndex, x);
	}

	public void updateLong(String columnName, long x) throws SQLException
	{
		_rs.updateLong(columnName, x);
	}

	public void updateNull(int columnIndex) throws SQLException
	{
		_rs.updateNull(columnIndex);
	}

	public void updateNull(String columnName) throws SQLException
	{
		_rs.updateNull(columnName);
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException
	{
		_rs.updateObject(columnIndex, x, scale);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException
	{
		_rs.updateObject(columnIndex, x);
	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException
	{
		_rs.updateObject(columnName, x, scale);
	}

	public void updateObject(String columnName, Object x) throws SQLException
	{
		_rs.updateObject(columnName, x);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException
	{
		_rs.updateRef(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException
	{
		_rs.updateRef(columnName, x);
	}

	public void updateRow() throws SQLException
	{
		_rs.updateRow();
	}

	public void updateShort(int columnIndex, short x) throws SQLException
	{
		_rs.updateShort(columnIndex, x);
	}

	public void updateShort(String columnName, short x) throws SQLException
	{
		_rs.updateShort(columnName, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException
	{
		_rs.updateString(columnIndex, x);
	}

	public void updateString(String columnName, String x) throws SQLException
	{
		_rs.updateString(columnName, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException
	{
		_rs.updateTime(columnIndex, x);
	}

	public void updateTime(String columnName, Time x) throws SQLException
	{
		_rs.updateTime(columnName, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException
	{
		_rs.updateTimestamp(columnIndex, x);
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException
	{
		_rs.updateTimestamp(columnName, x);
	}

	public boolean wasNull() throws SQLException
	{
		return _rs.wasNull();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
