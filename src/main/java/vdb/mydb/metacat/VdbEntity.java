package vdb.mydb.metacat;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.HasNoNameExeption;
import vdb.metacat.RelationKey;
import vdb.metacat.View;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.typelib.FieldType;
import cn.csdb.commons.sql.catalog.JdbcColumn;
import cn.csdb.commons.sql.catalog.JdbcTable;
import cn.csdb.commons.util.CollectionUtils;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.Matcher;
import cn.csdb.commons.util.StringKeyMap;

public class VdbEntity extends CatalogObjectImpl implements Entity
{
	private static final String VIEW_DETAIL = "showBean";

	private static final String VIEW_EDIT = "updateBean";

	private static final String VIEW_INDEX = "indexEntity";

	private static final String VIEW_LIST = "listBeans";

	private static final String VIEW_QUERY = "queryEntity";

	protected List<RelationKey> _collectionKeys = new ArrayList<RelationKey>();

	protected DataSet _dataSet;

	protected Field _editorField;

	protected ListMap<String, Field> _fields = new ListMap<String, Field>(
			new StringKeyMap<Field>());

	protected Field _groupField;

	protected Identifier _identifier;

	protected Field _orderField;

	protected ListMap<String, RelationKey> _referenceKeys = new ListMap<String, RelationKey>(
			new StringKeyMap<RelationKey>());

	protected Field _timeStampField;

	protected Field _titleField;

	public void addCollectionKey(RelationKey fk)
	{
		_collectionKeys.add(fk);
	}

	/**
	 * 将属性对象添加到实体属性列表中
	 */
	public void addField(Field field)
	{
		String name = field.getName();
		if (name == null)
			throw new HasNoNameExeption(field);

		_fields.add(name, field);
	}

	/**
	 * 根据属性名字New一个新的属性对象，并添加到此实体中
	 * 
	 * @param name
	 *            属性名字
	 * @return 返回属性对象
	 */
	public VdbField addField(String name)
	{
		VdbField field = new VdbField();
		field.setEntity(this);
		field.setName(name);
		addField(field);

		return field;
	}

	public void addReferenceKey(RelationKey fk)
	{
		_referenceKeys.add(fk.getColumnName(), fk);
	}

	public View addView(String viewName)
	{
		View view = new VdbView();
		view.setName(viewName);
		view.setSource(this);
		getDataSet().addView(view);

		return view;
	}

