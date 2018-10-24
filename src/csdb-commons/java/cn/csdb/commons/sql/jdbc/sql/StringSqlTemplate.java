package cn.csdb.commons.sql.jdbc.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSqlTemplate extends StringSql
{
	private static Pattern _parameterNamePattern = Pattern.compile("\\$([\\w]+)");

	public StringSqlTemplate(String sqlTemplate)
	{
		// replace named parameters
		StringBuffer sb = new StringBuffer();
		Matcher matcher = _parameterNamePattern.matcher(sqlTemplate);

		while (matcher.find())
		{
			// ²¶»ñ$varName
			String name = matcher.group(1);
			if (name != null && name.length() > 0)
			{
				String var = "$" + name;
				matcher.appendReplacement(sb, "?");
				addParameter(var);
			}
		}

		matcher.appendTail(sb);
		setString(sb.toString());
	}
}
