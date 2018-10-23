package vdb.mydb.filestat.impl;

import java.io.File;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import vdb.metacat.DataSet;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.util.CatalogUtil;
import vdb.mydb.xmlbeans.BeanWriter;
import vdb.mydb.xmlbeans.BeanWritterFactory;
import vdb.mydb.xmlbeans.SpringBeans;
import vdb.mydb.xmlbeans.SpringBeansWriter;

public class RepositoryDAO
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
						.getBean("vdb.mydb.filestat.impl.RepositoryManager");
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

	public FileRepository loadRepository(DataSet ds, String name)
	{
		RepositoryManager rm = getRepositoryManager(ds);
		if (rm != null)
		{
			for (FileRepository ir : rm.getRepositories())
			{
				if (ir.getName().equalsIgnoreCase(name))
					return ir;
			}
		}

		return null;
	}

	public void saveRepository(DataSet ds, RepositoryManager repositoryManager)
			throws Exception
	{
		File xml = new File(CatalogUtil.getDataSetRoot(ds), "files.xml");

		SpringBeans beans = new SpringBeans();
		beans.addBean(repositoryManager);

		BeanWritterFactory factory = new BeanWritterFactory();
		factory.registerBeanWriter(RepositoryManager.class, new BeanWriter(
				"repositories"));
		factory.registerBeanWriter(LocalRepository.class, new BeanWriter(
				"name", "path"));
		factory.registerBeanWriter(ApacheRepository.class, new BeanWriter(
				"name", "path"));
		factory.registerBeanWriter(WebdavRepository.class, new BeanWriter(
				"name", "path", "username", "password"));
		factory.registerBeanWriter(FtpRepository.class, new BeanWriter("name",
				"hostname", "port", "username", "password", "path"));

		new SpringBeansWriter(xml).write(beans, factory);
	}
}
