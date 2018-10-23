package vdb.mydb.files;

import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Field;

public interface FileManager
{
	FileMetaData createNewFile(DataSet ds) throws Exception;

	void delete(FileMetaData meta) throws Exception;

	FileMetaData getFile(DataSet ds, String id) throws Exception;

	List<FileMetaData> getFiles(DataSet ds, FileOwner owner) throws Exception;

	void grant(FileOwner fileOwner, List<FileMetaData> files) throws Exception;

	void insert(FileMetaData meta) throws Exception;

	void ungrant(List<FileMetaData> files) throws Exception;

	void ungrant(String recordId, Field field) throws Exception;
}