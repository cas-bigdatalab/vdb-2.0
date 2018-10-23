package vdb.mydb.typelib;

import java.io.File;
import java.util.List;
import java.util.Map;

import vdb.metacat.Field;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.StringKeyMap;

/*
 * @author bluejoe
 */
public class FieldType
{
	private boolean _advanced;

	private String _defaultQueryerStyleName;

	private Class _fieldClass;

	private String _name;

	private int _order;

	private ListMap<String, Style> _queryerStyles = new ListMap<String, Style>(
			new StringKeyMap<Style>());

	private boolean _readonly;

	private boolean _sortable;

	private String _title;

	private File _typePath;

	private Map<String, String> _extra;

	public void setExtra(Map<String, String> extra)
	{
		_extra = extra;
	}

	public VdbFieldDriver createDriver(Field field)
			throws InstantiationException, IllegalAccessException
	{
		VdbFieldDriver driver = (VdbFieldDriver) _fieldClass.newInstance();
		driver.setField(field);
		return driver;
	}

	public String getClassName()
	{
		return _fieldClass.getClass().getName();
	}

	public Style getDefaultQueryerStyle()
	{
		if (_defaultQueryerStyleName != null)
			return getQueryerStyle(_defaultQueryerStyleName);

		if (isQueryable())
			return _queryerStyles.list().get(0);

		return null;
	}

	public String getDefaultQueryerStyleName()
	{
		return _defaultQueryerStyleName;
	}

	public String getName()
	{
		return _name;
	}

	public int getOrder()
	{
		return _order;
	}

	public Style getQueryerStyle(String name)
	{
		return _queryerStyles.map().get(name);
	}

	public List<Style> getQueryerStyles()
	{
		return _queryerStyles.list();
	}

	public String getTitle()
	{
		return _title;
	}

	public File getTypePath()
	{
		return _typePath;
	}

	/*
	 * public boolean hasJs(String jsName) { return new File(_typePath,
	 * jsName).exists(); }
	 */

	public boolean isAdvanced()
	{
		return _advanced;
	}

	public boolean isQueryable()
	{
		return !_queryerStyles.list().isEmpty();
	}

	public boolean isReadonly()
	{
		return _readonly;
	}

	public boolean isSortable()
	{
		return _sortable;
	}

	public void setAdvanced(boolean advanced)
	{
		_advanced = advanced;
	}

	public void setDefaultQueryerStyleName(String defaultQueryerStyleName)
	{
		_defaultQueryerStyleName = defaultQueryerStyleName;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setOrder(int order)
	{
		_order = order;
	}

	public void setQueryerStyles(List<Style> styles)
	{
		for (Style style : styles)
		{
			_queryerStyles.add(style.getName(), style);
		}
	}

	public void setReadonly(boolean readonly)
	{
		_readonly = readonly;
	}

	public void setSortable(boolean sortable)
	{
		_sortable = sortable;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setTypePath(File typePath)
	{
		_typePath = typePath;
	}

	public Map<String, String> getExtra()
	{
		return _extra;
	}

	public void setFieldClass(Class fieldClass)
	{
		_fieldClass = fieldClass;
	}
}
