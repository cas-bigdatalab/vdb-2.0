package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Domain;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateDomain extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		Domain domain = VdbManager.getInstance().getDomain();
		VdbDomain domainEx = ((VdbDomain) domain);
		domainEx.attach(request);

		VdbManager.getInstance().getCatalogManager().saveDomain(domain);
	}

}
