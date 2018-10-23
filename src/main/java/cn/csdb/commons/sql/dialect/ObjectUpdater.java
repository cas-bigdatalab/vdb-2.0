package cn.csdb.commons.sql.dialect;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class ObjectUpdater extends PreparedStatementAdapter
{
	private ResultSet _rs;

	public ObjectUpdater(ResultSet rs)
	{
		_rs = rs;
	}

	public void setArray(int arg0, Array arg1) throws SQLException
	{
		_rs.updateArray(arg0, arg1);
	}

	public void setAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
		_rs.updateAsciiStream(arg0, arg1, arg2);
	}

	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException
	{
		_rs.updateBigDecimal(arg0, arg1);
	}

	public void setBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
		_rs.updateBinaryStream(arg0, arg1, arg2);
	}

	public void setBlob(int arg0, Blob arg1) throws SQLException
	{
		_rs.updateBlob(arg0, arg1);
	}

	public void setBoolean(int arg0, boolean arg1) throws SQLException
	{
		_rs.updateBoolean(arg0, arg1);
	}

	public void setByte(int arg0, byte arg1) throws SQLException
	{
		_rs.updateByte(arg0, arg1);
	}

	public void setBytes(int arg0, byte[] arg1) throws SQLException
	{
		_rs.updateBytes(arg0, arg1);
	}

	public void setCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException
	{
		_rs.updateCharacterStream(arg0, arg1, arg2);
	}

	public void setClob(int arg0, Clob arg1) throws SQLException
	{
		_rs.updateClob(arg0, arg1);
	}

	public void setDate(int arg0, java.sql.Date arg1) throws SQLException
	{
		_rs.updateDate(arg0, arg1);
	}

	public void setDate(int arg0, java.sql.Date arg1, Calendar arg2)
			throws SQLException
	{
		setDate(arg0, arg1);
	}

	public void setDouble(int arg0, double arg1) throws SQLException
	{
		_rs.updateDouble(arg0, arg1);
	}

	public void setFloat(int arg0, float arg1) throws SQLException
	{
		_rs.updateFloat(arg0, arg1);
	}

	public void setInt(int arg0, int arg1) throws SQLException
	{
		_rs.updateInt(arg0, arg1);
	}

	public void setLong(int arg0, long arg1) throws SQLException
	{
		_rs.updateLong(arg0, arg1);
	}

	public void setNull(int arg0, int arg1) throws SQLException
	{
		_rs.updateNull(arg0);
	}

	public void setNull(int arg0, int arg1, String arg2) throws SQLException
	{
		setNull(arg0, arg1);
	}

	public void setObject(int arg0, Object arg1) throws SQLException
	{
		_rs.updateObject(arg0, arg1);
	}

	public void setObject(int arg0, Object arg1, int arg2) throws SQLException
	{
		setObject(arg0, arg1);
	}

	public void setObject(int arg0, Object arg1, int arg2, int arg3)
			throws SQLException
	{
		setObject(arg0, arg1);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {

	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {

	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {

	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {

	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {

	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {

	}

	public void setRef(int arg0, Ref arg1) throws SQLException
	{
		_rs.updateRef(arg0, arg1);
	}

	public void setShort(int arg0, short arg1) throws SQLException
	{
		_rs.updateShort(arg0, arg1);
	}

	public void setString(int arg0, String arg1) throws SQLException
	{
		_rs.updateString(arg0, arg1);
	}

	public void setTime(int arg0, Time arg1) throws SQLException
	{
		_rs.updateTime(arg0, arg1);
	}

	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException
	{
		setTime(arg0, arg1);
	}

	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException
	{
		_rs.updateTimestamp(arg0, arg1);
	}

	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2)
			throws SQLException
	{
		setTimestamp(arg0, arg1);
	}

	public void setUnicodeStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
		setAsciiStream(arg0, arg1, arg2);
	}

	public void setURL(int arg0, URL arg1) throws SQLException
	{
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {

	}

	public void setNString(int parameterIndex, String value) throws SQLException {

	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {

	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {

	}

	public boolean isClosed() throws SQLException {
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {

	}

	public boolean isPoolable() throws SQLException {
		return false;
	}

	public void closeOnCompletion() throws SQLException {

	}

	public boolean isCloseOnCompletion() throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}