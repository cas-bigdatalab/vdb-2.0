package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.List;

import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.FileOwner;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbFiles;

public class VdbFilesType extends AbstractFieldDriver<VdbFiles>
{
	public VdbFiles createData() throws Exception
	{
		VdbFiles vd = new VdbFiles(_field.getEntity().getDataSet());
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbFiles data)
			throws Exception
	{
		FileManager manager = VdbManager.getInstance().getFileManager();

		// ����¼�����
		Serializable pk = jdbcRowWriter.getId();
		List<FileMetaData> files = ((VdbFiles) data).getFiles();
		// insert new files
		FileOwner owner = new FileOwner("" + pk, _field);
		manager.grant(owner, files);
	}

	public VdbFiles jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbFiles files = createData();
		FileManager manager = VdbManager.getInstance().getFileManager();
		Serializable pk = jdbcRowReader.getId();
		files.setFiles(manager.getFiles(_field.getEntity().getDataSet(),
				new FileOwner("" + pk, _field)));
		return files;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbFiles data,
			VdbFiles dataOutOfDate) throws Exception
	{
		// delete old files
		FileManager manager = VdbManager.getInstance().getFileManager();
		Serializable pk = jdbcRowWriter.getId();
		manager.ungrant("" + pk, _field);

		// insert new files
		manager.grant(new FileOwner("" + pk, _field), ((VdbFiles) data)
				.getFiles());
	}

	public void jdbcDelete(JdbcRowWriter jdbcRowWriter, VdbFiles data)
			throws Exception
	{
		Serializable beanId = jdbcRowWriter.getBean().getId().getValue();
		FileManager manager = VdbManager.getInstance().getFileManager();
		FileOwner owner = new FileOwner(beanId, _field);
		List<FileMetaData> files = manager.getFiles(_field.getEntity()
				.getDataSet(), owner);

		for (FileMetaData fi : files)
		{
			manager.delete(fi);
		}

		manager.ungrant(files);
	}
}