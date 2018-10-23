package vdb.metacat;

public interface Domain extends CatalogObject
{
	public void addDataSet(DataSet dataSet);

	public void addView(View view);

	public DataSet[] getDataSets();

	public String getName();

	public String getThemeName();

	public String getTitle();

	public View[] getViews();

	public void removeDataSet(DataSet dataSet);

	public void removeView(View view);

	public void setName(String name);

	public void setTitle(String title);
}