	// 周勋 20100417
	// 数据组织形式 N_字段名字段值 或 P_字段名字段值 N代表xml中新增的字段 P代表存在的字段
	public void attach(ServletRequest request) throws Exception
	{
		// xml中存在的字段
		Field[] fieldsList = this.getFields();
		// 请求中所有值
		Enumeration<?> e = request.getParameterNames();

		ArrayList<Field> fieldUnList = new ArrayList<Field>();
		ArrayList<String> columnList = new ArrayList<String>();
		// 请求包含值plist
		ArrayList<String> pList = new ArrayList<String>();
		while (e.hasMoreElements())
		{
			pList.add(e.nextElement().toString());
		}
		/* 判断是否为新增字段 */
		for (int i = 0; i < pList.size(); i++)
		{
			String name = pList.get(i);

			// 如果是新增字段，放入field，放入fieldUnList列表
			if (name.startsWith("N_name"))
			{
				Field field = new VdbField();
				// 1将当前vdbEntity设置
				field.setEntity(this);
				// 2
				field.setName(request.getParameter(name));
				// 3将field加入vdbEntity中
				this.addField(field);
				// 4将field加入fieldUnList列表
				fieldUnList.add(field);
				// 5将字段名加入columnList
				columnList.add(name.substring(6));
			}
		}

		for (int j = 0; j < pList.size(); j++)
		{
			// 参数原始值
			String oldName = pList.get(j);
			String name = oldName;

			// request中的参数与存在的做比较
			if (name.startsWith("P_"))
			{
				name = name.substring(2);
				for (Field field : fieldsList)
				{
					// 获取xml中存在的列名
					String columnName = field.getColumnName();
					// 存在的参数以列名结尾（xml中在） &&
					// 包含nametypetitledefaultsizecolumnName其中之一的关键字（这写法头次见）
					if (name.endsWith(columnName)
							&& "nametypetitledefaultsizecolumnName"
									.indexOf(name.substring(2, name.length()
											- columnName.length())) > 0)
					{
						// 取字段名
						String fieldName = name.substring(0, name.length()
								- columnName.length());
						// 如果是字段名是type
						if (fieldName.contains("type"))
						{
							((VdbField) field).setTypeName(request
									.getParameter(oldName));
							FieldType fieldType = VdbManager.getEngine()
									.getCatalogContext().getFieldType(
											request.getParameter(oldName));
							field.setType(fieldType);
							field.setTypeDriver(fieldType.createDriver(field));
						}
						// 字段名是其他的情况
						else

							field.set(fieldName, request.getParameter(oldName));

						RelationKey relationKey = field.getRelationKey();
						field.setRelationKey(relationKey);
						VdbManager.getEngine().getCatalog().cacheIn(field);
						break;
					}
				}
			}
			// 字段是新增时
			else if (name.startsWith("N_"))
			{
				name = name.substring(2);
				for (int i = 0; i < fieldUnList.size(); i++)
				{
					String columnName = columnList.get(i);
					if (name.endsWith(columnName)
							&& "nametypetitledefaultsizecolumnName"
									.indexOf(name.substring(2, name.length()
											- columnName.length())) > 0)
					{
						String fieldName = name.substring(0, name.length()
								- columnName.length());

						if (fieldName.contains("type"))
						{
							((VdbField) fieldUnList.get(i)).setTypeName(request
									.getParameter(oldName));
							FieldType fieldType = VdbManager.getEngine()
									.getCatalogContext().getFieldType(
											request.getParameter(oldName));
							fieldUnList.get(i).setType(fieldType);
							fieldUnList.get(i).setTypeDriver(
									fieldType.createDriver(fieldUnList.get(i)));
						}
						else
							fieldUnList.get(i).set(fieldName,
									request.getParameter(oldName));

						RelationKey relationKey = fieldUnList.get(i)
								.getRelationKey();
						fieldUnList.get(i).setRelationKey(relationKey);
						VdbManager.getEngine().getCatalog().cacheIn(
								fieldUnList.get(i));
						break;
					}
				}
			}
		}

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				this.getDataSet());
	}

	public void attachBatchUpdate(ServletRequest request) throws Exception
	{
		// 请求中所有值
		Enumeration<?> e = request.getParameterNames();

		// 参数中新增的字段列表[列表仅存储字段的名字]，名字以N_开始
		ArrayList<String> newList = new ArrayList<String>();
		ArrayList<String> tempList = new ArrayList<String>();

		// 参数中已经描述过的字段列表[列表仅存储字段的名字]，名字以P_开始
		ArrayList<String> preList = new ArrayList<String>();

		while (e.hasMoreElements())
		{
			String paraStr = e.nextElement().toString();
			if (paraStr.startsWith("N_name"))
			{
				newList.add(paraStr);
			}
			if (paraStr.startsWith("P_name"))
			{
				preList.add(paraStr);
			}
		}

		// 已经描述过的字段，进行修改
		Field[] fieldsList = this.getFields();// 得到实体当前所有字段
		for (Field field : fieldsList)
		{
			for (int j = 0; j < preList.size(); j++)
			{
				String name = preList.get(j);
				// getName相等意味着字段的URI相等，URI一旦新增后，就不允许再修改
				if (field.getName()
						.equalsIgnoreCase(request.getParameter(name)))
				{
					String columnName = preList.get(j).substring(6);
					field.set("size", request.getParameter("P_size"
							+ columnName));
					field.setDefaultValue(request.getParameter("P_defaultValue"
							+ columnName));
					field
							.setTitle(request.getParameter("P_title"
									+ columnName));

					((VdbField) field).setTypeName(request
							.getParameter("P_type" + columnName));
					FieldType fieldType = VdbManager
							.getEngine()
							.getCatalogContext()
							.getFieldType(
									request.getParameter("P_type" + columnName));
					field.setType(fieldType);
					field.setTypeDriver(fieldType.createDriver(field));

					VdbManager.getEngine().getCatalog().cacheIn(field);
					break;
				}
			}
		}

		// 数据库中字段列表
		JdbcTable jdbcTable = getJdbcTable();
		List<JdbcColumn> fieldsFromDB = new ArrayList<JdbcColumn>(jdbcTable
				.getColumns());

		// 将newList的顺序与fieldsFromDB保持一致
		for (Iterator<JdbcColumn> idb = fieldsFromDB.iterator(); idb.hasNext();)
		{
			JdbcColumn jc = (JdbcColumn) idb.next();
			for (Iterator<String> in = newList.iterator(); in.hasNext();)
			{
				String newStr = in.next().toString();
				if (newStr.equals("N_name" + jc.getColumnName()))
				{
					tempList.add(newStr);
				}
			}
		}
		newList.clear();
		newList.addAll(tempList);

		// 未描述的字段
		for (int i = 0; i < newList.size(); i++)
		{
			String name = newList.get(i);
			String columnName = name.substring(6);

			Field field = new VdbField();

			field.setEntity(this);
			field.setName(request.getParameter(name));

			FieldType fieldType = VdbManager.getEngine().getCatalogContext()
					.getFieldType(request.getParameter("N_type" + columnName));
			field.setType(fieldType);
			((VdbField) field).setTypeName(request.getParameter("N_type"
					+ columnName));
			field.setTypeDriver(fieldType.createDriver(field));

			field.setColumnName(request.getParameter("N_columnName"
					+ columnName));
			field.setTitle(request.getParameter("N_title" + columnName));
			field.set("size", request.getParameter("N_size" + columnName));
			field.setDefaultValue(request.getParameter("N_defaultValue"
					+ columnName));
			this.addField(field);

			VdbManager.getEngine().getCatalog().cacheIn(field);
		}

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				this.getDataSet());
	}

	/**
	 * @deprecated
	 * @return
	 */
	public List<Field> getAttributes()
	{
		return _fields.list();
	}

	/**
	 * 得到实体所有的集合关系键<br>
	 * 保存一对多或多对多关系时，会将集合关系键保存到相应的实体中
	 */
	public RelationKey[] getCollectionKeys()
	{
		return _collectionKeys.toArray(new RelationKey[0]);
	}

	public AnyBeanDao getDao() throws Exception
	{
		return new AnyBeanDao(this);
	}

	public String getDataClassNameFromJdbcType(int sqlType)
	{
		String type = "String";
		switch (sqlType)
		{
			case Types.SMALLINT:
			case Types.TINYINT:
			case Types.BIGINT:
			case Types.DECIMAL:
			case Types.INTEGER:
			case Types.NUMERIC:
				type = "Long";
				break;
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.REAL:
				type = "Double";
				break;

			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				type = "Date";
				break;

			case Types.CLOB:
			case Types.BLOB:
				type = "Files";
				break;
		}

		return type;
	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	private View getDefaultView(String viewName)
	{
		Matcher<Field> matcher = null;
		if (VIEW_LIST.equalsIgnoreCase(viewName))
			matcher = new Matcher<Field>()
			{

				public boolean matches(Field toMatch)
				{
					return !toMatch.isCollection();
				}
			};

		if (VIEW_INDEX.equalsIgnoreCase(viewName))
			matcher = new Matcher<Field>()
			{

				public boolean matches(Field toMatch)
				{
					return "String".equalsIgnoreCase(toMatch.getTypeName())
							|| "RichText".equalsIgnoreCase(toMatch
									.getTypeName());
				}
			};

		if (VIEW_EDIT.equalsIgnoreCase(viewName))
			matcher = new Matcher<Field>()
			{

				public boolean matches(Field toMatch)
				{
					try
					{
						return !toMatch.getType().isReadonly();
					}
					catch (Exception e)
					{
						return false;
					}
				}
			};

		if (VIEW_QUERY.equalsIgnoreCase(viewName))
			matcher = new Matcher<Field>()
			{

				public boolean matches(Field toMatch)
				{
					try
					{
						return toMatch.getType().isQueryable();
					}
					catch (Exception e)
					{
						return false;
					}
				}
			};

		return getFullView(matcher);
	}

	public View getDetailView()
	{
		return getView(VIEW_DETAIL);
	}

	public Field getEditorField()
	{
		return _editorField;
	}

	public View getEditView()
	{
		return getView(VIEW_EDIT);
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbEntity getEx()
	{
		return this;
	}

	public Field getField(String name)
	{
		return _fields.map().get(name);
	}

	public Field getFieldByColumnName(String columnName)
	{
		for (Field field : this.getFields())
		{
			if (columnName.equalsIgnoreCase(field.getColumnName()))
				return field;
		}

		return null;
	}

	public Field[] getFields()
	{
		return _fields.list().toArray(new Field[0]);
	}

	public void getFields(Map<String, Field> fields)
	{
		fields.putAll(_fields.map());
	}

	public View getFullView(Matcher<Field> matcher)
	{
		View view = new VdbView();
		view.setSource(this);

		for (Field field : this.getFields())
		{
			if (matcher == null || matcher.matches(field))
				view.addItem(field);
		}

		return view;
	}

	public Field getGroupField()
	{
		return _groupField;
	}

	public Identifier getIdentifier()
	{
		return _identifier;
	}

	/**
	 * 得到实体的数据库表对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public JdbcTable getJdbcTable() throws Exception
	{
		return ((VdbDataSet) getDataSet()).getJdbcDatabase().getTable(
				this.getTableName());
	}

	public String getNeedAudit()
	{
		return get("needAudit");
	}

	public String getOrder()
	{
		return get("order");
	}

	public String getOrderAsc()
	{
		return get("orderAsc");
	}

	public Field getOrderField()
	{
		return _orderField;
	}

	public String getOrderId()
	{
		return get("orderId");
	}

	public View getQueryView()
	{
		return getView(VIEW_QUERY);
	}

	/**
	 * @deprecated
	 * @param columnName
	 * @return
	 */
	public RelationKey getReferenceForeignKey(String columnName)
	{
		return getReferenceKey(columnName);
	}

	public RelationKey getReferenceKey(String columnName)
	{
		return _referenceKeys.map().get(columnName);
	}

	/**
	 * 得到实体所有的引用关系键<br>
	 * 保存一对多关系时，会将引用关系键保存到相应的实体中
	 */
	public RelationKey[] getReferenceKeys()
	{
		return _referenceKeys.list().toArray(new RelationKey[0]);
	}

	public Entity getTable()
	{
		return this;
	}

	public String getTableName()
	{
		return get("tableName");
	}

	public View getThumbView()
	{
		return getView(VIEW_LIST);
	}

	public Field getTimeStampField()
	{
		return _timeStampField;
	}

	public String getTitle()
	{
		return get("title");
	}

	public Field getTitleField()
	{
		Field field = internalGetTitleField();
		if (field == null)
		{
			setTitleField(this.getIdentifier().getField());
		}

		return field;
	}

	public List<RelationKey> getUntitledCollections() throws Exception
	{
		// 每个关系都有两个关系键
		// 一对多关系：一个是引用关系键，一个是集合关系键;
		// 多对多关系：两个都是集合关系键
		List<RelationKey> roles = new ArrayList<RelationKey>();
		// 获得实体中所有的集合关系键
		CollectionUtils.copy(getCollectionKeys(), roles);

		// 遍历VDB实体的所有属性列表
		for (Field field : getFields())
		{
			// 如果字段类型为“集合类型”
			if (field.isCollection())
			{
				// 将此字段对应的集合关系键从roles中删除
				roles.remove(field.getRelationKey());
			}
		}

		return roles;
	}

	/**
	 * 获得未被VDB实体描述的数据库表的列
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<JdbcColumn> getUntitledColumns() throws Exception
	{
		JdbcTable jdbcTable = getJdbcTable();
		// 得到数据库表对象的字段列表
		List<JdbcColumn> jdbcColumns = new ArrayList<JdbcColumn>(jdbcTable
				.getColumns());
		// 遍历VDB实体属性列表，判断某字段是否被描述
		for (Field field : getFields())
		{
			// 得到VDB实体属性对应的数据库表字段对象
			// 对于引用属性来说，有对应的字段名（列名）
			JdbcColumn column = jdbcTable.getColumn(field.getColumnName());
			// 如果不为空，则表示此字段已经被描述
			if (column != null)
				jdbcColumns.remove(column);
		}
		return jdbcColumns;
	}

	/**
	 * 获得未被VDB实体描述的引用类型字段<br>
	 * 当为实体建立起相应的“关系”时，会显示相应的引用类型字段<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RelationKey> getUntitledReferences() throws Exception
	{
		// 每个关系都有两个关系键
		// 一对多关系：一个是引用关系键，一个是集合关系键;
		// 多对多关系：两个都是集合关系键
		List<RelationKey> roles = new ArrayList<RelationKey>();
		// 获得实体中所有的引用关系键
		CollectionUtils.copy(getReferenceKeys(), roles);

		// 遍历VDB实体的所有属性列表
		for (Field field : getFields())
		{
			// 如果字段类型为“引用类型”
			if (field.isReference())
			{
				// 将此字段对应的引用关系键从roles中删除
				roles.remove(field.getRelationKey());
			}
		}

		return roles;
	}

	public String getUri()
	{
		return _dataSet.getUri() + "." + getName();
	}

	public View getView(String viewName)
	{
		DataSet dataSet = getDataSet();
		View view = new ViewsProxy(dataSet.getViews()).getView(this, viewName);
		if (view == null)
			view = getDefaultView(viewName);

		return view;
	}

	public String getViewAfterAudit()
	{
		return get("viewAfterAudit");
	}

	private Field internalGetTitleField()
	{
		return _titleField == null ? this.getIdentifier().getField()
				: _titleField;
	}

	public boolean isAutoPrimaryKey() throws Exception
	{
		return getIdentifier().getIdGenerator() != null;
	}

	public boolean isDictionaryTable()
	{
		return false;
	}

	public boolean isQuery()
	{
		return false;
	}

	public void removeCollectionKey(RelationKey fk)
	{
		_collectionKeys.remove(fk);
	}

	public void removeField(Field field)
	{
		_fields.remove(field.getName());
	}

	public void removeReferenceKey(RelationKey fk)
	{
		_referenceKeys.remove(fk.getColumnName());
	}

	public void setDataSet(DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void setEditorField(Field editorField)
	{
		_editorField = editorField;
	}

	public void setGroupField(Field groupField)
	{
		_groupField = groupField;
	}

	public void setIdentifier(Identifier identifier)
	{
		_identifier = identifier;
	}

	public void setOrderField(Field field)
	{
		_orderField = field;
	}

	public void setTableName(String tableName)
	{
		set("tableName", tableName);
	}

	public void setTimeStampField(Field timeStampField)
	{
		_timeStampField = timeStampField;
	}

	public void setTitle(String title)
	{
		set("title", title);
	}

	public void setTitleField(Field titleField)
	{
		_titleField = titleField;
	}
}
