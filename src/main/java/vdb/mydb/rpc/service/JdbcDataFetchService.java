package vdb.mydb.rpc.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface JdbcDataFetchService extends VdbEngineService
{
	int countRecords(String entityUri) throws Exception;

	List<Map<String, Serializable>> listRecords(String entityUri, int start,
			int end) throws Exception;
}
