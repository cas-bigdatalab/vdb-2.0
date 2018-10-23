package vdb.mydb.xmlbeans;

public class NoBeanWritterFoundException extends Exception
{
	private Object _bean;

	public NoBeanWritterFoundException(Object bean)
	{
		_bean = bean;
	}

}
