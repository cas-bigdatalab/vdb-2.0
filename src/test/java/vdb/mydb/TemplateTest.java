package vdb.mydb;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class TemplateTest extends TestCase
{
	public void _test1()
	{
		VelocityContext vc = new VelocityContext();
		StringWriter sw = new StringWriter();
		String template = "${a + b} bytes";
		try
		{
			vc.put("a", 1);
			vc.put("b", 2);
			Velocity.evaluate(vc, sw, "", template);
			System.out.print(sw.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void _test2()
	{
		VelocityContext vc = new VelocityContext();
		StringWriter sw = new StringWriter();
		String template = "$a * 200 + $b";
		try
		{
			vc.put("a", 1);
			vc.put("b", 2);
			template = "#set ($c=" + template + ")";
			Velocity.evaluate(vc, sw, "", template);
			Object o = vc.get("c");
			System.out.print(o.getClass());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void _testLoadTemplate(String path)
			throws ResourceNotFoundException, ParseErrorException, Exception
	{
		long start = System.currentTimeMillis();
		Template t = Velocity.getTemplate(path);
		long end = System.currentTimeMillis();

		System.out.println("=====loading " + t.getName() + ": " + (end - start)
				+ "ms");
	}

	public void test0() throws ResourceNotFoundException, ParseErrorException,
			Exception
	{
		Velocity.setProperty("file.resource.loader.path",
				"D:\\workspace\\vdb-1.2\\web\\views");
		_testLoadTemplate("cn.csdb.paperdb.book_listEdit.html");
	}
}
