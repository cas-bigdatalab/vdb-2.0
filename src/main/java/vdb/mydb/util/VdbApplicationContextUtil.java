package vdb.mydb.util;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import vdb.mydb.engine.VdbContextLoader;

public class VdbApplicationContextUtil
{
	public static WebApplicationContext createWebApplicationContext(
			ServletContext servletContext,
			ApplicationContext parentParentContext, String contextConfigLocation)
	{
		servletContext
				.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		Logger.getLogger(VdbApplicationContextUtil.class).debug(
				String.format("contextConfigLocation: %s",
						contextConfigLocation));

		DelegatingServletContext fakeServletContext = new DelegatingServletContext(
				servletContext);
		fakeServletContext.addInitParameter("contextConfigLocation",
				contextConfigLocation);
		servletContext = fakeServletContext;

		VdbContextLoader contextLoader = new VdbContextLoader();
		contextLoader.setParentParentContext(parentParentContext);
		contextLoader.initWebApplicationContext(servletContext);

		WebApplicationContext ac = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		return ac;
	}
}
