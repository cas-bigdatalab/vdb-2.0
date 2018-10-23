package vdb.mydb.theme;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.StringKeyMap;

public class ThemeManager implements InitializingBean
{
	File _themesDir;

	private ListMap<String, Theme> _themes = new ListMap<String, Theme>(
			new StringKeyMap<Theme>());

	private String _defaultThemeName;

	private void addTheme(Theme type)
	{
		_themes.add(type.getName(), type);
	}

	public Theme getTheme(String name)
	{
		if (!_themes.map().containsKey(name))
			name = _defaultThemeName;

		return _themes.map().get(name);
	}

	public List<Theme> getThemes()
	{
		return _themes.list();
	}

	public void loadThemes(File themesDir)
	{
		List<Theme> themes = new ArrayList<Theme>();
		for (File themeDir : themesDir.listFiles())
		{
			String name = themeDir.getName();
			if (name.startsWith("#"))
				continue;

			if (name.startsWith("_"))
				continue;

			if (name.startsWith("."))
				continue;

			File metaXml = new File(themeDir, "meta.xml");
			if (metaXml.exists())
			{
				try
				{
					XmlBeanFactory factory = new XmlBeanFactory(
							new FileSystemResource(metaXml));
					Theme theme = (Theme) factory.getBean(factory
							.getBeanNamesForType(Theme.class)[0]);
					theme.setName(name);
					themes.add(theme);
				}
				catch (Exception e)
				{
					System.err.println(String.format(
							"failed to load theme: %s", name));

					e.printStackTrace();
				}
			}
		}

		// sort
		Collections.sort(themes, new Comparator<Theme>()
		{

			public int compare(Theme t1, Theme t2)
			{
				return t1.getOrder() - t2.getOrder();
			}
		});

		for (Theme theme : themes)
		{
			Logger.getLogger(this.getClass()).debug(
					String.format("loading theme: %s(%s)", theme.getName(),
							theme.getTitle()));
			addTheme(theme);
		}
	}

	public File getThemesDir()
	{
		return _themesDir;
	}

	public void setThemesDir(File themesDir)
	{
		this._themesDir = themesDir;
	}

	public void afterPropertiesSet() throws Exception
	{
		loadThemes(_themesDir);
	}

	public String getDefaultThemeName()
	{
		return _defaultThemeName;
	}

	public void setDefaultThemeName(String defaultThemeName)
	{
		_defaultThemeName = defaultThemeName;
	}
}
