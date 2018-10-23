package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbMultiSelect;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.MapEntries;
import cn.csdb.commons.util.StringKeyMap;

public class VdbMultiSelectType extends AbstractFieldDriver<VdbMultiSelect>
{
	/**
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getOptions() throws Exception
	{
		Map<String, String> options = new StringKeyMap<String>();

		String optionStyle = _field.get("optionStyle");
		if (optionStyle.equals("sql"))
		{
			AnyBeanDao dao = new AnyBeanDao(_field.getEntity());
			JdbcSource js = dao.getJdbcSource();
			StringSql sql = new StringSql(_field.get("options"));
			try
			{
				List<Map<String, Serializable>> list = js.queryForObjects(sql);
				for (int i = 0; i < list.size(); i++)
				{
					Map<String, Serializable> map = list.get(i);
					Object[] o = map.values().toArray();
					if (o.length >= 2)
					{
						options.put(o[0].toString(), o[1].toString());
					}
					if (o.length == 1)
					{
						options.put(o[0].toString(), o[0].toString());
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			options = new MapEntries(_field.get("options"), ",", "=")
					.getOptions();
		}
		return options;
	}

	public VdbMultiSelect createData() throws Exception
	{
		VdbMultiSelect vd = new VdbMultiSelect(getOptions());
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbMultiSelect data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}

	public VdbMultiSelect jdbcSelect(JdbcRowReader jdbcRowReader)
			throws Exception
	{
		VdbMultiSelect vd = createData();
		vd.setValue("" + jdbcRowReader.get(_field.getColumnName()));
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbMultiSelect data,
			VdbMultiSelect dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}
}