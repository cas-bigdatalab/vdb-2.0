package vdb.service.httpservice;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

public class HttpServiceVerbManager implements InitializingBean
{
	File _verbsDir;

	private Set<String> _verbs = new HashSet<String>();

	public Set<String> getVerbs()
	{
		return _verbs;
	}

	public void setVerbs(Set<String> verbs)
	{
		this._verbs = verbs;
	}

	public boolean isAvailable(String verb)
	{
		return _verbs.contains(verb);
	}

	public void afterPropertiesSet() throws Exception
	{
		Set<String> defaultVerbs = new HashSet<String>();
		for (String fileName : _verbsDir.list())
		{
			int index = fileName.indexOf(".verb");
			if (index != -1)
			{
				defaultVerbs.add(fileName.substring(0, index));
			}
		}

		setVerbs(defaultVerbs);
	}

	public File getVerbsDir()
	{
		return _verbsDir;
	}

	public void setVerbsDir(File verbsDir)
	{
		_verbsDir = verbsDir;
	}

}
