package vdb.mydb.rpc.service;

import java.io.InputStream;
import java.util.List;

import vdb.mydb.rpc.simplex.SimpleFile;

public interface FileFetchService extends VdbEngineService
{
	InputStream openStream(String dataSetUri, String repositoryName, String path)
			throws Exception;

	SimpleFile getFile(String dataSetUri, String repositoryName, String path);

	List<SimpleFile> getRepositoriesRoot(String dataSetUri);

	public List<SimpleFile> listFiles(String dataSetUri, String repositoryName,
			String path);
}
