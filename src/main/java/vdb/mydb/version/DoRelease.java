package vdb.mydb.version;

import java.io.File;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import vdb.mydb.util.VdbApplicationContextUtil;

public class DoRelease
{
	public static void main(String[] args) throws Exception
	{
		if (args.length != 2)
		{
			System.err.println("usage: release <sourcedir> <revision number>");
			return;
		}

		File sourceDir = new File(args[0]);
		if (!sourceDir.exists())
		{
			System.err.println(String.format("file not found: %s", args[0]));
			return;
		}

		WebApplicationContext applicationContext = VdbApplicationContextUtil
				.createWebApplicationContext(
						new MockServletContext(sourceDir.getCanonicalPath(),
								new FileSystemResourceLoader()),
						null,
						"WEB-INF/conf/ioc-start.xml,WEB-INF/conf/ioc/ioc-config.xml,WEB-INF/conf/ioc/ioc-version.xml");

		ReleaseVersionBuilder releaseVersionBuilder = (ReleaseVersionBuilder) applicationContext
				.getBean("releaseVersionBuilder");
		releaseVersionBuilder.setSourceDir(sourceDir);
		Long revision = Long.parseLong(args[1]);

		releaseVersionBuilder.build(revision);
		System.out.println(String.format("success to build release 2.0.%d",
				revision));
	}
}
