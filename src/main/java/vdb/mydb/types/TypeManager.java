package vdb.mydb.types;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.ServletContextAware;

import vdb.mydb.typelib.FieldType;
import vdb.mydb.util.ClassPropertyEditor;
import vdb.mydb.util.FilePathSpan;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.StringKeyMap;

public class TypeManager implements InitializingBean, ServletContextAware
{
	VdbClassLoader _customClassLoader = new VdbClassLoader(this.getClass()
			.getClassLoader());

	File _typesDir;

	private ListMap<String, FieldType> _types = new ListMap<String, FieldType>(
			new StringKeyMap<FieldType>());

	private ServletContext _servletContext;

	private void addType(FieldType type)
	{
		_types.add(type.getName(), type);
	}

	public boolean hasFile(FieldType type, String fileName)
	{
		return new File(type.getTypePath(), fileName).exists();
	}

	public String getFilePath(FieldType type, String fileName)
			throws IOException
	{
		return new FilePathSpan(new File(_servletContext.getRealPath("/")),
				new File(type.getTypePath(), fileName)).getRelativePath();
	}

	public FieldType getFieldType(String typeName)
	{
		if ("LocalFiles".equalsIgnoreCase(typeName))
			return getFieldType("Files");

		if (!_types.map().containsKey(typeName))
			throw new RuntimeException(String.format("unknown field type: %s",
					typeName));

		return _types.map().get(typeName);
	}

	public List<FieldType> getFieldTypes()
	{
		return _types.list();
	}

	public void loadTypes(File typelibDir)
	{
		List<FieldType> types = new ArrayList<FieldType>();

		for (File typeDir : typelibDir.listFiles())
		{
			String name = typeDir.getName();
			if (name.startsWith("#"))
				continue;

			if (name.startsWith("_"))
				continue;

			if (name.startsWith("."))
				continue;

			File metaXml = new File(typeDir, "meta.xml");
			if (metaXml.exists())
			{
				File libDir = new File(typeDir, "lib");
				try
				{
					if (libDir.exists())
					{
						for (File jar : libDir.listFiles())
						{
							String jarName = jar.getName();
							if (jarName.endsWith(".jar"))
							{
								_customClassLoader.addURL(jar.toURL());
							}
						}
					}

					XmlBeanFactory factory = new XmlBeanFactory(
							new FileSystemResource(metaXml));
					factory.registerCustomEditor(Class.class,
							new ClassPropertyEditor(_customClassLoader));

					FieldType type = (FieldType) factory.getBean(factory
							.getBeanNamesForType(FieldType.class)[0]);
					type.setName(name);
					type.setTypePath(metaXml.getParentFile());
					types.add(type);
				}
				catch (Exception e)
				{
					System.err.println(String.format(
							"failed to load field type: %s", name));

					e.printStackTrace();
				}
			}
		}

		// sort
		Collections.sort(types, new Comparator<FieldType>()
		{

			public int compare(FieldType t1, FieldType t2)
			{
				return t1.getOrder() - t2.getOrder();
			}
		});

		for (FieldType type : types)
		{
			Logger.getLogger(this.getClass()).debug(
					String.format("loading field type: %s(%s)", type.getName(),
							type.getTitle()));
			addType(type);
		}
	}

	public void setTypes(List<FieldType> types)
	{
		for (FieldType type : types)
		{
			_types.add(type.getName(), type);
		}
	}

	public void afterPropertiesSet() throws Exception
	{
		loadTypes(_typesDir);
	}

	public File getTypesDir()
	{
		return _typesDir;
	}

	public void setTypesDir(File typesDir)
	{
		_typesDir = typesDir;
	}

	public VdbClassLoader getCustomClassLoader()
	{
		return _customClassLoader;
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}
}
