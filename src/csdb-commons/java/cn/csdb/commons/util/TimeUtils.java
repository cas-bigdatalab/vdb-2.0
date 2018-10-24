/*
 * 创建日期 2005-8-11
 */
package cn.csdb.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author bluejoe
 */
public class TimeUtils
{
	public static String getNowString()
	{
		Calendar calendar = Calendar.getInstance();
		return getTimeString(calendar);
	}

	public static String getNowString(String dateFormat)
	{
		Calendar calendar = Calendar.getInstance();
		return getTimeString(calendar, dateFormat);
	}

	/**
	 * 获取时间字符串
	 * 
	 * @param cldr
	 *            Calendar类型 String 字符串，格式"yyyy-MM-dd HH:mm:ss"
	 */
	public static String getTimeString(Calendar calendar)
	{
		return getTimeString(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getTimeString(java.util.Calendar calendar,
			String dateFormat)
	{
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(calendar.getTime());
	}

	public static String getTimeString(java.util.Date date)
	{
		return getTimeString(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getTimeString(java.util.Date date, String dateFormat)
	{
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}

	public static Date parseDate(String dateString) throws ParseException
	{
		return parseDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDate(String dateString, String dateFormat)
			throws ParseException
	{
		DateFormat df = new SimpleDateFormat(dateFormat);
		return df.parse(dateString);
	}
}
