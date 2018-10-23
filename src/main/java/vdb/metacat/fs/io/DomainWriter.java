package vdb.metacat.fs.io;

import java.io.File;

import org.jdom.Element;

import vdb.metacat.Domain;
import vdb.metacat.View;

public class DomainWriter
{
	private File _domainRoot;

	public DomainWriter(File domainRoot)
	{
		_domainRoot = domainRoot;
	}

	public void write(Domain domain) throws Exception
	{
		Element root = new Element("domain");

		Element ve = new Element("version");
		root.addContent(0, ve.setText("1.2.1"));

		ElementWriter ew = new ElementWriter(root);

		// properties
		ew.writeAttributes(domain.meta());

		// views
		for (View view : domain.getViews())
		{
			new ViewWriter(root).write(view);
		}

		new XmlWriter(new File(_domainRoot, "domain.xml")).write(root);
	}

}
