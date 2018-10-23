package vdb.mydb.files.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import vdb.metacat.DataSet;
import vdb.mydb.files.FileMetaData;
import cn.csdb.commons.orm.BeanMapping;
import cn.csdb.commons.orm.Persistor;
import cn.csdb.commons.orm.SimplePersistor;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.DeleteSql;
import cn.csdb.commons.sql.jdbc.sql.InsertSql;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.SelectSql;
import cn.csdb.commons.sql.jdbc.sql.Sql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

class JdbcFileDao
{
	class FileItemMapperRule implements BeanMapping
	{
		public Object createBean() throws Exception
		{
			return new FileMetaDataImpl(_manager);
		}

		public Class getBeanClass()
		{
			return FileMetaDataImpl.class;
		}

		public DataSource getDataSource() throws Exception
		{
			return _jdbcSource.getDataSource();
		}

		public String getPrimaryKey()
		{
			return "ID";
		}

		public String getTableName()
		{
			return VDB_FILES;
		}
	}

	private static final String VDB_FILES = "VDB_FILES";

	private static final String VDB_FILES_REL = "VDB_FILES_REL";

	private JdbcSource _jdbcSource;

	private JdbcFileManager _manager;

	private Persistor _persistor;

	private FileItemMapperRule _rule;

	public JdbcFileDao(DataSet dataSet, JdbcFileManager manager)
			throws Exception
	{
		this(manager.getJdbcSource(dataSet), manager);
	}

	public JdbcFileDao(JdbcSource jdbcSource, JdbcFileManager manager)
			throws Exception
	{
		_jdbcSource = jdbcSource;
		_rule = new FileItemMapperRule();
		_persistor = new SimplePersistor(_rule);
		_manager = manager;
	}

	public int delete(FileMetaData file) throws Exception
	{
		int rows = _persistor.delete(file);
		if (rows > 0)
		{
			unlink(file.getId());
		}

		return rows;
	}

	public FileMetaData getFile(String id) throws Exception
	{
		return (FileMetaData) _persistor.lookup(id);
	}

	public List<FileMetaData> getFiles(QuerySql sql) throws Exception
	{
		return _persistor.createQuery().setSql(sql).list();
	}

	public List<FileMetaData> getFiles(Serializable recordId, String fieldId)
			throws Exception
	{
		SelectSql sql = new SelectSql();
		sql.setField("FILEID").setTableName(VDB_FILES_REL).setFilter(
				new StringSql("RECORDID=? and FIELDID=?", recordId, fieldId))
				.setOrderBy("POSTTIME asc");

		List<FileMetaData> files = new ArrayList<FileMetaData>();
		List<Map<String, Serializable>> ids;
		try
		{
			ids = _jdbcSource.queryForObjects(sql);
			for (Map map : ids)
			{
				files.add((FileMetaDataImpl) _persistor
						.lookup((Serializable) map.get("FILEID")));
			}
		}
		catch (Exception e)
		{
			ids = null;
		}

		return files;
	}

	public boolean hasFiles(String recordId, String fieldId) throws Exception
	{
		SelectSql sql = new SelectSql();
		sql.setField("FILEID").setTableName(VDB_FILES_REL).setFilter(
				new StringSql("RECORDID=? and FIELDID=?", recordId, fieldId));

		return _jdbcSource.existsRecord(sql);
	}

	public int insert(FileMetaData file) throws Exception
	{
		return _persistor.insert(file);
	}

	public void link(Serializable recordId, String fieldId, FileMetaData fi)
			throws Exception
	{
		String fileId = fi.getId();
		Map m = new HashMap();
		m.put("ID", fileId);
		m.put("RECORDID", recordId);
		m.put("FIELDID", fieldId);
		m.put("FILEID", fileId);
		m.put("POSTTIME", new Date());
		Sql sql = new InsertSql(VDB_FILES_REL, m);
		_jdbcSource.executeUpdate(sql);
	}

	public void unlink(Serializable recordId, String fieldId) throws Exception
	{
		Sql sql = new DeleteSql(VDB_FILES_REL, new StringSql(
				"RECORDID=? and FIELDID=?", recordId, fieldId));
		_jdbcSource.executeUpdate(sql);
	}

	public void unlink(String fileId) throws Exception
	{
		Sql sql = new DeleteSql(VDB_FILES_REL,
				new StringSql("FILEID=?", fileId));
		_jdbcSource.executeUpdate(sql);
	}
}
