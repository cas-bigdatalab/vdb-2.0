package vdb.mydb.vtl.toolbox;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.velocity.tools.view.ViewToolContext;

import vdb.metacat.CatalogObject;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.View;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.bean.ItemID;
import vdb.mydb.bean.VdbItemID;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbView;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.BeanList;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.QueryCreator;
import vdb.mydb.query.QueryCreatorManager;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.QueryFromSql;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.BeanListImpl;
import vdb.mydb.query.impl.JsoExpr;
import vdb.mydb.query.impl.JsoQuery;
import vdb.tool.generic.FormatTool;
import vdb.tool.ui.PagerTool;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.jdbc.sql.StringSqlTemplate;
import cn.csdb.commons.util.StringKeyMap;
import cn.csdb.commons.util.StringUtils;

/**
 * @deprecated USE specific tool instead!
 * @author bluejoe
 * 
 */

public class VdbTool
{
	private ViewToolContext _context;

	public BeanList createBeanList(Entity entity) throws Exception
	{
		return new BeanListImpl(entity);
	}

	public BeanList createBeanList(String uriEntity) throws Exception
	{
		return createBeanList((Entity) VdbManager.getInstance().getCatalog()
				.fromUri(uriEntity));
	}

	public Object createObject(String className) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException
	{
		return Class.forName(className).newInstance();
	}

	/**
	 * @deprecated
	 */
	public PageViewer createPageViewer(Pageable query, int pageIndex, int size)
			throws Exception
	{
		return new PagerTool().create(query, pageIndex, size);
	}

	/**
	 * @deprecated use PagerTool instead
	 */
	public PageViewer createPageViewer(VarQuery query, int pageIndex, int size)
			throws Exception
	{
		return new PagerTool().create(query, pageIndex, size);
	}

	/**
	 * @deprecated use PagerTool instead!
	 */
	public PageViewer createPageViewer(VarQuery query, int pageIndex,
			String size) throws Exception
	{
		return new PagerTool().create(query, pageIndex, Integer.parseInt(size));
	}

	public VarQuery createQuery(Entity entity) throws Exception
	{
		return new AnyBeanDao(entity).createQuery();
	}

	public AnyQuery createQuery(JsoQuery jsoQuery) throws Exception
	{
		QueryCreator queryCreator = QueryCreatorManager.getInstance()
				.getQueryCreator(jsoQuery);

		if (queryCreator != null)
			return queryCreator.createQuery();

		return null;
	}

	public VarQuery createQuery(String uriEntity) throws Exception
	{
		return createQuery((Entity) VdbManager.getInstance().getCatalog()
				.fromUri(uriEntity));
	}

	public QueryFromSql createQueryFromSql(Entity entity, String sqlTemplate)
			throws Exception
	{
		QueryFromSql query = new AnyBeanDao(entity).createQueryFromSql();
		query.setSql(new StringSqlTemplate(sqlTemplate));

		return query;
	}

	public QueryFromSql createQueryFromSql(String uriEntity, String sqlTemplate)
			throws Exception
	{
		return createQueryFromSql((Entity) VdbManager.getInstance()
				.getCatalog().fromUri(uriEntity), sqlTemplate);
	}

	public View createView(CatalogObject node)
	{
		return new VdbView();
	}

	public String encodeXml(String src, boolean encodeBlank)
	{
		return StringUtils.encodeXml(src, encodeBlank);
	}

	public QueryExecutor execute(VarQuery query) throws Exception
	{
		return new AnyBeanDao(query.getEntity()).execute(query);
	}

	/**
	 * @deprecated
	 */
	public String format(String pattern, Object o)
	{
		return new FormatTool().format(pattern, o);
	}

	/**
	 * @deprecated
	 */
	public String decode(String str, String code)
	{
		String result = null;
		try
		{
			result = URLDecoder.decode(str, code);
		}
		catch (Exception e)
		{

		}
		return result;
	}

	/**
	 * @deprecated
	 */
	public String encode(String str, String code)
	{
		String result = null;
		try
		{
			result = URLEncoder.encode(str, code);
		}
		catch (Exception e)
		{

		}
		return result;
	}

	/**
	 * @deprecated
	 */
	public String formatSubStr(String str, int length, String replace)
	{
		return new FormatTool().formatSubStr(str, length, replace);
	}

	/**
	 * @deprecated
	 */
	public String formatDate(Date o)
	{
		return new FormatTool().formatDate(o);
	}

	/**
	 * @deprecated
	 */
	public String formatDate(Date o, String pattern)
	{
		return new FormatTool().formatDate(o, pattern);
	}

