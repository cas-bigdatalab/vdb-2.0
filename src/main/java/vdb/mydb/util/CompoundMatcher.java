package vdb.mydb.util;

import cn.csdb.commons.util.Matcher;

public class CompoundMatcher<T> implements Matcher<T>
{
	private Logic _logic;

	public enum Logic
	{
		AND, OR
	}

	private Matcher<T>[] _matchers;

	public CompoundMatcher(Logic logic, Matcher<T>... matchers)
	{
		_logic = logic;
		_matchers = matchers;
	}

	public boolean matches(T arg0)
	{
		if (_logic == Logic.AND)
		{
			for (Matcher<T> matcher : _matchers)
			{
				if (!matcher.matches(arg0))
					return false;
			}
			return true;
		}

		if (_logic == Logic.OR)
		{
			for (Matcher<T> matcher : _matchers)
			{
				if (matcher.matches(arg0))
					return true;
			}
			return false;
		}

		return false;
	}
}
