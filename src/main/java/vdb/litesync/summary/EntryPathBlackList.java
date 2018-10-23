package vdb.litesync.summary;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.csdb.commons.util.Matcher;

public class EntryPathBlackList implements Matcher<String>
{
	List<Pattern> _patterns = new ArrayList<Pattern>();

	public void addPattern(String pattern)
	{
		_patterns.add(Pattern.compile(pattern));
	}

	public boolean matches(String arg0)
	{
		for (Pattern pattern : _patterns)
		{
			if (pattern.matcher(arg0).matches())
				return false;
		}

		return true;
	}

	public void setPatterns(List<String> patterns)
	{
		for (String pattern : patterns)
		{
			addPattern(pattern);
		}
	}
}
