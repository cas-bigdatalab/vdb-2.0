package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

import vdb.metacat.CatalogObject;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoExpandClass extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String type = request.getParameter("type");
		String tid = request.getParameter("tid");
		String mid = request.getParameter("mid");
		CatalogObject meta = VdbManager.getEngine().getCatalog().fromId(mid);
		CatalogObject entity = VdbManager.getEngine().getCatalog().fromId(tid);

		Context vc = VdbManager.getEngine().getVelocityEngine().createContext(
				(HttpServletRequest) request, (HttpServletResponse) response);
		vc.put("field", meta);
		vc.put("entity", entity);
		vc.put("meta", meta);

		VdbManager.getEngine().getVelocityEngine().render(
				"/WEB-INF/typelib/" + type + "/options.html", vc,
				response.getWriter());
	}
}
