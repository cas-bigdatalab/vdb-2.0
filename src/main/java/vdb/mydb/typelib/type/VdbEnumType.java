package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbEnum;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.MapEntries;
import cn.csdb.commons.util.StringKeyMap;

public class VdbEnumType extends AbstractFieldDriver<VdbEnum>
{
	/**
	 * ��ȡ����ö��ֵ
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
			// ���ѡ����sql��ʽ����ô���õ����
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
						// ����ƺ�ֵ�ķ�ʽ������Ҫȡ��һ�ԶԵ����
						options.put(o[0].toString(), o[1].toString());
					}
					if (o.length == 1)
					{
						// ����ƺ�ֵ�ķ�ʽ������Ҫȡ��һ�ԶԵ����
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
			// ����ͨ��ʽ
			options = new MapEntries(_field.get("options"), ",", "=")
					.getOptions();
		}
		return options;
	}

	public VdbEnum createData() throws Exception
	{
		VdbEnum vd = new VdbEnum(getOptions());
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbEnum data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}

	public VdbEnum jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbEnum vd = createData();
		Object o = jdbcRowReader.get(_field.getColumnName());
		if (o == null)
			vd.setValue("");
		else
			vd.setValue("" + o);
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbEnum data,
			VdbEnum dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}
}