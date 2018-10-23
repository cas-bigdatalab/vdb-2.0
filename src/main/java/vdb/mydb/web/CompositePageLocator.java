package vdb.mydb.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositePageLocator implements PageLocator
{
	Map<String, String> _cache = new HashMap<String, String>();

	List<PageLocator> _locators;

	boolean _usingCache;

	public List<PageLocator> getLocators()
	{
		return _locators;
	}

	public String getRealPath(String url)
	{
		if (_usingCache && _cache.containsKey(url))
		{
			return _cache.get(url);
		}

		for (PageLocator locator : _locators)
		{
			String realPath = locator.getRealPath(url);
			if (realPath != null)
			{
				_cache.put(url, realPath);
				return realPath;
			}
		}

		return null;
	}

	public boolean isUsingCache()
	{
		return _usingCache;
	}

	public void setLocators(List<PageLocator> locators)
	{
		_locators = locators;
	}

	public void addLocator(PageLocator locator)
	{
		_locators.add(locator);
	}

	public void setUsingCache(boolean usingCache)
	{
		_usingCache = usingCache;
	}

}
