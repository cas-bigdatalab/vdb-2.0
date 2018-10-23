package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.List;

import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.files.FileOwner;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbLocalFile;

public class VdbLocalFileType extends AbstractFieldDriver<VdbLocalFile>
{
	public VdbLocalFile createData() throws Exception
	{
		VdbLocalFile vd = new VdbLocalFile(_field.getEntity().getDataSet());
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbLocalFile data)
			throws Exception
	{

		FileManager manager = VdbManager.getInstance().getFileManager();

		// ����¼�����
		Serializable pk = jdbcRowWriter.getId();
		List<FileMetaData> files = ((VdbLocalFile) data).getFiles();
		// insert new files
		FileOwner owner = new FileOwner("" + pk, _field);
		manager.grant(owner, files);
	}

	public VdbLocalFile jdbcSelect(JdbcRowReader jdbcRowReader)
			throws Exception
	{
		VdbLocalFile files = createData();
		FileManager manager = VdbManager.getInstance().getFileManager();
		Serializable pk = jdbcRowReader.getId();
		files.setFiles(manager.getFiles(_field.getEntity().getDataSet(),
				new FileOwner("" + pk, _field)));
		return files;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbLocalFile data,
			VdbLocalFile dataOutOfDate) throws Exception
	{
		// delete old files
		FileManager manager = VdbManager.getInstance().getFileManager();
		Serializable pk = jdbcRowWriter.getId();
		manager.ungrant("" + pk, _field);

		// insert new files
		manager.grant(new FileOwner("" + pk, _field), ((VdbLocalFile) data)
				.getFiles());
	}

	public void jdbcDelete(JdbcRowWriter jdbcRowWriter, VdbLocalFile data)
			throws Exception
	{
		// Serializable beanId = jdbcRowWriter.getId();
		// FileManager manager = VdbManager.getInstance().getFileManager();
		// FileOwner owner = new FileOwner(beanId, _field);
		// List<FileMetaData> files = manager.getFiles(_field.getEntity()
		// .getDataSet(), owner);
		//
		// for (FileMetaData fi : files)
		// {
		// manager.delete(fi);
		// }
		//
		// manager.ungrant(files);
	}
}