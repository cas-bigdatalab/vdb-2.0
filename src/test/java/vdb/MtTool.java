package vdb;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class MtTool extends TestCase
{
	private Object evaluate(String expr) throws ParseErrorException,
			MethodInvocationException, ResourceNotFoundException, IOException
	{
		Context context = new VelocityContext();
		Writer sw = new StringWriter();
		String vtl = "#set ($__var__=" + expr + ")";
		Velocity.evaluate(context, sw, "", vtl);
		return context.get("__var__");
	}

	private Object evaluate(Context context, String expr)
			throws ParseErrorException, MethodInvocationException,
			ResourceNotFoundException, IOException
	{
		Writer sw = new StringWriter();
		String vtl = "#set ($__var__=" + expr + ")";
		Velocity.evaluate(context, sw, "", vtl);
		return context.get("__var__");
	}

	public void test2() throws ParseErrorException, MethodInvocationException,
			ResourceNotFoundException, IOException
	{
		Context context = new VelocityContext();
		context.put("Alexander", 100);
		assertEquals(140, evaluate(context, "$Alexander+200/5"));
	}

	public void test1() throws ParseErrorException, MethodInvocationException,
			ResourceNotFoundException, IOException
	{
		assertEquals(140, evaluate("100+200/5"));
	}
}
