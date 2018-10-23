package vdb.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
	public static String generateWeekend()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH,
				-calendar.get(Calendar.DAY_OF_WEEK) + 1 + 6);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static String generateWeekstart()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH,
				-calendar.get(Calendar.DAY_OF_WEEK) + 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static Calendar getDateAsCalendar(String date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		calendar
				.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8,
				10)));
		return calendar;
	}

	public static String getDateAsString(Calendar calendar)
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	public static Date getDateEnd(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		return calendar.getTime();
	}

	public static Date getDateStart(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	public static String getFirstDayofMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getDateAsString(calendar);
	}

	public static Date getHourEnd(int hour)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour - 1);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		return calendar.getTime();
	}

	public static Date getHourStart(int hour)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour - 1);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	public static String getLastDayofMonth(int month)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return getDateAsString(calendar);
	}

	public static String getTodayAsString()
	{
		Calendar today = Calendar.getInstance();
		return new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());
	}

	public static String getTomorrowAsString()
	{
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());
	}

	public static String getYesterdayAsString()
	{
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());
	}

	public static String formatDate2ChineseCustom(Date d)
	{
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public static long getDays(Date startDate, Date endDate)
	{
		if (startDate == null || endDate == null)
		{
			return -1;
		}

		if (endDate.compareTo(startDate) < 0)
			return -1;
		else
		{
			Calendar c1 = Calendar.getInstance();
			c1.setTime(startDate);
			long l1 = c1.getTimeInMillis();

			Calendar c2 = Calendar.getInstance();
			c2.setTime(endDate);
			long l2 = c2.getTimeInMillis();

			long day = (l2 - l1) / (1000 * 60 * 60 * 24);

			return day;

		}
	}

	public static void main(String[] args)
	{
		String date1 = "2009-10-1";
		String date2 = "2009-10-1";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try
		{
			Date startDate = sdf.parse(date1);
			Date endDate = sdf.parse(date2);
			System.out.println(getDays(startDate, endDate));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
}
