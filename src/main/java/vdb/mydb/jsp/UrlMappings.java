package vdb.mydb.jsp;

import java.util.List;

import vdb.mydb.util.OrderedEntry;

public class UrlMappings
{
	private List<OrderedEntry<RequestHandler>> _handlers;

	public void setHandlers(List<OrderedEntry<RequestHandler>> handlers)
	{
		_handlers = handlers;
	}

	public List<OrderedEntry<RequestHandler>> getHandlers()
	{
		return _handlers;
	}
}
