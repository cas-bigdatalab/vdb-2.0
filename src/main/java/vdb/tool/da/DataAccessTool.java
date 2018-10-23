package vdb.tool.da;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParser;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.QueryCreator;
import vdb.mydb.query.QueryCreatorManager;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.JsoExpr;
import vdb.mydb.query.impl.JsoQuery;
import vdb.mydb.vtl.toolbox.EscapeUnescape;
import vdb.service.parser.VdbJsoQueryBuilder;
import vdb.service.parser.VdbQueryNode;
import vdb.service.parser.VdbQueryNodeFactory;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.StringKeyMap;

public class DataAccessTool
{
	public Pageable<AnyBean> query(String uri, String queryString)
	{
		try
		{
			VdbJsoQueryBuilder builder = new VdbJsoQueryBuilder();
			builder.setEntity(uri);
			CQLParser parser = new CQLParser();
			CQLNode node = parser.parse(queryString);
			VdbQueryNode n = VdbQueryNodeFactory.createNode(node);
			builder.setWhereFilter(n);
			String jsonQuery = builder.toString();
			return query(jsonQuery);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	public Pageable<AnyBean> query(String jsonQuery)
	{
		try
		{
			// 根据Json的字符串构造JsoQuery对象
			JsoQuery jsoQuery = parseJsoQuery(jsonQuery);
			// 根据JsoQuery对象的Entity属性，构造实际查询对象（此时，实际查询对象除Entity属性外，其他均为空）
			AnyQuery anyQuery = createQuery(jsoQuery);
			// 将JsoQuery中的其他属性，如whereFilter等合并到实际查询对象anyQuery中
			mergeQuery(anyQuery, jsoQuery);

			final QueryExecutor qe = execute(anyQuery);
			Pageable<AnyBean> anyBeans = new Pageable<AnyBean>()
			{
				private int _size = qe.size();

				public List<AnyBean> list(int beginning, int size)
						throws Exception
				{
					if (beginning < 1)
					{
						beginning = 1;
					}
					int end = beginning + size;
					if (end > _size)
						end = _size;

					return qe.list(beginning, end);
				}

				public int size() throws Exception
				{
					return _size;
				}
			};
			return anyBeans;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	// 目前，还不能支持term形式，即不能将VdbTremQueryNode转换到JsoQuery中，因为JsoExpr中还没有term属性
	// TODO 待完善，将term属性添加到JsoExpr类中
	@SuppressWarnings("deprecation")
	public JsoQuery parseJsoQuery(String jsoQueryString)
	{
		JSONObject jq = JSONObject.fromObject(EscapeUnescape
				.unescape(jsoQueryString));
		Map<String, Class<JsoExpr>> map = new StringKeyMap<Class<JsoExpr>>();
		map.put("whereFilter", JsoExpr.class);
		map.put("a", JsoExpr.class);
		map.put("b", JsoExpr.class);

		// 将JSONObject转换为JsoQuery对象[JSONObject实现了MAP接口]
		// map变量的作用在于：对于key为“whereFilter”、“a”、“b”的变量，按照JSOExpr类的形式转换
		return (JsoQuery) JSONObject.toBean(jq, JsoQuery.class, map);
	}

	public AnyQuery createQuery(JsoQuery jsoQuery) throws Exception
	{
		QueryCreator queryCreator = QueryCreatorManager.getInstance()
				.getQueryCreator(jsoQuery);

		if (queryCreator != null)
			return queryCreator.createQuery();

		return null;
	}

	public void mergeQuery(AnyQuery query, JsoQuery jsoQuery) throws Exception
	{
		JdbcExpr filter = query.where();

		if (jsoQuery.getOrderField() != null)
		{
			Field orderField = ((Field) VdbManager.getEngine().getCatalog()
					.fromUri(jsoQuery.getOrderField()));

			if (orderField != null)
			{
				query.orderBy(orderField, jsoQuery.getOrderAsc());
			}
			else
			{
				Entity entity = (Entity) VdbManager.getEngine().getCatalog()
						.fromUri(jsoQuery.getEntity());
				orderField = entity.getOrderField();
				if (orderField != null)
				{
					query.orderBy(orderField, entity.getOrder());
				}
			}
		}

		JsoExpr whereFilter = jsoQuery.getWhereFilter();
		if (whereFilter != null)
			filter = query.and(filter, whereFilter.toJdbcExpr(query));

		query.where(filter);
		for (Entry<String, Serializable> var : jsoQuery.getVariables()
				.entrySet())
		{
			query.bindVariable(var.getKey(), var.getValue());
		}
	}

	public QueryExecutor execute(VarQuery query) throws Exception
	{
		return new AnyBeanDao(query.getEntity()).execute(query);
	}

	public List<Map<String, Serializable>> execute(String dataSetUri,
			String sql, int start, int size) throws Exception
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet dataset = catalog.fromUri(dataSetUri);
		AnyBeanDao dao = new AnyBeanDao(dataset);
		JdbcSource js = dao.getJdbcSource();
		StringSql strsql = new StringSql(sql);

		List<Map<String, Serializable>> list = js.queryForObjects(strsql,
				start, size);
		return list;
	}

	public List<Map<String, Serializable>> execute(String dataSetUri, String sql)
			throws Exception
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet dataset = catalog.fromUri(dataSetUri);
		AnyBeanDao dao = new AnyBeanDao(dataset);
		JdbcSource js = dao.getJdbcSource();
		StringSql strsql = new StringSql(sql);

		List<Map<String, Serializable>> list = js.queryForObjects(strsql);
		return list;
	}

	/**
	 * 初始化下拉菜单组件
	 * 
	 * @param name
	 * @param list
	 * @param value
	 * @param view
	 * @return
	 */
	public String initSelect(String name, List<Map<String, Serializable>> list,
			String value, String view)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='" + name + "' name='" + name + "'>");
		for (Map<String, Serializable> map : list)
		{

			sb.append("<option value='" + map.get(value) + "'>" + map.get(view)
					+ "</option>");
		}
		sb.append("</select>");

		return sb.toString();
	}

	/**
	 * 初始化下拉菜单组件加选中标记
	 * 
	 * @param name
	 * @param list
	 * @param value
	 * @param view
	 * @return
	 */
	public String initSelect(String name, String selectvalue,
			List<Map<String, Serializable>> list, String value, String view)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='" + name + "' name='" + name + "'>");
		for (Map<String, Serializable> map : list)
		{
			if (map.get(value).equals(selectvalue))
				sb.append("<option value='" + map.get(value) + "' selected >"
						+ map.get(view) + "</option>");
			else
				sb.append("<option value='" + map.get(value) + "'>"
						+ map.get(view) + "</option>");
		}
		sb.append("</select>");

		return sb.toString();
	}

