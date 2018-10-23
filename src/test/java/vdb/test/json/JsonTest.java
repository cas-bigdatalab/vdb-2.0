package vdb.test.json;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

public class JsonTest
{

	@Test
	public void testJson()
	{
		Course c = new Course();
		c.setCourseid(1);
		c.setCoursename("语言");

		Student s = new Student();
		s.setId(203);
		s.setGender(false);
		s.setName("张三");
		s.setCourse(c);

		String expected = "{\"course\":{\"courseid\":1,\"coursename\":\"语言\"},"
				+ "\"gender\":false,\"id\":203,\"name\":\"张三\",\"test\":\"\"}";
		Assert.assertEquals(expected, JSONObject.fromObject(s).toString());
	}
}
