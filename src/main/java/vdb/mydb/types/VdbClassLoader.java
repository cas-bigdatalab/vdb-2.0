package vdb.mydb.types;

import java.net.URL;
import java.net.URLClassLoader;

public class VdbClassLoader extends URLClassLoader
{
	public VdbClassLoader(ClassLoader arg1)
	{
		super(new URL[0], arg1);
	}

	@Override
	protected void addURL(URL arg0)
	{
		super.addURL(arg0);
	}
}
