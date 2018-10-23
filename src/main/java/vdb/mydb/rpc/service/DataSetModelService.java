package vdb.mydb.rpc.service;

import java.util.List;

import vdb.mydb.rpc.simplex.SimpleDataSet;
import vdb.mydb.rpc.simplex.SimpleEntity;
import vdb.mydb.rpc.simplex.SimpleField;

public interface DataSetModelService extends VdbEngineService
{
	List<SimpleDataSet> getDataSets();

	List<SimpleEntity> getEntities(String dataSetUri);

	SimpleDataSet getDataSet(String uri);

	SimpleEntity getEntity(String uri);

	SimpleField getField(String uri);

	List<SimpleField> getFields(String entityUri);
}
