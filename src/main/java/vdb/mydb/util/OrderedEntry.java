package vdb.mydb.util;

public class OrderedEntry<T>
{
	private double _order;

	private T _value;

	public double getOrder()
	{
		return _order;
	}

	public void setOrder(double order)
	{
		_order = order;
	}

	public T getValue()
	{
		return _value;
	}

	public void setValue(T value)
	{
		_value = value;
	}
}
