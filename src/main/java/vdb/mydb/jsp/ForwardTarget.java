package vdb.mydb.jsp;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ForwardTarget
{
	public void forward(Matcher matcher, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;
}
