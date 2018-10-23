package vdb.mydb.bean;

import java.io.Serializable;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.typelib.VdbData;

public interface AnyBean extends Serializable
{
	public VdbData get(Field field) throws Exception;

	/**
	 * 
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public VdbData get(String fieldName) throws Exception;

	public Entity getEntity();

	public Map<String, Field> getFields();

	public VdbData getId() throws Exception;

	public ItemID getUri() throws Exception;

	public VdbData getTitle() throws Exception;

	public void set(Field field, VdbData value);

	public void setId(Serializable id) throws Exception;
}