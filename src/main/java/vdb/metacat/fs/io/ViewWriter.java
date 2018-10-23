package vdb.metacat.fs.io;

import org.jdom.Element;

import vdb.metacat.CatalogObject;
import vdb.metacat.View;

public class ViewWriter
{
	private Element _views;

	public ViewWriter(Element views)
	{
		_views = views;
	}

	public void write(View view)
	{
		ElementWriter viewWriter = new ElementWriter(new ElementWriter(_views)
				.appendChild("view"));

		viewWriter.writeAttributes(view.meta(), "source");
		viewWriter.writeAttribute("source", view.getSource().getUri());

		// items!
		for (CatalogObject item : view.getItems())
		{
			ElementWriter itemWriter = new ElementWriter(viewWriter
					.appendChild("item"));
			itemWriter.writeAttribute("source", item.getUri());
		}
	}

}
