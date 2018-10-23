package cn.csdb.commons.sql.dialect;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class StringUpdater extends PreparedStatementAdapter
{
	private String _s;

	public StringUpdater()
	{
		_s = null;
	}

	public String getString()
	{
		return _s;
	}

	public void setArray(int arg0, Array arg1) throws SQLException
	{
	}

	public void setAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
		try
		{
			byte[] bytes = new byte[arg1.available()];
			arg1.read(bytes);

			_s = new String(bytes);
		}
		catch (Exception e)
		{
		}
	}

	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException
	{
		_s = arg1.toString();
	}

	public void setBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
		try
		{
			byte[] bytes = new byte[arg1.available()];
			arg1.read(bytes);

			_s = new String(bytes);
		}
		catch (Exception e)
		{
		}
	}

	public void setBlob(int arg0, Blob arg1) throws SQLException
	{
		setBinaryStream(arg0, arg1.getBinaryStream(), (int) arg1.length());
	}

	public void setBoolean(int arg0, boolean arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setByte(int arg0, byte arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setBytes(int arg0, byte[] arg1) throws SQLException
	{
		_s = new String(arg1);
	}

	public void setCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException
	{
		int start = 1;
		_s = "";

		try
		{
			while (true)
			{
				char[] bytes = new char[1024];
				int count = arg1.read(bytes, start, 1024);
				_s += new String(bytes);
				if (count < 1024)
					break;

				start += count;
			}
		}
		catch (Exception e)
		{
		}
	}

	public void setClob(int arg0, Clob arg1) throws SQLException
	{
		setAsciiStream(arg0, arg1.getAsciiStream(), (int) arg1.length());
	}

	public void setDate(int arg0, java.sql.Date arg1) throws SQLException
	{
		_s = new SimpleDateFormat("yyyy-MM-dd").format(arg1);
	}

	public void setDate(int arg0, java.sql.Date arg1, Calendar arg2)
			throws SQLException
	{
		setDate(arg0, arg1);
	}

	public void setDouble(int arg0, double arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setFloat(int arg0, float arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setInt(int arg0, int arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setLong(int arg0, long arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setNull(int arg0, int arg1) throws SQLException
	{
		_s = null;
	}

	public void setNull(int arg0, int arg1, String arg2) throws SQLException
	{
		setNull(arg0, arg1);
	}

	public void setObject(int arg0, Object arg1) throws SQLException
	{
		_s = "" + arg1;
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
		_s = "" + arg1;
	}

	public void setShort(int arg0, short arg1) throws SQLException
	{
		_s = "" + arg1;
	}

	public void setString(int arg0, String arg1) throws SQLException
	{
		_s = arg1;
	}

	public void setTime(int arg0, Time arg1) throws SQLException
	{
		_s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arg1);
	}

	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException
	{
		setTime(arg0, arg1);
	}

	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException
	{
		_s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arg1);
	}

	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2)
			throws SQLException
	{
		setTimestamp(arg0, arg1);
	}

	public void setUnicodeStream(int arg0, InputStream arg1, int arg2)
			throws SQLException
	{
	}

	public void setURL(int arg0, URL arg1) throws SQLException
	{
		_s = "" + arg1;
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
