/*
 * Created on 2006-7-23
 */
package cn.csdb.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipFile;

/**
 * 从指定的类路径中查找符合条件的类。 匹配条件由ClassMatcher给出。
 * 
 * @author bluejoe
 */
public class ClassFinder
{
	public interface ClassMatcher
	{
		public boolean matches(Class clazz);
	}

	private static final String DOT_CLASS = ".class"; // $NON-NLS-1$

	private static final String DOT_JAR = ".jar"; // $NON-NLS-1$

	public static List findClasses(List listClasses, ClassMatcher matcher)
	{
		List cs = new Vector();
		for (int i = 0; i < listClasses.size(); i++)
		{
			Class c = (Class) listClasses.get(i);
			if (c != null && matcher.matches(c))
				listClasses.add(c);
		}

		return cs;
	}

	public static List findClassesInPaths(String strPaths, ClassMatcher matcher)
	{
		List listClasses = new Vector();
		StringTokenizer st = new StringTokenizer(strPaths, System
				.getProperty("path.separator"));
		while (st.hasMoreTokens())
		{
			try
			{
				findClassesInRootPath(st.nextToken(), listClasses, matcher);
			}
			catch (Throwable e)
			{
				// e.printStackTrace();
			}
		}

		return listClasses;
	}

	public static void findClassesInRootPath(String rootPath, List listClasses,
			ClassMatcher matcher) throws IOException
	{
		findClassesInPath(rootPath, new File(rootPath), listClasses, matcher);
	}

	public static void findClassesInPath(String rootPath, File file,
			List listClasses, ClassMatcher matcher) throws IOException
	{
		if (!file.exists())
			return;

		// 目录？
		if (file.isDirectory())
		{
			String[] list = file.list();
			for (int i = 0; i < list.length; i++)
			{
				File fi = new File(file, list[i]);
				// Recursive call
				findClassesInPath(rootPath, fi, listClasses, matcher);
			}
		}

		// 文件
		else if (file.length() > 0)
		{
			findClassesInFile(rootPath, file, listClasses, matcher);
		}
	}

	private static Class forName(String strClassName)
			throws ClassNotFoundException
	{
		strClassName = strClassName.replace('\\', '.'); // $NON-NLS-1$ //
		// $NON-NLS-2$
		strClassName = strClassName.replace('/', '.'); // $NON-NLS-1$ //
		// $NON-NLS-2$
		// remove ".class"
		strClassName = strClassName.substring(0, strClassName.length()
				- DOT_CLASS.length());
		return Class.forName(strClassName, false, ClassFinder.class
				.getClassLoader());
	}

	/**
	 * 从jar或者class文件中加载类
	 */
	public static void findClassesInFile(String rootPath, File file,
			List listClasses, ClassMatcher matcher) throws IOException
	{
		String path = file.getPath();

		// 文件后缀
		// .jar
		if (path.endsWith(DOT_JAR))
		{
			ZipFile zipFile = new ZipFile(file);
			Enumeration entries = zipFile.entries();
			while (entries.hasMoreElements())
			{
				String strEntry = entries.nextElement().toString();
				if (strEntry.endsWith(DOT_CLASS))
				{
					Class c;
					try
					{
						c = forName(strEntry);
						if (c != null && matcher.matches(c))
							listClasses.add(c);
					}
					catch (Throwable e)
					{
						// e.printStackTrace();
					}
				}
			}
		}

		// .class
		else if (path.endsWith(DOT_CLASS))
		{
			String cn = path.substring(rootPath.length() + 1,
					path.lastIndexOf(".")) // $NON-NLS-1$
					.replace(File.separator.charAt(0), '.');

			try
			{
				Class c = Class.forName(cn, false, ClassFinder.class
						.getClassLoader());
				if (c != null && matcher.matches(c))
					listClasses.add(c); // $NON-NLS-1$
			}
			catch (Throwable e)
			{
				// e.printStackTrace();
			}
		}
	}
}