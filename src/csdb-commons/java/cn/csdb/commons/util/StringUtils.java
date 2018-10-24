package cn.csdb.commons.util;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;

/*
 * @author bluejoe
 */
public class StringUtils
{
	private static Pattern _parameterNamePattern = Pattern.compile("@([\\w]+)");

	private static short counter = (short) 0;

	private static final char[] HEX_TABLE = new char[] { '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final int IP;

	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private static String SEPARATOR = "";

	static
	{
		int ipadd;
		try
		{
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		}
		catch (Exception e)
		{
			ipadd = 0;
		}
		IP = ipadd;
	}

	/**
	 * ���ַ���Ϣ����HTML����
	 * 
	 * @param source
	 *            Դ��Ϣ
	 * @param encodeBlank
	 *            �Ƿ�ͬʱ�Իس��Ϳո���б���
	 * @return HTML����
	 */
	static public String encodeXml(String source, boolean encodeBlank)
	{
		if (source == null)
			return "";

		String result = new String(source);
		result = result.replaceAll("&", "&amp;");
		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");
		result = result.replaceAll("\"", "&quot;");

		if (encodeBlank)
		{
			result = result.replaceAll("\t", "    ");
			result = result.replaceAll("  ", "&nbsp; ");
			result = result.replaceAll("\r\n", "<br>");
			result = result.replaceAll("\n", "<br>");
		}
		return result;
	}

	/**
	 * ת��һ�δ��룬�罫�س������"\r\n"�ַ�
	 * 
	 * @param value
	 * @return
	 */
	public static String encodeScript(String value)
	{
		if (value == null)
			return null;

		value = value.replaceAll("\\\\", "\\\\\\\\");
		value = value.replaceAll("\"", "\\\\\"");
		value = value.replaceAll("\\t", "\\\\t");
		value = value.replaceAll("\\r", "\\\\r");
		value = value.replaceAll("\\n", "\\\\n");

		return value;
	}

	/**
	 * ת��URL�е������ַ�
	 * 
	 * @param source
	 * @return
	 */
	static public String encodeUrl(String source)
	{
		byte[] bytes = source.getBytes();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			byte c = bytes[i];
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z'))
				result.append((char) c);
			else
			{
				result.append('%');
				result.append(HEX_TABLE[(c >> 4) & 0x0f]);
				result.append(HEX_TABLE[c & 0x0f]);
			}
		}

		return result.toString();
	}

