package cn.csdb.commons.util;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 工厂模式
 * 
 * @author bluejoe
 */
public class Messages
{
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages()
	{
	}

	public static String getString(String key)
	{
		try
		{
			return new String(RESOURCE_BUNDLE.getString(key).getBytes(
					"iso-8859-1"), "gbk");
		}
		catch (MissingResourceException e)
		{
			return "<<<" + key + ">>>";
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return "<<<" + key + ">>>";
		}
	}
}
