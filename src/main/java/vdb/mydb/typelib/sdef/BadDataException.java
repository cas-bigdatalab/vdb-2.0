package vdb.mydb.typelib.sdef;

public class BadDataException extends SdefException
{
	private String _propertyName;

	BadDataException(String propertyName)
	{
		_propertyName = propertyName;
	}
}