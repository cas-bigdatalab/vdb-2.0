package vdb.metacat;

import vdb.metacat.idgen.IdGenerator;

public interface Identifier extends CatalogObject
{

	Field getField();

	public IdGenerator getIdGenerator();

	String getName();

	void setEntity(Entity entity);

	void setField(Field field);

	public void setIdGenerator(IdGenerator idGenerator);

	Entity getEntity();
}
