package vdb.mydb.jsp;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoMapping implements RequestHandler
{
	@Override
	public String toString()
	{
		return "do nothing...";
	}

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		return false;
	}
}
