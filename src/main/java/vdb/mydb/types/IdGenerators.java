package vdb.mydb.types;

import java.util.List;

import vdb.metacat.idgen.IdGenerator;
import cn.csdb.commons.util.ListMap;

public class IdGenerators
{
	private ListMap<String, IdGenerator> _generators = new ListMap<String, IdGenerator>();

	public IdGenerator getGenerator(String type)
	{
		return _generators.map().get(type);
	}

	public List<IdGenerator> getGenerators()
	{
		return _generators.list();
	}

	public void setGenerators(List<IdGenerator> products)
	{
		for (IdGenerator product : products)
		{
			_generators.add(product.getName(), product);
		}
	}
}
