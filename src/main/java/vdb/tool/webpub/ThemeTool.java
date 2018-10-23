package vdb.tool.webpub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vdb.mydb.VdbManager;
import vdb.mydb.theme.Theme;

public class ThemeTool
{
	public List<Theme> getThemes()
	{
		return VdbManager.getEngine().getThemeManager().getThemes();
	}

	public Theme getTheme()
	{
		return VdbManager.getEngine().getTheme();
	}

	/**
	 * @deprecated
	 */
	public List<String> getJsList()
	{
		List<String> jsList = new ArrayList<String>();

		String filePath = "domain/js";
		File file = getThemeFile(filePath);
		for (String js : file.list())
		{
			if (js.endsWith(".js"))
				jsList.add("js/" + js);
		}

		return jsList;
	}

	public File getThemeFile(String filePath)
	{
		Theme theme = getTheme();
		return getThemeFile(theme, filePath);
	}

	public File getThemeFile(Theme theme, String filePath)
	{
		return new File(new File(VdbManager.getEngine().getThemeManager()
				.getThemesDir(), theme.getName()), filePath);
	}

	/**
	 * @deprecated
	 */
	public List<String> getCssList()
	{
		List<String> jsList = new ArrayList<String>();

		String filePath = "domain/js";
		File file = getThemeFile(filePath);
		for (String css : file.list())
		{
			if (css.endsWith(".css"))
				jsList.add("js/" + css);
		}

		return jsList;
	}
}
