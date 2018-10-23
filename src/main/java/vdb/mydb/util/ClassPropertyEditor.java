package vdb.mydb.util;

import java.beans.PropertyEditorSupport;

public class ClassPropertyEditor extends PropertyEditorSupport
{
	private ClassLoader _customClassLoader;

	public ClassPropertyEditor()
	{
		this(ClassPropertyEditor.class.getClassLoader());
	}

	public ClassPropertyEditor(ClassLoader customClassLoader)
	{
		super();
		_customClassLoader = customClassLoader;
	}

	public void setAsText(String text) throws IllegalArgumentException
	{
		try
		{
			setValue(Class.forName(text, true, _customClassLoader));
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
}