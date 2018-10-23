package vdb.mydb.filestat.tool;

import java.io.File;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.filestat.impl.RepositoryManager;
import vdb.mydb.filestat.impl.ServerRepository;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import vdb.mydb.util.CatalogUtil;

public class FilesTool
{
	public RepositoryManager getRepositoryManager(DataSet ds)
	{
		File xml = new File(CatalogUtil.getDataSetRoot(ds), "files.xml");
		if (xml.exists())
		{
			try
			{
				XmlBeanFactory factory = new XmlBeanFactory(
						new FileSystemResource(xml));
				RepositoryManager rm = (RepositoryManager) factory
						.getBean(factory
								.getBeanNamesForType(RepositoryManager.class)[0]);
				rm.addRepository(new ServerRepository(ds));
				return rm;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			RepositoryManager rm = new RepositoryManager();
			rm.addRepository(new ServerRepository(ds));
			return rm;
		}
		return null;
	}

	public FileRepository getRepository(String uri, String name)
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);
		return getRepository(ds, name);
	}

	public FileRepository getRepository(DataSet ds, String name)
	{
		RepositoryManager rm = getRepositoryManager(ds);
		if (rm != null && rm.getRepositories() != null)
		{
			for (FileRepository ir : rm.getRepositories())
			{
				ir.setDataSet(ds);
				if (ir.getName().equalsIgnoreCase(name))
					return ir;
			}
		}
		return null;
	}

	public RepositoryFile getRootFile(DataSet ds, String repositoryName)
	{
		FileRepository ir = getRepository(ds.getUri(), repositoryName);
		return ir.getRoot();
	}

	public RepositoryFile getRootFile(String uri, String repositoryName)
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);
		FileRepository ir = getRepository(ds.getUri(), repositoryName);
		return ir.getRoot();
	}
}
