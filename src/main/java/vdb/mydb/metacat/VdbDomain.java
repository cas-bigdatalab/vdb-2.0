/*
 * Created on 2004-9-7
 */
package vdb.mydb.metacat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.View;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.mydb.VdbManager;

public class VdbDomain extends CatalogObjectImpl implements Domain
{
	List<DataSet> _dataSets = new ArrayList<DataSet>();

	List<View> _views = new ArrayList<View>();

	public VdbDomain()
	{
	}

	public void addDataSet(DataSet dataSet)
	{
		_dataSets.add(dataSet);
	}

	public View addView(String viewName)
	{
		View view = new VdbView();
		view.setName(viewName);
		view.setSource(this);
		addView(view);

		return view;
	}

	public void addView(View view)
	{
		_views.add(view);
	}

	public void attach(ServletRequest request) throws Exception
	{
		new CatalogObjectProxy(this).attach(request);
	}

	public VdbDataSet createDataSet(String uri)
	{
		VdbDataSet ds = new VdbDataSet();
		ds.setName(uri);
		return ds;
	}

	public DataSet[] getDataSets()
	{
		return _dataSets.toArray(new DataSet[0]);
	}

	private View getDefaultView(String viewName)
	{
		VdbView view = new VdbView();
		view.setSource(this);
		view.setName(viewName);

		for (DataSet ds : this.getDataSets())
		{
			view.addItem(ds);
		}

		return view;
	}

	/**
	 * @deprecated
	 * @return
	 * @throws IOException
	 */
	public File getDomainXml() throws IOException
	{
		return new File(getRootPath(), "domain.xml");
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbDomain getEx()
	{
		return this;
	}

	public String getLayoutTemplate()
	{
		return "/~layout.vpage";
	}

	public String getRootPath() throws IOException
	{
		return VdbManager.getInstance().getVdbRootDir().getCanonicalPath();
	}

	public String getThemeName()
	{
		return get("theme");
	}

	public View getView(String viewName)
	{
		View view = internalGetView(viewName);
		if (view == null)
		{
			view = getDefaultView(viewName);
		}

		return view;
	}

	public View[] getViews()
	{
		return _views.toArray(new View[0]);
	}

	public View internalGetView(String viewName)
	{
		View view = new ViewsProxy(this.getViews()).getView(this, viewName);
		return view;
	}

	public void removeDataSet(DataSet dataSet)
	{
		_dataSets.remove(dataSet);

		// views?
		for (View view : getViews())
		{
			for (int i = 0; i < view.getItems().length; i++)
			{
				if (view.getItems()[i] == dataSet)
				{
					view.removeItem(view.getItems()[i]);
				}
			}
		}
	}

	public void removeView(View view)
	{
		_views.remove(view);
	}
}