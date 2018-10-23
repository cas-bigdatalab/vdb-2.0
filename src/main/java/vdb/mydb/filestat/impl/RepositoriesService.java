package vdb.mydb.filestat.impl;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.repo.FileRepository;

public class RepositoriesService
{
	private static final RepositoryDAO repositoryDAO = new RepositoryDAO();

	public List<FileRepository> getAllRepositories(String ds)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		List<FileRepository> ir = repositoryDAO.getRepositoryManager(dataset)
				.getRepositories();
		return ir;
	}

	public List<DataSet> getDataSets()
	{
		List<DataSet> ds = new ArrayList<DataSet>();
		for (DataSet dataSet : VdbManager.getEngine().getDomain().getDataSets())
			ds.add(dataSet);

		return ds;
	}

	public List<FileRepository> getFtpRepositories(String ds)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		List<FileRepository> ir = repositoryDAO.getRepositoryManager(dataset)
				.getRepositories();
		for (int i = 0; i < ir.size(); i++)
		{
			if (!ir.get(i).getType().equalsIgnoreCase("ftp"))
			{
				ir.remove(i);
				i--;
			}
		}
		return ir;
	}

	public List<FileRepository> getWebdavRepositories(String ds)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		List<FileRepository> ir = repositoryDAO.getRepositoryManager(dataset)
				.getRepositories();
		for (int i = 0; i < ir.size(); i++)
		{
			if (!ir.get(i).getType().equalsIgnoreCase("webdav"))
			{
				ir.remove(i);
				i--;
			}
		}
		return ir;
	}

	public List<FileRepository> getApacheRepositories(String ds)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		List<FileRepository> ir = repositoryDAO.getRepositoryManager(dataset)
				.getRepositories();
		for (int i = 0; i < ir.size(); i++)
		{
			if (!ir.get(i).getType().equalsIgnoreCase("apache"))
			{
				ir.remove(i);
				i--;
			}
		}
		return ir;
	}

	public List<FileRepository> getLocalRepositories(String ds)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		List<FileRepository> ir = repositoryDAO.getRepositoryManager(dataset)
				.getRepositories();
		for (int i = 0; i < ir.size(); i++)
		{
			if (!ir.get(i).getType().equalsIgnoreCase("local"))
			{
				ir.remove(i);
				i--;
			}
		}
		return ir;
	}

	public void deleteRepository(String ds, String repName)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		RepositoryManager repositoryManager = repositoryDAO
				.getRepositoryManager(dataset);
		for (int i = 0; i < repositoryManager.getRepositories().size(); i++)
		{
			if (repositoryManager.getRepositories().get(i).getName()
					.equalsIgnoreCase(repName)
					|| repositoryManager.getRepositories().get(i).getType()
							.equalsIgnoreCase("server"))
			{
				repositoryManager.getRepositories().remove(i);
				i--;
			}
		}
		try
		{
			repositoryDAO.saveRepository(dataset, repositoryManager);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addFtpRepository(String ds, String name, String hostname,
			String port, String username, String password, String path)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		RepositoryManager repositoryManager = repositoryDAO
				.getRepositoryManager(dataset);
		FtpRepository fr = new FtpRepository();
		fr.setName(name);
		fr.setHostname(hostname);
		try
		{
			fr.setPort(Integer.parseInt(port));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		fr.setUsername(username);
		fr.setPassword(password);
		fr.setPath(path);
		repositoryManager.getRepositories().add(fr);
		for (int i = 0; i < repositoryManager.getRepositories().size(); i++)
		{
			if (repositoryManager.getRepositories().get(i).getType()
					.equalsIgnoreCase("server"))
			{
				repositoryManager.getRepositories().remove(i);
				i--;
			}
		}
		try
		{
			repositoryDAO.saveRepository(dataset, repositoryManager);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addLocalRepository(String ds, String name, String path)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		RepositoryManager repositoryManager = repositoryDAO
				.getRepositoryManager(dataset);
		LocalRepository lr = new LocalRepository();
		lr.setName(name);
		lr.setPath(path);
		repositoryManager.getRepositories().add(lr);
		for (int i = 0; i < repositoryManager.getRepositories().size(); i++)
		{
			if (repositoryManager.getRepositories().get(i).getType()
					.equalsIgnoreCase("server"))
			{
				repositoryManager.getRepositories().remove(i);
				i--;
			}
		}
		try
		{
			repositoryDAO.saveRepository(dataset, repositoryManager);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addApacheRepository(String ds, String name, String path)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		RepositoryManager repositoryManager = repositoryDAO
				.getRepositoryManager(dataset);
		ApacheRepository ar = new ApacheRepository();
		ar.setName(name);
		ar.setPath(path);
		repositoryManager.getRepositories().add(ar);
		for (int i = 0; i < repositoryManager.getRepositories().size(); i++)
		{
			if (repositoryManager.getRepositories().get(i).getType()
					.equalsIgnoreCase("server"))
			{
				repositoryManager.getRepositories().remove(i);
				i--;
			}
		}
		try
		{
			repositoryDAO.saveRepository(dataset, repositoryManager);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addWebdavRepository(String ds, String name, String path,
			String username, String password)
	{
		DataSet dataset = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(ds);
		RepositoryManager repositoryManager = repositoryDAO
				.getRepositoryManager(dataset);
		WebdavRepository ar = new WebdavRepository();
		ar.setName(name);
		ar.setPath(path);
		ar.setUsername(username);
		ar.setPassword(password);
		repositoryManager.getRepositories().add(ar);
		for (int i = 0; i < repositoryManager.getRepositories().size(); i++)
		{
			if (repositoryManager.getRepositories().get(i).getType()
					.equalsIgnoreCase("server"))
			{
				repositoryManager.getRepositories().remove(i);
				i--;
			}
		}
		try
		{
			repositoryDAO.saveRepository(dataset, repositoryManager);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateLocalRepository(String ds, String name, String path)
	{
		deleteRepository(ds, name);
		addLocalRepository(ds, name, path);
	}

	public void updateApacheRepository(String ds, String name, String path)
	{
		deleteRepository(ds, name);
		addApacheRepository(ds, name, path);
	}

	public void updateFtpRepository(String ds, String name, String hostname,
			String port, String username, String password, String path)
	{
		deleteRepository(ds, name);
		addFtpRepository(ds, name, hostname, port, username, password, path);
	}

	public void updateWebdavRepository(String ds, String name, String path,
			String username, String password)
	{
		deleteRepository(ds, name);
		addWebdavRepository(ds, name, path, username, password);
	}

}
