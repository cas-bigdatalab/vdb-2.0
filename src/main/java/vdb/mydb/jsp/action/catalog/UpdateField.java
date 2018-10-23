package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.util.StringUtils;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.catalog.JdbcColumn;

public class UpdateField extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// 如果用户点击的为修改，则传入的参数为字段ID
		String id = request.getParameter("id");
		VdbField field = null;
		VdbEntity table = null;

		if (id != null)
		{
			request.setAttribute("isNew", "false");// 此值可以在vpage页面上直接通过$isNew来引用
			// 根据字段ID得到字段对象
			field = VdbManager.getEngine().getCatalog().fromId(id);
			// 得到字段所属实体对象
			table = (VdbEntity) field.getEntity();
		}
		// 如果用户点击的为“描述成**属性“或者“添加新的属性”，则传入的参数肯定有：实体ID
		else
		{
			request.setAttribute("isNew", "true");// 此值可以在vpage页面上直接通过$isNew来引用
			// 得到实体ID
			table = VdbManager.getEngine().getCatalog().fromId(
					request.getParameter("tid"));
			// 得到描述的类型：具体有列、引用、集合、表达式等
			String type = request.getParameter("type");
			// 如果描述为列属性，则执行以下操作
			if ("column".equals(type))
			{
				String columnName = request.getParameter("cn");
				String fieldName = StringUtils.formatWords(columnName);
				VdbField fi = new VdbField();
				fi.setEntity(table);
				fi.setName(fieldName);
				fi.setColumnName(columnName);
				fi.setTitle(StringUtils.formatWords2(columnName));

				VdbEntity ee = table;
				JdbcColumn column = ee.getJdbcTable().getColumn(columnName);
				fi.setTypeName(ee.getDataClassNameFromJdbcType(column
						.getDataType()));
				fi.setSize(column.getColumnSize());
				fi.setDefaultValue(column.getDefaultValue());
				field = fi;
				// 将字段对象写入缓存，供DoExpandClass.java中调用，因为字段类型有对应的参数文件需要编辑(option.html)
				VdbManager.getEngine().getCatalog().cacheIn(field);// 将字段信息写入缓存，此时并未保存到schema.xml文件中
			}
			// 如果描述为集合属性，则执行以下操作
			else if ("collection".equals(type))
			{
				// 任何关系都会有两个关系键
				// 得到关系键的ID
				String roleId = request.getParameter("role");
				String peerColumnName = request.getParameter("cn");
				// 得到关系键
				RelationKey role = VdbManager.getEngine().getCatalog().fromId(
						roleId);
				VdbField fi = new VdbField();
				fi.setEntity(table);
				fi.setName(role.getTarget().getName() + peerColumnName);
				fi.setTypeName("Collection");
				fi.setRelationKey(role);

				fi.setTitle(role.getTarget().getTitle());
				field = fi;
				// 将字段对象写入缓存，供DoExpandClass.java中调用，因为字段类型有对应的参数文件需要编辑(option.html)
				VdbManager.getEngine().getCatalog().cacheIn(field);// 将字段信息写入缓存，此时并未保存到schema.xml文件中
			}
			// 如果描述为引用属性，则执行以下操作
			else if ("reference".equals(type))
			{
				// 得到关系键的ID
				String roleId = request.getParameter("role");
				String columnName = request.getParameter("cn");
				RelationKey role = VdbManager.getEngine().getCatalog().fromId(
						roleId);
				VdbField fi = new VdbField();
				// 当调用getId()方法时，生成字段的ID，因此vpage页面直接通过$meta.id能够得到字段的ID
				fi.setEntity(table);
				fi.setName(role.getTarget().getName() + columnName);
				fi.setTypeName("Reference");
				fi.setTitle(role.getTarget().getTitle());
				fi.setRelationKey(role);

				field = fi;
				// 将字段对象写入缓存，供DoExpandClass.java中调用，因为字段类型有对应的参数文件需要编辑(option.html)
				VdbManager.getEngine().getCatalog().cacheIn(field);// 将字段信息写入缓存，此时并未保存到schema.xml文件中
			}
			// 其他，比如新增属性时，默认均为表达式类型
			else
			{
				VdbField fi = new VdbField();
				fi.setEntity(table);
				fi.setTypeName("Expression");
				field = fi;

				// 注：field的ID号是在getId方法调用时生成的，不需要显示的赋值
			}
		}
		request.setAttribute("table", table);
		request.setAttribute("meta", field);
		request.setAttribute("field", field);
	}
}
