/*
 * Created on 2007-6-5
 */
package cn.csdb.commons.orm;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.util.ConfigurationUtils;
import cn.csdb.commons.util.StringKeyMap;

public class XmlMapper implements BeanMapper
{
	private Map<Class, BeanMapping> _classifiedRules = new HashMap<Class, BeanMapping>();

	private Map<String, BeanMapping> _namedRules = new HashMap<String, BeanMapping>();

	public void load(String xmlPath) throws Exception
	{
		try
		{
			if (xmlPath == null)
			{
				throw new Exception("configure error: xml path is null");
			}

			File xmlFile = new File(xmlPath);
			Configuration root = new DefaultConfigurationBuilder()
					.buildFromFile(xmlPath);
			Configuration conf = root.getChild("catalog-config");

			if (conf == null)
			{
				throw new Exception(MessageFormat.format(
						"can not find node `<catalog-config>` in `{0}`",
						new Object[] { xmlPath }));
			}

			// 读取repository结点
			Configuration[] repositoryNodes = conf.getChildren("repository");
			for (int i = 0; i < repositoryNodes.length; i++)
			{
				Configuration repositoryNode = repositoryNodes[i];
				if (repositoryNode == null)
				{
					throw new Exception(MessageFormat.format(
							"can not find node `<repository>` in `{0}`",
							new Object[] { xmlPath }));
				}

				Map rp = new StringKeyMap();
				rp.putAll(ConfigurationUtils.loadProperties(repositoryNode, 1));
				rp.put("current-working-directory", xmlFile.getParent());
				String repositoryName = (String) rp.get("name");

				if (repositoryName == null)
					repositoryName = "";
				try
				{
					// 获取source
					DataSource ds = getJdbcSource(rp).getDataSource();

					// 获取所有node
					Configuration[] nodeNodes = repositoryNode
							.getChildren("node");
					for (int j = 0; j < nodeNodes.length; j++)
					{
						// 获取node配置信息
						Configuration nodeNode = nodeNodes[j];

						// 读取所有属性
						Map<String, String> ep = new StringKeyMap<String>();
						ep.putAll(ConfigurationUtils
								.loadProperties(nodeNode, 1));

						BeanMapping rule = createMapping(ds, ep);
						_classifiedRules.put(rule.getBeanClass(), rule);
						_namedRules.put(ep.get("name"), rule);
					}
				}
				catch (Exception e)
				{
					throw new Exception(MessageFormat.format(
							"failed to load repository `{0}` from `{1}`",
							new Object[] { repositoryName, xmlPath }), e);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	private JdbcSource getJdbcSource(Map properties) throws Exception
	{
		String source = (String) properties.get("source");
		JdbcSource ss = JdbcManager.getInstance().getJdbcSource(source);
		if (ss == null)
		{
			throw new RuntimeException(MessageFormat
					.format("failed to find data source `{0}`",
							new Object[] { source }));
		}

		return ss;
	}

	private BeanMapping createMapping(DataSource dataSource, Map properties)
			throws Exception
	{
		String nn = (String) properties.get("name");
		if (nn == null)
		{
			throw new Exception(MessageFormat.format(
					"attribute `name` of node required", new Object[] {}));
		}

		String bn = (String) properties.get("bean-class");
		if (bn == null)
		{
			throw new Exception(MessageFormat.format(
					"`{0}` of node `{1}` required", new Object[] {
							"bean-class", nn }));
		}

		Class bc = Class.forName(bn);
		String pkns = (String) properties.get("primary-key-name");
		if (bn == null)
		{
			throw new Exception(MessageFormat.format(
					"`{0}` of node `{1}` required", new Object[] {
							"primary-key-name", nn }));
		}

		return new SimpleBeanMapping(bc, dataSource, (String) properties
				.get("path"), pkns);
	}

	public BeanMapping getMapping(Class beanClass)
	{
		return (BeanMapping) _classifiedRules.get(beanClass);
	}

	public BeanMapping getMapping(String ruleName)
	{
		return (BeanMapping) _namedRules.get(ruleName);
	}

}
