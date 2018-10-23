/*
 * Created on 2006-1-22
 */
package cn.csdb.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bluejoe
 */
public class Parameters
{
	private List<Serializable> _parameters = new ArrayList<Serializable>();

	public Parameters()
	{
	}

	public Parameters add(Serializable value)
	{
		_parameters.add(value);
		return this;
	}

	public Parameters addAll(List<Serializable> parameters)
	{
		_parameters.addAll(parameters);
		return this;
	}

	public Parameters clear()
	{
		_parameters.clear();
		return this;
	}

	public Object get(int i)
	{
		return _parameters.get(i - 1);
	}

	public List getAll()
	{
		return _parameters;
	}

	public Parameters set(int i, Serializable value)
	{
		synchronized (this)
		{
			while (_parameters.size() < i)
			{
				_parameters.add(null);
			}
		}

		_parameters.set(i - 1, value);
		return this;
	}

	public int size()
	{
		return _parameters.size();
	}
}
