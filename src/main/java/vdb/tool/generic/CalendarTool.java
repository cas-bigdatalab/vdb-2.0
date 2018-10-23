package vdb.tool.generic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 得到当前的日历，并以中文的形式输出
 * 
 * @author 苏贤明
 * 
 */
public class CalendarTool
{

	public static String getCalendar()
	{
		Date date = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dr = df.format(date);
		StringBuffer calendar = new StringBuffer();
		calendar.append(dr.substring(0, 4) + "年");
		calendar.append(dr.substring(5, 7) + "月");
		calendar.append(dr.subSequence(8, 10) + "日 ");

		int s = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

		switch (s)
		{
			case 1:
				calendar.append("星期日");
				break;
			case 2:
				calendar.append("星期一");
				break;
			case 3:
				calendar.append("星期二");
				break;
			case 4:
				calendar.append("星期三");
				break;
			case 5:
				calendar.append("星期四");
				break;
			case 6:
				calendar.append("星期五");
				break;
			case 7:
				calendar.append("星期六");
				break;
			default:
				break;
		}
		return calendar.toString();
	}

	public static String getTime()
	{
		return "" + Calendar.getInstance().getTimeInMillis();
	}
}
