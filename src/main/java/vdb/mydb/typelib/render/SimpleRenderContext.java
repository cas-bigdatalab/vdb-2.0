package vdb.mydb.typelib.render;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

public class SimpleRenderContext implements RenderContext
{
	private ServletContext _application;

	private HttpServletRequest _request;

	private HttpServletResponse _response;

	public SimpleRenderContext(PageContext pageContext)
	{
		this(pageContext.getRequest(), pageContext.getResponse(), pageContext
				.getServletContext());
	}

	public SimpleRenderContext(RenderContext context)
	{
		_request = context.getRequest();
		_response = context.getResponse();
		_application = context.getServletContext();
	}

	public SimpleRenderContext(ServletRequest request,
			ServletResponse response, ServletContext application)
	{
		super();
		_request = (HttpServletRequest) request;
		_response = (HttpServletResponse) response;
		_application = application;
	}

	public HttpServletRequest getRequest()
	{
		return _request;
	}

	public HttpServletResponse getResponse()
	{
		return _response;
	}

	public ServletContext getServletContext()
	{
		return _application;
	}
}
