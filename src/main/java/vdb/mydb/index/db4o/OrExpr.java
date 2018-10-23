package vdb.mydb.index.db4o;

import cn.csdb.commons.util.Matcher;

public class OrExpr<T> implements Matcher<T>
{
	private Matcher<T>[] _matchers;

	public OrExpr(Matcher<T>[] matchers)
	{
		_matchers = matchers;
	}

	public boolean matches(T toMatch)
	{
		for (Matcher<T> matcher : _matchers)
		{
			if (matcher.matches(toMatch))
				return true;
		}
		return false;
	}
}
