package vdb.mydb.rpc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import vdb.mydb.rpc.simplex.SimpleAnyBean;
import vdb.mydb.rpc.simplex.SimpleDataSet;
import vdb.mydb.rpc.simplex.SimpleEntity;
import vdb.mydb.rpc.simplex.SimpleField;
import vdb.mydb.rpc.simplex.SimpleFile;

/**
 * @deprecated
 * @author bluejoe
 * 
 */
public interface DataFetchService
{
	SimpleAnyBean getBean(String entityUri, Serializable id) throws Exception;

	int countRecords(String entityUri) throws Exception;

	List<Map<String, Serializable>> listRecords(String entityUri, int start,
			int end) throws Exception;

	SimpleFile getFile(String dataSetUri, String repositoryName, String path);

	List<SimpleFile> getRepositoriesRoot(String dataSetUri);

	public List<SimpleFile> listFiles(String dataSetUri, String repositoryName,
			String path);

	List<SimpleDataSet> getDataSets();

	List<SimpleEntity> getEntities(String dataSetUri);

	SimpleDataSet getDataSet(String uri);

	SimpleEntity getEntity(String uri);

	public SimpleField getField(String uri);

	List<SimpleField> getFields(String entityUri);
}
