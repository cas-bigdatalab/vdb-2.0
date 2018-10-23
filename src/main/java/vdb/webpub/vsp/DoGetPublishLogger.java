package vdb.webpub.vsp;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.webpub.publisher.ProgressReporter;
import vdb.webpub.publisher.VdbProgressReporter;

public class DoGetPublishLogger extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		HttpSession session = ((HttpServletRequest) request).getSession();

		ProgressReporter progressBar = (ProgressReporter) session
				.getAttribute(VdbProgressReporter.class.getName());

		request.setAttribute("progress", progressBar);
	}
}
