package vdb.mydb.context;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import cn.csdb.commons.util.ObjectHelper;

public class VdbPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer
{
	private Map<String, ExtendedProperty> _extendedProperties;

	@Override
	protected String resolvePlaceholder(String arg0, Properties arg1)
	{
		String value = super.resolvePlaceholder(arg0, arg1);
		// if arg0 is a VTL expression
		if (value != null && !arg0.equalsIgnoreCase(value))
		{
			Logger.getLogger(this.getClass()).debug(
					String.format("`%s` ==> `%s`", arg0, value));

			return new ObjectHelper(value).evalString();
		}
		else
		{
			Logger.getLogger(this.getClass()).warn(
					String.format("unable to evaluate `%s`", arg0));
		}

		return super.resolvePlaceholder(arg0, arg1);
	}

	@Override
	protected Properties mergeProperties() throws IOException
	{
		Properties ps = super.mergeProperties();
		for (Entry<String, ExtendedProperty> entry : _extendedProperties
				.entrySet())
		{
			ps.put(entry.getKey(), entry.getValue().getString());
		}

		return ps;
	}

	public void setExtendedProperties(
			Map<String, ExtendedProperty> extendedProperties)
	{
		_extendedProperties = extendedProperties;
	}
}
