package vdb.mydb.index;

import java.io.Serializable;
import java.util.Date;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;

public interface Hit
{
	public Serializable getBeanId();

	public DataSet getDataSet();

	public Entity getEntity();

	public Date getIndexTime();

	public String getText();

	public String getTitle();
}
