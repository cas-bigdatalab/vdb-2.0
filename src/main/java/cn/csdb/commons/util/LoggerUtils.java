/*
 * 创建日期 2005-5-9
 */
package cn.csdb.commons.util;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author bluejoe
 */
public class LoggerUtils
{
	public static Logger getLogger(String loggerName, String pattern)
	{
		Logger logger = Logger.getLogger(loggerName);
		PatternLayout pl = new PatternLayout(pattern);
		ConsoleAppender ca = new ConsoleAppender(pl);
		logger.addAppender(ca);

		return logger;
	}

	/**
	 * 获取logger
	 * 
	 * @param loggerName
	 * @param logFilePath
	 * @throws IOException
	 */
	public static Logger getLogger(String loggerName, String logFilePath,
			String pattern) throws IOException
	{
		Logger logger = Logger.getLogger(loggerName);
		PatternLayout pl = new PatternLayout(pattern);
		FileAppender fa = new FileAppender(pl, logFilePath, true);
		logger.addAppender(fa);

		return logger;
	}

}
