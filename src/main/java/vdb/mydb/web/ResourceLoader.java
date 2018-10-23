package vdb.mydb.web;

import java.io.InputStream;

public interface ResourceLoader
{
	/**
	 * returns null if resource not found
	 */
	InputStream getResourceStream(String arg0);

}
