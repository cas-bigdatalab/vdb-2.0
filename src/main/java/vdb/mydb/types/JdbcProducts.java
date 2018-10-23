package vdb.mydb.types;

import java.util.List;

import org.apache.log4j.Logger;

import cn.csdb.commons.util.ListMap;

public class JdbcProducts
{
	private ListMap<String, JdbcProduct> _products = new ListMap<String, JdbcProduct>();

	public JdbcProduct getProduct(String type)
	{
		return _products.map().get(type);
	}

	public List<JdbcProduct> getProducts()
	{
		return _products.list();
	}

	public void setProducts(List<JdbcProduct> products)
	{
		for (JdbcProduct product : products)
		{
			Logger.getLogger(this.getClass()).debug(
					String.format("loading database driver: %s(%s)", product
							.getName(), product.getTitle()));
			_products.add(product.getName(), product);
		}
	}
}
