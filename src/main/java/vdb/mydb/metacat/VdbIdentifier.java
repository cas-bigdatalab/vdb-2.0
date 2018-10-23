package vdb.mydb.metacat;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.metacat.idgen.IdGenerator;

public class VdbIdentifier extends CatalogObjectImpl implements Identifier
{
	private Entity _entity;

	private Field _field;

	private IdGenerator _idGenerator;

	public VdbIdentifier()
	{

	}

	public Entity getEntity()
	{
		return _entity;
	}

	public Field getField()
	{
		return _field;
	}

	public IdGenerator getIdGenerator()
	{
		return _idGenerator;
	}

	public String getIdGeneratorName()
	{
		IdGenerator idGenerator = getIdGenerator();
		if (idGenerator == null)
			return null;
		return getIdGenerator().getName();
	}

	public String getUri()
	{
		return _entity.getUri() + ".identifier";
	}

	public void setEntity(Entity entity)
	{
		_entity = entity;
	}

	public void setField(Field field)
	{
		_field = field;
	}

	public void setIdGenerator(IdGenerator idGenerator)
	{
		_idGenerator = idGenerator;
	}
}
