package vdb.mydb.index.util;

import vdb.metacat.Field;
import vdb.mydb.bean.AnyBean;

public class IndexUtils
{
	public static String getFullText(AnyBean bean, Field[] fields)
	{
		String text = "";
		StringBuffer buf = new StringBuffer();

		for (Field field : fields)
		{
			try
			{
				String pt = bean.get(field).getTitle();
				if (pt != null)
					buf.append(pt).append(" ");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		text = buf.toString();
		return text;
	}
}
