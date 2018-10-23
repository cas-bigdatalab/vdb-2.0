package vdb.mydb.jsp.ip;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class IpFilter implements Filter
{
	private List<Address> _allow;

	private List<Address> _deny;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		String clientAddr = request.getRemoteAddr();

		if (_deny != null && !_deny.isEmpty() && hasMatch(clientAddr, _deny))
		{
			handleInvalidAccess(request, response, clientAddr);
			return;
		}

		if (_allow != null && !_allow.isEmpty()
				&& !hasMatch(clientAddr, _allow))
		{
			handleInvalidAccess(request, response, clientAddr);
			return;
		}

		chain.doFilter(request, response);
	}

	private void handleInvalidAccess(ServletRequest request,
			ServletResponse response, String clientAddr) throws IOException
	{
		((HttpServletResponse) response)
				.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	private boolean hasMatch(String clientAddr, List<Address> addressList)
	{
		for (Address addr : addressList)
		{
			if (addr.matches(clientAddr))
				return true;
		}

		return false;
	}

	/**
	 * Destroy method for this filter
	 * 
	 */
	public void destroy()
	{
		this._allow = null;
		this._deny = null;
	}

	/**
	 * Init method for this filter
	 * 
	 */
	public void init(FilterConfig filterConfig)
	{
	}

	public void setAllow(List<Address> allow)
	{
		_allow = allow;
	}

	public void setDeny(List<Address> deny)
	{
		_deny = deny;
	}
}
