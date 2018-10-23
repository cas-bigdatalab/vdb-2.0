package vdb.metacat;

import java.io.Serializable;

import vdb.mydb.typelib.FieldType;
import vdb.mydb.typelib.VdbFieldDriver;

public interface Field extends CatalogObject
{
	public String getColumnName();

	public Serializable getDefaultValue();

	Entity getEntity();

	String getName();

	public RelationKey getRelationKey();

	public int getSize();

	public String getTitle();

	public FieldType getType();

	public VdbFieldDriver getTypeDriver();

	public String getTypeName();

	boolean isCollection();

	public boolean isReference();

	boolean isStrongCollection();

	boolean isWeakCollection();

	void setColumnName(String columnName);

	public void setDefaultValue(Serializable defaultValue);

	void setEntity(Entity entity);

	void setName(String name);

	void setRelationKey(RelationKey key);

	public void setSize(int columnSize);

	public void setTitle(String title);

	public void setType(FieldType type);

	public void setTypeDriver(VdbFieldDriver typeDriver);
}
