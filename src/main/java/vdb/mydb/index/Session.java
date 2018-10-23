package vdb.mydb.index;

public interface Session
{
	public void close();

	public void commit();
}
