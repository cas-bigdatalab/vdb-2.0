package vdb.mydb.web;

public interface PageLocator
{
	/**
	 * get real path of given url
	 * 
	 * @param path
	 * @return
	 */
	public String getRealPath(String url);
}