	protected static String format(int intval)
	{
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private static String format(short shortval)
	{
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	private static short getCount()
	{
		if (counter < 0)
			counter = 0;
		return counter++;
	}

	public static String getDigestPassword(String userid, String password)
			throws NoSuchAlgorithmException
	{
		String myinfo = userid + ":" + password;
		return md5(myinfo);
	}

	public static String md5(String src) throws NoSuchAlgorithmException
	{
		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		byte[] digesta = null;
		MessageDigest alga = MessageDigest.getInstance("MD5");
		alga.update(src.getBytes());
		digesta = alga.digest();

		int b = (digesta[15] + 256) & 0xff;
		byte[] digestb = new byte[15];
		for (int i = 0; i < 15; i++)
		{
			digestb[i] = digesta[i];
		}

		String ret = new BASE64Encoder().encode(digestb);
		ret = ret + base64.charAt(b >> 6) + base64.charAt(b & 0x3f);
		return ret;
	}

	/**
	 * Unique down to millisecond
	 */
	private static short getHiTime()
	{
		return (short) (System.currentTimeMillis() >> 32);
	}

	/**
	 * Unique in a local network
	 */
	private static int getIP()
	{
		return IP;
	}

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	private static int getJVM()
	{
		return JVM;
	}

	private static int getLoTime()
	{
		return (int) System.currentTimeMillis();
	}

	public static String getNotNullString(Object source)
	{
		return source == null ? "" : source.toString();
	}

	public static String getUid()
	{
		return new StringBuffer(16).append(format(getHiTime())).append(
				SEPARATOR).append(format(getLoTime())).append(
				format(getCount())).toString();
	}

	public static String getGuid()
	{
		return new StringBuffer(36).append(format(getIP())).append(SEPARATOR)
				.append(format(getJVM())).append(SEPARATOR).append(
						format(getHiTime())).append(SEPARATOR).append(
						format(getLoTime())).append(SEPARATOR).append(
						format(getCount())).toString();
	}

	public static int ocurrence(String source, String find)
	{
		int i = 0;
		int start = 0;
		int flen = find.length();

		while (true)
		{
			start = source.indexOf(find, start);
			if (start < 0)
				break;

			i++;
			start += flen;
		}

		return i;
	}

	public static String replaceParameters(String source,
			Map<String, String> parameters)
	{
		if (source == null)
			return null;

		// replace named parameters
		StringBuffer sb = new StringBuffer();
		Matcher matcher = _parameterNamePattern.matcher(source);

		while (matcher.find())
		{
			// ����@name
			String matched = matcher.group();
			String name = matcher.group(1);
			if (name != null && name.length() > 0)
			{
				String value = (String) parameters.get(name);

				if (value != null)
				{
					matcher.appendReplacement(sb, matched.replaceAll(
							"@" + name, value));
				}
				else
				{
					matcher.appendReplacement(sb, matched);
				}
			}
		}

		matcher.appendTail(sb);
		return sb.toString();
	}

	public static void visitTokens(String source, Pattern pattern,
			TokenVisitor tv)
	{
		if (source == null)
			return;

		Matcher matcher = pattern.matcher(source);
		while (matcher.find())
		{
			tv.visitToken(matcher);
		}

		tv.finish(matcher);
	}

	public static String replaceParameters(String source,
			Map<String, String>[] parameters)
	{
		if (source == null)
			return null;

		Map<String, String> ps = new StringKeyMap<String>();
		for (int i = 0; i < parameters.length; i++)
		{
			Map<String, String> m = parameters[i];
			if (m != null)
			{
				ps.putAll(m);
			}
		}

		return replaceParameters(source, ps);
	}

	public static String strictReplaceAll(String source, String match,
			String replacement)
	{
		StringBuffer sb = new StringBuffer(source);

		int f = 0;
		int len1 = match.length();
		int len2 = replacement.length();

		while (true)
		{
			int i = sb.indexOf(match, f);
			if (i < 0)
				break;

			sb.replace(i, i + len1, replacement);
			f = i + len2;

			if (f >= sb.length())
				break;
		}

		return sb.toString();
	}

	private static int toInt(byte[] bytes)
	{
		int result = 0;
		for (int i = 0; i < 4; i++)
		{
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	/**
	 * ���ַ���Ϣtrim��ָ�����ȣ����ಿ����...����
	 * 
	 * @param source
	 *            Դ��Ϣ
	 * @param limit
	 *            ָ������
	 */
	public static String trimToLimit(String source, int limit)
	{
		String head = new String(source);
		int length = head.length();
		if (length > limit)
		{
			if (length > 3)
			{
				head = head.substring(0, limit - 3);
				head += "...";
			}
			else
			{
				head = head.substring(0, limit);
			}
		}
		return head;
	}

	/**
	 * ��url patternת��������pattern
	 * 
	 * @param pattern
	 * @return
	 */
	public static String urlPatternToRegexpPattern(String pattern)
	{
		if (pattern.length() == 0 || pattern.length() == 1
				&& pattern.charAt(0) == '/')
		{
			return "^.*$";
		}

		else if (pattern.equals("/*"))
			return "^.*$";

		int length = pattern.length();

		if (pattern.charAt(0) != '/' && pattern.charAt(0) != '*')
		{
			pattern = "/" + pattern;
			length++;
		}

		boolean isExact = true;
		StringBuffer cb = new StringBuffer();
		cb.append("^");
		for (int i = 0; i < length; i++)
		{
			char ch = pattern.charAt(i);

			if (ch == '*' && i + 1 == length && i > 0)
			{
				isExact = false;

				if (pattern.charAt(i - 1) == '/')
				{
					cb.setLength(cb.length() - 1);
				}
			}
			else if (ch == '*')
			{
				isExact = false;
				cb.append(".*");
			}
			else if (ch == '.' || ch == '[' || ch == '^' || ch == '$'
					|| ch == '{' || ch == '}' || ch == '|' || ch == '('
					|| ch == ')' || ch == '?')
			{
				cb.append('\\');
				cb.append(ch);
			}
			else
				cb.append(ch);
		}

		if (isExact)
			cb.append("\\z");
		else
			cb.append("(?=/)|" + cb.toString() + "\\z");

		if (cb.length() > 0 && cb.charAt(0) == '/')
			cb.insert(0, '^');

		return cb.toString();
	}
}
