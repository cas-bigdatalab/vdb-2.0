package vdb.mydb.widget;

import java.io.File;
import java.util.Map;

/**
 * a widget which is defined in /WEB-INF/widget folder. a widget always has a
 * meta.xml which tells what it is and a params.properties which stores its
 * configuaration
 */
public class Widget
{
	String _name;

	String _title;

	Map _properties;

	String _description;

	File _widgetDir;

	boolean _asyc;

	boolean _selectable = true;

	boolean _readOnly = false;

	public boolean getAsyc()
	{
		return _asyc;
	}

	public void setAsyc(boolean asyc)
	{
		this._asyc = asyc;
	}

	public boolean getSelectable()
	{
		return _selectable;
	}

	public void setSelectable(boolean selectable)
	{
		this._selectable = selectable;
	}

	public boolean getReadOnly()
	{
		return _readOnly;
	}

	public void setReadOnly(boolean readOnly)
	{
		this._readOnly = readOnly;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getEnglishName()
	{
		return _name;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String title)
	{
		this._title = title;
	}

	public Map<String, String> getProps()
	{
		return _properties;
	}

	public Map<String, String> getProperties()
	{
		return _properties;
	}

	public void setProperties(Map properties)
	{
		_properties = properties;
	}

	public File getWidgetDir()
	{
		return _widgetDir;
	}

	public void setWidgetDir(File widgetDir)
	{
		_widgetDir = widgetDir;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}
}
