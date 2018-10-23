package vdb.mydb.index.db4o;

import cn.csdb.commons.util.Matcher;

public class KeywordsMatcher implements Matcher<Db4oIndexEntry>
{
	private String _keyword;

	public KeywordsMatcher(String keyword)
	{
		_keyword = keyword;
	}

	public boolean matches(Db4oIndexEntry record)
	{
		return record.getText().indexOf(_keyword) >= 0;
	}
}
