package vdb.mydb.jsp;

import java.io.IOException;
import java.util.regex.Matcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vdb.mydb.VdbManager;

public class ThemePathShortCut extends UrlForward implements RequestHandler
{
	public boolean handle(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		String uri = ((HttpServletRequest) request).getRequestURI();
		Matcher matcher = _pattern.matcher(uri);
		if (matcher.matches())
		{
			String target = matcher.replaceAll(_target.replaceAll(
					"SELECTED_THEME_NAME", VdbManager.getEngine().getTheme()
							.getName()));

			VdbManager.getEngine().getServletContext().getRequestDispatcher(
					target).forward(request, response);

			Logger.getLogger(this.getClass()).debug(uri + ": " + target);
			return true;
		}

		return false;
	}
}
