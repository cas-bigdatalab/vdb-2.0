package vdb.metacat.io;

import vdb.metacat.DataSet;

public interface SchemaFactory
{
	DataSet getDataSet(String dataSetUri);
}
