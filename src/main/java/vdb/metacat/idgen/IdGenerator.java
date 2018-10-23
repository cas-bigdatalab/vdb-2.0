package vdb.metacat.idgen;

import java.io.Serializable;

import vdb.metacat.Field;

public interface IdGenerator
{
	public boolean accepts(Field field) throws Exception;

	public Serializable generateId(IdGeneratorContext context) throws Exception;

	public String getName();

	public String getTitle();
}
