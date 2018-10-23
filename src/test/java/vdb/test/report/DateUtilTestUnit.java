package vdb.test.report;

import org.junit.Assert;
import org.junit.Test;

import vdb.report.util.DateUtil;

public class DateUtilTestUnit
{
	@Test
	public void testGetrecord()
	{
		Assert.assertEquals("2009-03-07", DateUtil.generateWeekend());
		Assert.assertEquals("2008-05-06", DateUtil.getDateAsString(DateUtil
				.getDateAsCalendar("2008-05-06")));
		Assert.assertEquals("2009-03-01", DateUtil.generateWeekstart());
		Assert.assertEquals("2009-03-01", DateUtil.getFirstDayofMonth(3));
		Assert.assertEquals("2009-03-31", DateUtil.getLastDayofMonth(3));
		Assert.assertEquals("2009-03-04", DateUtil.getTodayAsString());
	}

}
