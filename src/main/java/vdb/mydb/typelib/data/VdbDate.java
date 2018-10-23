package vdb.mydb.typelib.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.util.TimeUtils;

public class VdbDate extends AbstractData
{
	private Date _date;

	/**
	 * @deprecated use format() instead
	 * @return
	 */
	public String dateFormat(String format)
	{
		return format(format);
	}

	public String format(String format)
	{
		if (format == null || format.length() == 0)
			format = "yyyy-MM-dd";

		try
		{
			return TimeUtils.getTimeString(_date, format);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public Sdef getAsSdef()
	{
		if (_date != null)
			return new SimpleSdef("" + _date.getTime(), format("yyyy-MM-dd"));
		else
			return new SimpleSdef("", "");
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		if (ddl.getValue() != null && ddl.getValue().length() > 0)
			_date = new Date(Long.parseLong(ddl.getValue()));
	}

	public void setDate(Date date)
	{
		_date = date;
	}

	public Date getDate()
	{
		return _date;
	}

	public String getAsText()
	{
		return getAsSdef().getValue();
	}

	public void setAsText(String text)
	{
		if (text.trim().length() == 10)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				_date = sdf.parse(text);
			}
			catch (ParseException e)
			{

				e.printStackTrace();
			}
		}
		else if (text.trim().length() == 19)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				_date = sdf.parse(text);
			}
			catch (ParseException e)
			{

				e.printStackTrace();
			}
		}
		// _date = new Date(Long.parseLong(text));
	}

	/**
	 * 日期格式的大小返回一个长整型
	 */
	public long getBytes() {
		return Long.SIZE;
	}

	public boolean isEmpty() throws Exception {
		return _date==null || getValue().getBytes().length==0;
	}
}
