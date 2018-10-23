package vdb.mydb.typelib;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;

/*
 * a VdbData is data created by a VdbType binding with certain value
 * 
 * 
 * @author bluejoe
 */
public interface VdbData
{
	public String getValue();

	public String getTitle();

	public Sdef getAsSdef();

	public void setAsSdef(Sdef ddl) throws SdefException;

	public void setAsText(String text);

	public String getAsText();

	public long getBytes();

	public boolean isEmpty() throws Exception;

}
