package vdb.mydb.jsp;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler
{
	/**
	 * @return DO NOT continue next handler or do?
	 */
	boolean handle(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain) throws ServletException, IOException;
}