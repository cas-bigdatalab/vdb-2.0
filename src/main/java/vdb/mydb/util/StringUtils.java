package vdb.mydb.util;

import java.util.StringTokenizer;

public class StringUtils
{
	/**
	 * ��"FORMAT_WORDS"ת����"formatWords"
	 * 
	 * @param words
	 * @return
	 */
	public static String formatWords(String words)
	{
		// ����_
		StringTokenizer st = new StringTokenizer(words, "_");
		int i = 0;
		String ws = "";
		while (st.hasMoreTokens())
		{
			String s = st.nextToken().toLowerCase();
			if (i > 0)
			{
				// ����ĸ��д
				ws += Character.toUpperCase(s.charAt(0));
			}
			else
			{
				ws += s.charAt(0);
			}

			ws += s.substring(1);

			i++;
		}

		return ws;
	}

	/**
	 * ��"FORMAT_WORDS"ת����"Format Words"
	 * 
	 * @param words
	 * @return
	 */
	public static String formatWords2(String words)
	{
		// ����_
		StringTokenizer st = new StringTokenizer(words, "_");
		int i = 0;
		String ws = "";
		while (st.hasMoreTokens())
		{
			String s = st.nextToken().toLowerCase();
			if (i > 0)
			{
				ws += " ";
			}

			// ����ĸ��д
			ws += Character.toUpperCase(s.charAt(0));
			ws += s.substring(1);

			i++;
		}

		return ws;
	}
}
