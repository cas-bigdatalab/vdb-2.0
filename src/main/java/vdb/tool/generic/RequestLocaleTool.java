package vdb.tool.generic;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.LocaleEditor;

import vdb.tool.ServletAware;

public class RequestLocaleTool extends ServletContextLocaleTool implements
		ServletAware
{
	private HttpServletRequest _request;

	public String message(String key, Object... params)
	{
		// get locale from request
		Locale locale = (Locale) _request.getAttribute(Locale.class.getName());

		if (locale != null)
			return message(locale, key, params);

		// failed, try to get locale from session
		return super.message(key, params);
	}

	public void setRequest(ServletRequest request)
	{
		_request = (HttpServletRequest) request;
	}

	public void setLocale(String locale)
	{
		LocaleEditor le = new LocaleEditor();
		le.setAsText(locale);
		_request.setAttribute(Locale.class.getName(), le.getValue());
	}

	public void setServletContext(ServletContext servletContext)
	{
	}
}
