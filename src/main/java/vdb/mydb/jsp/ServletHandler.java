package vdb.mydb.jsp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.ServletContextAware;

public class ServletHandler implements RequestHandler, ServletContextAware,
		InitializingBean
{
	@Override
	public String toString()
	{
		return _patternString + "-->" + _target;
	}

	Pattern _pattern;

	HttpServlet _target;

	Map<String, String> _params = new HashMap<String, String>();

	private String _patternString;

	private ServletContext _servletContext;

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		String uri = ((HttpServletRequest) request).getRequestURI();
		Matcher matcher = _pattern.matcher(uri);
		if (matcher.matches())
		{
			Logger.getLogger(this.getClass()).debug(
					uri + ": " + this.getClass());

			_target.service(request, response);
			return true;
		}

		return false;
	}

	public HttpServlet getTarget()
	{
		return _target;
	}

	public void setTarget(HttpServlet target)
	{
		_target = target;
	}

	public void setPattern(String pattern)
	{
		_patternString = pattern;
		_pattern = Pattern.compile(pattern);
	}

	public void afterPropertiesSet() throws Exception
	{
		MockServletConfig msc = new MockServletConfig(_servletContext);
		for (Entry<String, String> en : _params.entrySet())
		{
			msc.addInitParameter(en.getKey(), en.getValue());
		}

		_target.init(msc);
	}

	public void setParams(Map<String, String> params)
	{
		_params = params;
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}
}
