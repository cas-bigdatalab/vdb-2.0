package vdb.mydb.typelib;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

public class JsonUtilTest extends TestCase
{
	public void test1()
	{
		JSON j = JSONSerializer.toJSON(new int[][] { new int[] { 1 } });
		Object o = (Serializable) JSONSerializer.toJava(j);
		return;
	}

	/*
	 * Test method for 'vdb.mydb.typelib.JsonUtil.fromJsonString(String)'
	 */
	public void testFromJsonString()
	{

	}

	/*
	 * Test method for 'vdb.mydb.typelib.JsonUtil.getJsonString(Object)'
	 */
	public void testGetJsonString()
	{
		System.out.println(JsonUtil.toJsonString("abc"));
		System.out.println(JsonUtil.toJsonString(12));
		System.out.println(JsonUtil.toJsonString(12.4));
		System.out.println(JsonUtil.toJsonString(true));
		System.out
				.println(JsonUtil.toJsonString(new String[] { "aaa", "bbb" }));

		Map m = new HashMap();
		m.put("a", "text");
		m.put("b", 2);
		System.out.println(JsonUtil.toJsonString((Serializable) m));
	}

}
