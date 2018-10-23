package vdb.mydb.files.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.FileOwner;
import vdb.mydb.jdbc.JdbcSourceManager;
import cn.csdb.commons.sql.JdbcSource;

public class JdbcFileManager implements FileManager, Serializable
{
	public FileMetaData createNewFile(DataSet ds)
	{
		FileMetaDataImpl file = new FileMetaDataImpl(this);
		file.setDataSet(ds);
		return file;
	}

	public void delete(FileMetaData meta) throws Exception
	{
		// delete file first
		String relativeFilePath = ((FileMetaDataImpl) meta).getFilePath();
		File file = new File(new File(VdbManager.getInstance().getDataSetRoot(
				meta.getDataSet()), "files"), relativeFilePath);
		file.delete();

		new JdbcFileDao(meta.getDataSet(), this).delete(meta);
	}

	public FileMetaData getFile(DataSet ds, String id) throws Exception
	{
		return new JdbcFileDao(ds, this).getFile(id);
	}

	public List<FileMetaData> getFiles(DataSet ds, FileOwner owner)
			throws Exception
	{
		return new JdbcFileDao(ds, this).getFiles(owner.getRecordId(), owner
				.getField().getId());
	}

	public File getIoFile(DataSet ds, String relativeFilePath)
	{
		return new File(new File(VdbManager.getInstance().getDataSetRoot(ds),
				"files"), relativeFilePath);
	}

	public JdbcSource getJdbcSource(DataSet dataSet) throws Exception
	{
		return JdbcSourceManager.getInstance().getFilesJdbcSource(dataSet);
	}

	public void grant(FileOwner fileOwner, List<FileMetaData> files)
			throws Exception
	{
		for (FileMetaData file : files)
		{
			new JdbcFileDao(file.getDataSet(), this).link(fileOwner
					.getRecordId(), fileOwner.getField().getId(), file);
		}
	}

	public void insert(FileMetaData meta) throws Exception
	{
		// save file first
		// String relativeFilePath = MessageFormat.format("{0,date,yyyy-MM}",
		// new Date());
		// File filesDir = new File(new File(VdbManager.getInstance()
		// .getDataSetRoot(meta.getDataSet()), "files"), relativeFilePath);
		// filesDir.mkdirs();
		//
		// relativeFilePath += "/" + meta.getId();
		// File newFile = new File(filesDir, meta.getId());
		// FileOutputStream fos = new FileOutputStream(newFile);
		// InputStream fis = meta.getFileStream();
		// while (true)
		// {
		// byte[] bs = new byte[2408];
		// int n = fis.read(bs);
		// if (n < 0)
		// break;
		// fos.write(bs, 0, n);
		// }
		//
		// fis.close();
		// ((FileMetaDataImpl) meta).setFilePath(relativeFilePath);
		new JdbcFileDao(meta.getDataSet(), this).insert(meta);
	}

	public void ungrant(List<FileMetaData> files) throws Exception
	{
		for (FileMetaData file : files)
		{
			new JdbcFileDao(file.getDataSet(), this).unlink(file.getId());
		}
	}

	public void ungrant(String recordId, Field field) throws Exception
	{
		new JdbcFileDao(field.getEntity().getDataSet(), this).unlink(recordId,
				field.getId());
	}
}
