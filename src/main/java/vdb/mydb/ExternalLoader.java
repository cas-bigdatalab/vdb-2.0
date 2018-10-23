package vdb.mydb;

import java.io.Serializable;

public interface ExternalLoader<T extends Serializable>
{
	T load(Serializable key);
}
