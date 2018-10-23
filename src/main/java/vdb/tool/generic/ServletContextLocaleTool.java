package vdb.tool.generic;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.beans.propertyeditors.LocaleEditor;

import vdb.mydb.VdbManager;

public class ServletContextLocaleTool
{
	private ServletContext getServletContext()
	{
		return VdbManager.getEngine().getServletContext();
	}

	public String message(String key, Object... params)
	{
		Locale locale = getLocale();
		return message(locale, key, params);
	}

	public String message(Locale locale, String key, Object... params)
	{
		return VdbManager.getEngine().getApplicationContext().getMessage(key,
				params, locale);
	}

	public void setLocale(String locale)
	{
		LocaleEditor le = new LocaleEditor();
		le.setAsText(locale);
		getServletContext().setAttribute(Locale.class.getName(), le.getValue());
	}

	public Locale getLocale()
	{
		return (Locale) getServletContext()
				.getAttribute(Locale.class.getName());
	}
}
