package vdb.mydb.web;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class RegexpUrlLocator implements PageLocator, ServletContextAware
{
	Pattern _pattern;

	private ServletContext _servletContext;

	String _target;

	private boolean existsPage(String targetPage)
	{
		return new File(_servletContext.getRealPath(targetPage)).exists();
	}

	public String getRealPath(String url)
	{
		Matcher matcher = _pattern.matcher(url);
		if (!matcher.matches())
			return null;

		String targetPage = matcher.replaceAll(_target);

		if (existsPage(targetPage))
			return targetPage;

		return null;
	}

	public String getTarget()
	{
		return _target;
	}

	public void setPattern(String pattern)
	{
		_pattern = Pattern.compile(pattern);
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public void setTarget(String target)
	{
		_target = target;
	}
}
