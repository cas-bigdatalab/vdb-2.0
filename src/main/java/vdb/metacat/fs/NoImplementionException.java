package vdb.metacat.fs;

public class NoImplementionException extends Exception
{
	private Class _interfaceClass;

	public NoImplementionException(Class interfaceClass)
	{
		super();
		_interfaceClass = interfaceClass;
	}

	@Override
	public String getMessage()
	{
		return String.format("no implemention defined for interface `%s`",
				_interfaceClass);
	}

}
