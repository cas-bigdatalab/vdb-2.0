package vdb.mydb.index.db4o;

import java.io.File;

import org.apache.velocity.context.Context;

import vdb.mydb.VdbManager;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.Session;
import vdb.mydb.index.VdbIndexer;

import com.db4o.Db4o;
import com.db4o.config.Configuration;

public class Db4oIndexer implements VdbIndexer
{
	private Configuration _db4oConf;

	private File _db4oFile;

	private String _indexFilePath;

	public Db4oIndexer()
	{
		_db4oConf = Db4o.newConfiguration();
		_db4oConf.lockDatabaseFile(false);
	}

	public File getDb4oFile()
	{
		return _db4oFile;
	}

	public String getIndexFilePath()
	{
		return _indexFilePath;
	}

	public IndexSearcher getSearcher() throws Exception
	{
		return new Db4oIndexSearcher(this);
	}

	public IndexWriter getWriter() throws Exception
	{
		return new Db4oIndexWriter(this);
	}

	public Session openSession() throws Exception
	{
		return new Db4oSession(Db4o.openFile(_db4oConf, getDb4oFile()
				.getCanonicalPath()));
	}

	public void setIndexFilePath(String indexFilePath) throws Exception
	{
		_indexFilePath = indexFilePath;
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		_db4oFile = new File(VdbManager.getInstance().getVelocityEngine()
				.evaluate(_indexFilePath, ctx));
	}
}