	/**
	 * 初始化下拉菜单组件,包含全部
	 * 
	 * @param name
	 *            下拉菜单id,name
	 * @param list
	 *            下拉菜单内容列表
	 * @param value
	 *            key
	 * @param view
	 *            value
	 * @param all
	 *            全部实现的中文
	 * @return
	 */
	public String initSelect(String name, List<Map<String, Serializable>> list,
			String value, String view, String all, String sel)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<select id='" + name + "' name='" + name + "'>");
		sb.append("<option value='all' selected>" + all + "</option>");
		for (Map<String, Serializable> map : list)
		{
			if (map.get(value).toString().equalsIgnoreCase(sel))
				sb.append("<option value='" + map.get(value) + "' selected>"
						+ map.get(view) + "</option>");
			else
				sb.append("<option value='" + map.get(value) + "'>"
						+ map.get(view) + "</option>");
		}
		sb.append("</select>");

		return sb.toString();
	}

	public int recordsSize(String dataSetUri, String sql) throws Exception
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet dataset = catalog.fromUri(dataSetUri);
		AnyBeanDao dao = new AnyBeanDao(dataset);
		JdbcSource js = dao.getJdbcSource();
		StringSql strsql = new StringSql(sql);

		List<Map<String, Serializable>> list = js.queryForObjects(strsql);
		return list.size();
	}

	public int executeUpdate(String entityUri, String sql) throws Exception
	{
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Entity entity = catalog.fromUri(entityUri);
		AnyBeanDao dao = new AnyBeanDao(entity);
		StringSql strsql = new StringSql(sql);
		int i = dao.getJdbcSource().executeUpdate(strsql);
		return i;
	}
}