package vdb.metacat.meta;

public class BasicValueGetter<T> implements ValueGetter<T>
{
	private T _o;

	public BasicValueGetter()
	{
	}

	public BasicValueGetter(T o)
	{
		_o = o;
	}

	public T get()
	{
		return _o;
	}

	public void setValue(T o)
	{
		_o = o;
	}
}
