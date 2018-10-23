package vdb.mydb.typelib.data;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SdefNode;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbLocalFile extends AbstractData
{
	DataSet _dataSet;

	List<FileMetaData> _files = new ArrayList<FileMetaData>();

	public VdbLocalFile(DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void addFile(FileMetaData fi)
	{
		_files.add(fi);
	}

	public List<FileMetaData> getFiles() throws Exception
	{
		return _files;
	}

	public boolean isEmpty()
	{
		return _files != null && _files.isEmpty();
	}

	public void removeAll()
	{
		_files.clear();
	}

	public void setFiles(List<FileMetaData> files)
	{
		_files = files;
	}

	public Sdef getAsSdef()
	{
		SimpleSdef sd = new SimpleSdef();
		SdefNode value = sd.addChild("value");
		SdefNode title = sd.addChild("title");
		SdefNode files = value.addChild("files");

		StringBuilder sb = new StringBuilder();
		try
		{
			List<FileMetaData> list = getFiles();
			for (FileMetaData fm : list)
			{
				SdefNode file = files.addChild("file");
				file.setNodeValue("" + fm.getId());
				sb.append(fm.getTitle() + ", ");
			}
			String s = sb.toString();
			title.setNodeValue(s.substring(0, s.lastIndexOf(',')));
		}
		catch (Exception e)
		{
		}
		return sd;
	}

	/**
	 * 
	 */
	public void setAsSdef(Sdef ddl) throws SdefException
	{
		FileManager manager = VdbManager.getInstance().getFileManager();
		removeAll();

		for (SdefNode node : ddl.selectNodes("files/file/id"))
		{
			try
			{
				addFile(manager.getFile(_dataSet, node.getNodeValue()));
			}
			catch (Exception e)
			{
				// e.printStackTrace();
			}
		}
	}

	public String getAsText()
	{
		return getValue();
	}

	public void setAsText(String text)
	{
	}

	/**
	 * 文件类型的数据量为0
	 */
	public long getBytes() {
		return 0;
	}
}