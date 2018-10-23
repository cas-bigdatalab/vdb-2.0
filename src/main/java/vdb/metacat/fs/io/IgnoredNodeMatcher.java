package vdb.metacat.fs.io;

import org.jdom.Element;

import cn.csdb.commons.util.Matcher;

public class IgnoredNodeMatcher implements Matcher<Element>
{
	private String[] _blackListNames;

	public IgnoredNodeMatcher(String... blackListNames)
	{
		_blackListNames = blackListNames;
	}

	public boolean matches(Element toMatch)
	{
		String name = toMatch.getName();
		for (String bln : _blackListNames)
		{
			if (name.equalsIgnoreCase(bln))
				return false;
		}

		return true;
	}

}
