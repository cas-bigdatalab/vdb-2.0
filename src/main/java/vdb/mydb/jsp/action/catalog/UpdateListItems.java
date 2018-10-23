package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.fs.page.ListItemsPage;
import vdb.metacat.fs.page.PagesManager;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class UpdateListItems extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		PagesManager pm = (PagesManager) VdbManager.getEngine()
				.getApplicationContext().getBean("pagesManager");

		MakeReq2Page mrp = new MakeReq2Page();
		ListItemsPage lip = mrp.makeListItemsPage(request);

		pm.updatePage(lip);
	}

}
