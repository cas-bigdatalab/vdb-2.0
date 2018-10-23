package vdb.mydb.jsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import vdb.mydb.util.OrderedEntry;

public class UrlMappingFilter implements Filter, BeanFactoryPostProcessor
{
	private List<RequestHandler> _handlers;

	public void destroy()
	{
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws ServletException, IOException
	{
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		for (RequestHandler _handler : _handlers)
		{
			if (_handler.handle(request, response, arg2))
			{
				Logger.getLogger(this.getClass()).debug(
						request.getRequestURI() + ": " + _handler.toString());
				return;
			}
		}

		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException
	{
		List<OrderedEntry<RequestHandler>> handlers = new ArrayList<OrderedEntry<RequestHandler>>();
		for (String name : arg0.getBeanNamesForType(UrlMappings.class))
		{
			UrlMappings list = (UrlMappings) arg0.getBean(name);
			handlers.addAll(list.getHandlers());
		}

		Collections.sort(handlers, new Comparator<OrderedEntry>()
		{
			public int compare(OrderedEntry arg0, OrderedEntry arg1)
			{
				return ((Double) arg0.getOrder()).compareTo(arg1.getOrder());
			}
		});

		_handlers = new ArrayList<RequestHandler>();
		for (OrderedEntry<RequestHandler> item : handlers)
		{
			RequestHandler value = item.getValue();
			Logger.getLogger(this.getClass()).debug(
					String.format("loading url-mapping: %s", value));
			_handlers.add(value);
		}
	}
}
