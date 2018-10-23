package vdb.mydb.rpc.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBeanImpl;
import vdb.mydb.filestat.impl.RepositoryManager;
import vdb.mydb.filestat.tool.FilesTool;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import vdb.mydb.rpc.simplex.SimpleAnyBean;
import vdb.mydb.rpc.simplex.SimpleAnyBeanImpl;
import vdb.mydb.rpc.simplex.SimpleDataSet;
import vdb.mydb.rpc.simplex.SimpleDataSetImpl;
import vdb.mydb.rpc.simplex.SimpleEntity;
import vdb.mydb.rpc.simplex.SimpleEntityImpl;
import vdb.mydb.rpc.simplex.SimpleField;
import vdb.mydb.rpc.simplex.SimpleFieldImpl;
import vdb.mydb.rpc.simplex.SimpleFile;
import vdb.mydb.rpc.simplex.SimpleFileImpl;
import vdb.mydb.vtl.toolbox.VdbTool;
import vdb.tool.da.DataAccessTool;
import vdb.tool.generic.FormatTool;

/**
 * @deprecated
 * @author bluejoe
 * 
 */
public class DataFetchServiceImpl implements DataFetchService,
		FileFetchService, JdbcDataFetchService, BeanFetchService,
		DataSetModelService
{

	public int countRecords(String entityUrl) throws Exception
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		VdbEntity entity = catalog.fromUri(entityUrl);
		int count = new DataAccessTool().recordsSize(entity.getDataSet()
				.getUri(), "select * from " + entity.getTableName());
		return count;
	}

	public List<Map<String, Serializable>> listRecords(String entityUrl,
			int start, int size) throws Exception
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		VdbEntity entity = catalog.fromUri(entityUrl);
		// FIXME: 为什么自己拼SQL？表名是关键字咋办？
		List<Map<String, Serializable>> list = new DataAccessTool().execute(
				entity.getDataSet().getUri(), "select * from "
						+ entity.getTableName(), start, size);
		return list;
	}

	public SimpleAnyBean getBean(String entityUrl, Serializable id)
			throws Exception
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		VdbEntity entity = catalog.fromUri(entityUrl);

		Map<String, Object> map = new HashMap<String, Object>();
		AnyBeanImpl bean = (AnyBeanImpl) new VdbTool().getBean(entity, id);
		Field[] ef = entity.getFields();

		for (int j = 0; j < ef.length; j++)
		{
			map.put(ef[j].getUri().substring(
					ef[j].getEntity().getUri().length() + 1), bean.get(ef[j])
					.getValue());
		}

		SimpleAnyBeanImpl sabi = new SimpleAnyBeanImpl();
		sabi.setBean(map);
		return sabi;
	}

	public List<SimpleDataSet> getDataSets()
	{
		DataSet[] ds = VdbManager.getEngine().getDomain().getDataSets();

		List<SimpleDataSet> list = new ArrayList<SimpleDataSet>();
		for (DataSet d : ds)
		{
			VdbDataSet vds = (VdbDataSet) d;

			SimpleDataSet sds = new SimpleDataSetImpl();
			sds.setDescription(d.getDescription());
			sds.setId(d.getId());
			sds.setName(d.getUri());
			sds.setUri(d.getUri());

			if (vds.getJdbcSource() == null)
				sds.setConnected(false);
			else
				sds.setConnected(true);

			sds.setLastModified(vds.getLastModified());
			sds.setTitle(vds.meta().get("title"));
			sds.setDataBaseType(vds.getRepository().getProductName());

			list.add(sds);
		}
		return list;
	}

	public List<SimpleEntity> getEntities(String dsuri)
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dsuri);
		Entity[] entities = ds.getEntities();

		List<SimpleEntity> list = new ArrayList<SimpleEntity>();
		for (Entity d : entities)
		{
			SimpleEntity se = new SimpleEntityImpl();
			se.setDescription(d.getDescription());
			se.setId(d.getId());
			se.setName(d.get("name"));
			se.setUri(d.getUri());
			se.setIdentifier(d.getIdentifier().getField().getColumnName());
			se.setTitle(d.getTitle());
			se.setTableName(d.getTableName());
			se.setTitleField(d.getTitleField().getName());
			list.add(se);
		}

		return list;
	}

	public SimpleFile getFile(String dsuri, String repositoryName, String path)
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dsuri);
		FileRepository ir = new FilesTool().getRepository(ds, repositoryName);
		RepositoryFile f = ir.getFile(path);

		SimpleFile file = new SimpleFileImpl();
		file.setDataSetUri(dsuri);
		file.setFileContentType(f.getContentType());
		file.setFileExtension(f.getFileExtension());
		file.setFileLastModified(new FormatTool().formatDate(f
				.getLastModified()));
		file.setFileLength(String.valueOf(f.length()));
		file.setFileName(f.getFileName());
		file.setFilePath(f.getFilePath().replace("\\", "/"));
		file.setFileTitle(f.getFileTitle());
		file.setRepositoryName(f.getRepository().getName());

		return file;
	}

	public List<SimpleFile> listFiles(String dsuri, String repositoryName,
			String path)
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dsuri);
		FileRepository ir = new FilesTool().getRepository(ds, repositoryName);
		List<RepositoryFile> l = ir.getFile(path).listFiles();
		List<SimpleFile> listFiles = new ArrayList<SimpleFile>();
		for (RepositoryFile f : l)
		{
			if (f.getFileName().startsWith("."))
				continue;
			SimpleFile file = new SimpleFileImpl();

			file.setFileLastModified(new FormatTool().formatDate(f
					.getLastModified()));
			file.setFileLength(String.valueOf(f.length()));
			file.setFileName(f.getFileName());
			String filePath = f.getFilePath().replace("\\", "/");
			if (filePath.startsWith("/"))
				filePath = filePath.substring(1);
			file.setFilePath(filePath);
			file.setFileTitle(f.getFileTitle());
			file.setDirectory(f.isDirectory());
			file.setRepositoryName(f.getRepository().getName());
			listFiles.add(file);
		}

		return listFiles;
	}

	public List<SimpleFile> getRepositoriesRoot(String dsuri)
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dsuri);

		List<SimpleFile> listFiles = new ArrayList<SimpleFile>();
		RepositoryManager rm = new FilesTool().getRepositoryManager(ds);
		List<FileRepository> list = rm.getRepositories();

		for (int i = 0; i < list.size(); i++)
		{
			RepositoryFile f = new FilesTool().getRootFile(ds.getUri(), list
					.get(i).getName());

			SimpleFile file = new SimpleFileImpl();
			file.setFileContentType(f.getContentType());
			file.setFileExtension(f.getFileExtension());
			file.setFileLastModified(new FormatTool().formatDate(f
					.getLastModified()));
			file.setFileLength(String.valueOf(f.length()));
			file.setFileName(f.getFileName());
			file.setFilePath("/");
			file.setFileTitle(f.getFileTitle());
			file.setDirectory(f.isDirectory());
			file.setRepositoryName(f.getRepository().getName());
			listFiles.add(file);
		}

		return listFiles;
	}

	/**
	 * @deprecated use listFiles()
	 */
	public List<SimpleFile> getDirectoryFiles(String dsuri,
			String repositoryName, String path)
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dsuri);
		FileRepository ir = new FilesTool().getRepository(ds, repositoryName);
		List<RepositoryFile> l = ir.getFile(path).listFiles();
		List<SimpleFile> listFiles = new ArrayList<SimpleFile>();
		for (RepositoryFile f : l)
		{
			if (f.getFileName().startsWith("."))
				continue;
			SimpleFile file = new SimpleFileImpl();

			file.setFileLastModified(new FormatTool().formatDate(f
					.getLastModified()));
			file.setFileLength(String.valueOf(f.length()));
			file.setFileName(f.getFileName());
			String filePath = f.getFilePath().replace("\\", "/");
			if (filePath.startsWith("/"))
				filePath = filePath.substring(1);
			file.setFilePath(filePath);
			file.setFileTitle(f.getFileTitle());
			file.setDirectory(f.isDirectory());
			file.setRepositoryName(f.getRepository().getName());
			listFiles.add(file);
		}

		return listFiles;
	}

	public SimpleDataSet getDataSet(String uri)
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		VdbDataSet d = catalog.fromUri(uri);

		SimpleDataSet sds = new SimpleDataSetImpl();
		sds.setDescription(d.getDescription());
		sds.setId(d.getId());
		sds.setName(d.getUri());
		sds.setUri(d.getUri());

		if (d.getJdbcSource() == null)
			sds.setConnected(false);
		else
			sds.setConnected(true);

		sds.setLastModified(d.getLastModified());
		sds.setTitle(d.meta().get("title"));
		sds.setDataBaseType(d.getRepository().getProductName());

		return sds;
	}

	public SimpleEntity getEntity(String uri)
	{

		Catalog catalog = VdbManager.getEngine().getCatalog();
		Entity d = catalog.fromUri(uri);
		SimpleEntity se = new SimpleEntityImpl();
		se.setDescription(d.getDescription());
		se.setId(d.getId());
		se.setName(d.get("name"));
		se.setUri(d.getUri());
		se.setIdentifier(d.getIdentifier().getField().getColumnName());
		se.setTitle(d.getTitle());
		se.setTableName(d.getTableName());
		se.setTitleField(d.getTitleField().getName().toLowerCase());
		se.setDataSetName(d.getDataSet().getUri());
		return se;
	}

	public SimpleField getField(String uri)
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Field d = catalog.fromUri(uri);
		SimpleField se = new SimpleFieldImpl();

		se.setId(d.getId());
		se.setName(d.get("name"));
		se.setUri(d.getUri());
		se.setTitle(d.getTitle());
		se.setType(d.getTypeName());
		se.setEntityName(d.getEntity().getName());
		return se;
	}

	public List<SimpleField> getFields(String entityUri)
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Entity e = catalog.fromUri(entityUri);
		Field[] fields = e.getFields();

		List<SimpleField> list = new ArrayList<SimpleField>();
		for (Field d : fields)
		{
			SimpleField se = new SimpleFieldImpl();
			se.setId(d.getId());
			se.setName(d.get("name"));
			se.setUri(d.getUri());
			se.setTitle(d.getTitle());
			se.setType(d.getTypeName());
			se.setEntityName(d.getEntity().getName());
			list.add(se);
		}

		return list;
	}

	public InputStream openStream(String dataSetUri, String repositoryName,
			String path) throws Exception
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet ds = catalog.fromUri(dataSetUri);
		FileRepository ir = new FilesTool().getRepository(ds, repositoryName);
		return ir.openStream(path);
	}
}
