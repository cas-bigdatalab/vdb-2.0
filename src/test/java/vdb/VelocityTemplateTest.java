package vdb;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

public class VelocityTemplateTest
{
	public static void main(String[] args)
	{
		Velocity.setProperty("file.resource.loader.path", ".");
		String templateName = "test.vm";
		long start = System.currentTimeMillis();
		Template t;
		try
		{
			t = Velocity.getTemplate(templateName);
			long end = System.currentTimeMillis();

			System.out.println("=====loading " + t.getName() + ": "
					+ (end - start) + "ms");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
