package vdb.mydb.types;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.Entity;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.ListUtils;
import cn.csdb.commons.util.Matcher;
import cn.csdb.commons.util.StringKeyMap;

public class ViewTypeManager
{
	private ListMap<String, ViewType> _viewTypes = new ListMap<String, ViewType>(
			new ArrayList<ViewType>(), new StringKeyMap<ViewType>());

	public List<ViewType> getDataSetViewTypes()
	{
		return ListUtils.subList(_viewTypes.list(), new Matcher<ViewType>()
		{
			public boolean matches(ViewType toMatch)
			{
				return toMatch.isSuitableFor(DataSet.class);
			}
		});
	}

	public List<ViewType> getDomainViewTypes()
	{
		return ListUtils.subList(_viewTypes.list(), new Matcher<ViewType>()
		{
			public boolean matches(ViewType toMatch)
			{
				return toMatch.isSuitableFor(Domain.class);
			}
		});
	}

	public List<ViewType> getEntityViewTypes()
	{
		return ListUtils.subList(_viewTypes.list(), new Matcher<ViewType>()
		{
			public boolean matches(ViewType toMatch)
			{
				return toMatch.isSuitableFor(Entity.class);
			}
		});
	}

	public List<ViewType> getTypes()
	{
		return _viewTypes.list();
	}

	public ViewType getView(String name)
	{
		return _viewTypes.map().get(name);
	}

	public void setTypes(List<ViewType> dataSetViews)
	{
		for (ViewType vt : dataSetViews)
		{
			Logger.getLogger(this.getClass()).debug(
					String.format("loading view type: %s(%s)", vt.getName(), vt
							.getTitle()));
			_viewTypes.add(vt.getName(), vt);
		}
	}
}
