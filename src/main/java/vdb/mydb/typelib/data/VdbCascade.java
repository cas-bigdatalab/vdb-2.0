package vdb.mydb.typelib.data;

import vdb.metacat.DataSet;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbCascade extends AbstractData
{
	private DataSet _dataSet;

	private String _sql;

	private String _value;

	public String getValue()
	{
		return _value;
	}

	public void setValue(String value)
	{
		_value = value;
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef(_value, getTitle());
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		_value = ddl.getValue();
	}

	public void set_sql(String _sql)
	{
		this._sql = _sql;
	}

	public String get_sql()
	{
		return _sql;
	}

	public String getAsText()
	{

		return null;
	}

	public void setAsText(String text)
	{

	}

	public long getBytes() {
		return _value==null?0:_value.getBytes().length;
	}

	public boolean isEmpty() throws Exception {
		if(_value==null || _value.length()==0){
			return true;
		}else{
			return false;
		}
	}
}
