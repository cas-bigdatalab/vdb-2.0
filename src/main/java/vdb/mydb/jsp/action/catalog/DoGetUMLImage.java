package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.jsp.CreateUMLImage;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetUMLImage extends ServletActionProxy
{
	@SuppressWarnings("deprecation")
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext arg2) throws Exception
	{
		String type = request.getParameter("type");
		String path1 = request.getRealPath("");
		CreateUMLImage cui = new CreateUMLImage();

		String pathC = null;
		String pathE = null;

		if (type.equals("entity"))
		{
			String tid = request.getParameter("tid");
			Entity entity = VdbManager.getEngine().getCatalog().fromId(tid);

			String ePath = path1 + "/console/catalog/UMLImages/"
					+ entity.getUri() + "(e).jpg";
			pathE = "/console/catalog/UMLImages/" + entity.getUri() + "(e).jpg";
			cui.getEntityUML(entity, ePath, "e");

			String cPath = path1 + "/console/catalog/UMLImages/"
					+ entity.getUri() + "(c).jpg";
			pathC = "/console/catalog/UMLImages/" + entity.getUri() + "(c).jpg";
			cui.getEntityUML(entity, cPath, "c");
		}
		else
		{
			String dsid = request.getParameter("dsid");
			DataSet ds = VdbManager.getEngine().getCatalog().fromId(dsid);

			String ePath = path1 + "/console/catalog/UMLImages/" + ds.getUri()
					+ "(e).jpg";
			pathE = "/console/catalog/UMLImages/" + ds.getUri() + "(e).jpg";
			cui.getDataSetUML(ds, ePath, "e");

			String cPath = path1 + "/console/catalog/UMLImages/" + ds.getUri()
					+ "(c).jpg";
			pathC = "/console/catalog/UMLImages/" + ds.getUri() + "(c).jpg";
			cui.getDataSetUML(ds, cPath, "c");
		}
		request.setAttribute("type", type);
		request.setAttribute("pathC", pathC);
		request.setAttribute("pathE", pathE);
	}

}
