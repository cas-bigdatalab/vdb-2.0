package vdb.mydb.typelib;

import java.io.Serializable;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class JsonUtil
{
	public static Serializable fromJsonString(String jsonString)
	{
		jsonString = "[" + jsonString + "]";
		JSONArray ja = JSONArray.fromObject(jsonString);
		return toJavaObject(ja.get(0));
	}

	public static Serializable toJavaObject(Object json)
	{
		if (json instanceof JSON)
		{
			return (Serializable) JSONSerializer.toJava((JSON) json);
		}

		return (Serializable) json;
	}

	public static String toJsonString(Serializable object)
	{
		Object os[] = { object };
		String js = JSONArray.fromObject(os).toString();
		int len = js.length();
		return js.substring(1, len - 1);
	}
}
