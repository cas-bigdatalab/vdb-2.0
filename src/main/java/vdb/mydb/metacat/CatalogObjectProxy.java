package vdb.mydb.metacat;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

import vdb.metacat.CatalogObject;
import vdb.mydb.VdbManager;

public class CatalogObjectProxy
{
	private CatalogObject _meta;

	public CatalogObjectProxy(CatalogObject meta)
	{
		_meta = meta;
	}

	@SuppressWarnings("unchecked")
	public void attach(ServletRequest request) throws Exception
	{
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			String name = e.nextElement();
			if (name.startsWith("P_"))
			{
				_meta.set(name.substring(2), request.getParameter(name));
			}

			if (name.startsWith("R_"))
			{
				_meta.set(name.substring(2), ((CatalogObject) VdbManager
						.getEngine().getCatalog().fromId(
								request.getParameter(name))).getUri());
			}
		}

	}
}