	public AnyBean getBean(Entity entity, Serializable id) throws Exception
	{
		return ((VdbEntity) entity).getDao().lookup(id);
	}

	public AnyBean getBean(String uri, Serializable id) throws Exception
	{
		return getEntity(uri).getDao().lookup(id);
	}

	public FileMetaData getFile(String uri, Serializable id) throws Exception
	{
		DataSet ds = getDataSet(uri);
		FileMetaData s = VdbManager.getInstance().getFileManager()
				.getFile(ds, "" + id);
		return s;
	}

	public DataSet getDataSet(String uri)
	{
		return VdbManager.getInstance().getCatalog().fromUri(uri);
	}

	public VdbEntity getEntity(String uri)
	{
		return VdbManager.getInstance().getCatalog().fromUri(uri);
	}

	public Field getField(String uri)
	{
		return VdbManager.getInstance().getCatalog().fromUri(uri);
	}

	public CatalogObject getNode(String uri)
	{
		return VdbManager.getInstance().getCatalog().fromUri(uri);
	}

	public View getView(CatalogObject node, String viewName)
	{
		if (node instanceof Domain)
			return ((VdbDomain) node).getView(viewName);

		if (node instanceof DataSet)
			return ((VdbDataSet) node).getView(viewName);

		if (node instanceof Entity)
			return ((VdbEntity) node).getView(viewName);

		return null;
	}

	public void init(Object obj)
	{
		_context = (ViewToolContext) obj;
	}

	public void mergeQuery(QueryFromSql query, JsoQuery jsoQuery)
			throws Exception
	{
		if (jsoQuery.getOrderField() != null)
		{
			List<Field> fields = new ArrayList<Field>();
			String uriField = jsoQuery.getOrderField();
			String[] uriFields = uriField.split(",");
			for (int i = 0; i < uriFields.length; i++)
			{
				fields.add((Field) VdbManager.getInstance().getCatalog()
						.fromUri(uriFields[i]));
			}

			if (fields != null && fields.size() > 0 && fields.get(0) != null)
			{
				query.orderBy(fields.get(0), jsoQuery.getOrderAsc());
			}
			else
			{
				Entity entity = (Entity) VdbManager.getInstance().getCatalog()
						.fromUri(jsoQuery.getEntity());
				Field orderField = entity.getOrderField();
				if (orderField != null)
				{
					query.orderBy(orderField, entity.getOrder());
				}
			}
		}
	}

	public void mergeQuery(AnyQuery query, JsoQuery jsoQuery) throws Exception
	{
		JdbcExpr filter = query.where();

		if (jsoQuery.getOrderField() != null)
		{
			List<Field> field = new ArrayList<Field>();
			String uriField = jsoQuery.getOrderField();
			String[] uriFields = uriField.split(",");
			for (int i = 0; i < uriFields.length; i++)
			{
				field.add((Field) VdbManager.getInstance().getCatalog()
						.fromUri(uriFields[i]));
			}

			if (field != null && field.size() > 0)
			{
				query.orderBy(field, jsoQuery.getOrderAsc());
			}
			else
			{
				Entity entity = (Entity) VdbManager.getInstance().getCatalog()
						.fromUri(jsoQuery.getEntity());
				Field orderField = entity.getOrderField();
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

	// doQuery里面的请求
	public JsoQuery parseJsoQuery(String jsoQueryString)
	{
		JSONObject jq = JSONObject.fromObject(EscapeUnescape
				.unescape(jsoQueryString));
		Map<String, Class> map = new StringKeyMap<Class>();
		map.put("whereFilter", JsoExpr.class);
		map.put("a", JsoExpr.class);
		map.put("b", JsoExpr.class);
		return (JsoQuery) JSONObject.toBean(jq, JsoQuery.class, map);
	}

	// getTableData里面的请求
	public JsoQuery parseJsoQuery(String jsoQueryString, String flag)
			throws UnsupportedEncodingException
	{
		FormatTool formatter = new FormatTool();
		JSONObject jq = JSONObject.fromObject(formatter.decodeUrl(formatter
				.decodeUrl(jsoQueryString)));
		Map<String, Class> map = new StringKeyMap<Class>();
		map.put("whereFilter", JsoExpr.class);
		map.put("a", JsoExpr.class);
		map.put("b", JsoExpr.class);
		return (JsoQuery) JSONObject.toBean(jq, JsoQuery.class, map);
	}

	public ItemID createItemId(String ids)
	{
		return new VdbItemID(ids);
	}

	public String unescape(String src)
	{
		return EscapeUnescape.unescape(src);
	}

	public String escape(String src)
	{
		return EscapeUnescape.escape(src);
	}
}
