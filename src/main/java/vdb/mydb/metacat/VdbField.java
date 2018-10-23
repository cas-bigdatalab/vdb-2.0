package vdb.mydb.metacat;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.RelationKey;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.mydb.VdbManager;
import vdb.mydb.typelib.FieldType;
import vdb.mydb.typelib.VdbFieldDriver;
import cn.csdb.commons.sql.catalog.JdbcColumn;

public class VdbField extends CatalogObjectImpl implements Field
{
	protected Entity _entity;

	protected RelationKey _relationKey;

	protected FieldType _type;

	protected VdbFieldDriver _typeDriver;

	public VdbField()
	{

	}

	public void attach(ServletRequest request) throws Exception
	{
		new CatalogObjectProxy(this).attach(request);
	}

	public String getColumnName()
	{
		return get("columnName");
	}

	public Serializable getDefaultValue()
	{
		return get("defaultValue");
	}

	public Entity getEntity()
	{
		return _entity;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbField getEx()
	{
		return this;
	}

	/**
	 * @deprecated
	 */
	public FieldType getFieldType()
	{
		return getTypeDef();
	}

	/**
	 * @deprecated
	 * @return
	 */
	public RelationKey getForeignKey()
	{
		return getRelationKey();
	}

	public String getHint()
	{
		return get("hint");
	}

	public JdbcColumn getJdbcColumn() throws Exception
	{
		return ((VdbEntity) getEntity()).getJdbcTable().getColumn(
				getColumnName());
	}

	public String getOptionsText()
	{
		return get("options");
	}

	public RelationKey getRelationKey()
	{
		return _relationKey;
	}

	public int getSize()
	{
		try
		{
			return _meta.getNumber("size").intValue();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public String getTitle()
	{
		return get("title");
	}

	public FieldType getType()
	{
		return _type;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public FieldType getTypeDef()
	{
		return getType();
	}

	public VdbFieldDriver getTypeDriver()
	{
		return _typeDriver;
	}

	public String getTypeName()
	{
		return getType().getName();
	}

	@Override
	public String getUri()
	{
		return _entity.getUri() + "." + getName();
	}

	public boolean isCollection()
	{
		return "Collection".equalsIgnoreCase(getTypeName());
	}

	public boolean isIdentifier()
	{
		return _entity.getIdentifier().getField() == this;
	}

	public boolean isNullable()
	{
		return _meta.getBoolean("nullable");
	}

	public boolean isNumeric()
	{
		String typeName = getTypeName();
		return "integer".equalsIgnoreCase(typeName)
				|| "double".equalsIgnoreCase(typeName);
	}

	public boolean isReference()
	{
		return "Reference".equalsIgnoreCase(getTypeName());
	}

	public boolean isStrongCollection()
	{
		return isCollection() && getRelationKey().isOneToMany();
	}

	public boolean isWeakCollection()
	{
		return isCollection() && !getRelationKey().isOneToMany();
	}

	public void setColumnName(String columnName)
	{
		set("columnName", columnName);
	}

	public void setDefaultValue(Serializable defaultValue)
	{
	}

	public void setEntity(Entity entity)
	{
		_entity = entity;
	}

	public void setRelationKey(RelationKey relationKey)
	{
		_relationKey = relationKey;
	}

	public void setSize(int columnSize)
	{
		_meta.setNumber("size", columnSize);
	}

	public void setTitle(String title)
	{
		set("title", title);
	}

	public void setType(FieldType type)
	{
		_type = type;
	}

	public void setTypeDriver(VdbFieldDriver typeDriver)
	{
		_typeDriver = typeDriver;
	}

	public void setTypeName(String typeName)
	{
		setType(VdbManager.getInstance().getTypeManager()
				.getFieldType(typeName));
	}
}
