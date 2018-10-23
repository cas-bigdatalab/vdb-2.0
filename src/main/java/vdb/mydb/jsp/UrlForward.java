package vdb.mydb.jsp;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

public class UrlForward implements RequestHandler, ServletContextAware
{
	@Override
	public String toString()
	{
		return _patternString + "-->" + _target;
	}

	Pattern _pattern;

	String _target;

	private String _patternString;

	private ServletContext _servletContext;

	private boolean _checkFileExistence;

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		// DO NOT reenter this handle() method!
		if (request.getAttribute(this.getClass().getName()) != null)
			return false;

		String uri = ((HttpServletRequest) request).getRequestURI();
		Matcher matcher = _pattern.matcher(uri);
		if (matcher.matches())
		{
			String target = matcher.replaceAll(_target);
			if (_checkFileExistence)
			{
				if (!existsTarget(target))
				{
					return false;
				}
			}

			Logger.getLogger(this.getClass()).debug(uri + ": " + target);
			request.setAttribute(this.getClass().getName(), this);
			_servletContext.getRequestDispatcher(target).forward(request,
					response);

			return true;
		}

		return false;
	}

	private boolean existsTarget(String target)
	{
		return new File(_servletContext.getRealPath(target)).exists();
	}

	public void setPattern(String pattern)
	{
		_patternString = pattern;
		_pattern = Pattern.compile(pattern);
	}

	public String getTarget()
	{
		return _target;
	}

	public void setTarget(String target)
	{
		_target = target;
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public void setCheckFileExistence(boolean checkFileExistence)
	{
		_checkFileExistence = checkFileExistence;
	}
}
