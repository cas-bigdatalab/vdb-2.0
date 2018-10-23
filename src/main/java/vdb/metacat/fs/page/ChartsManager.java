package vdb.metacat.fs.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import vdb.metacat.DataSet;
import vdb.mydb.util.CatalogUtil;

import com.thoughtworks.xstream.XStream;

/**
 * 统计方案管理
 * 
 * @author gzb
 * 
 */
public class ChartsManager
{

	private XStream xstream = null;

	public ChartsManager()
	{

	}

	public static final String CHART_CONFIG_FILE_NAME = "charts.xml";// 配置文件名称

	/**
	 * 获得配置文件完整路径
	 * 
	 * @param dataSetUri
	 *            数据集uri
	 * @return
	 */
	private File getFile(DataSet ds)
	{
		return new File(CatalogUtil.getDataSetRoot(ds), CHART_CONFIG_FILE_NAME);
	}

	/**
	 * 加载配置文件中信息
	 * 
	 * @param chart
	 * @param dataSetUri
	 */
	public Chart load(DataSet ds) throws FileNotFoundException
	{
		xstream = new XStream();
		xstream.alias("chart", Chart.class);
		xstream.alias("project", Project.class);
		File file = this.getFile(ds);
		if (file.exists())
		{// 配置文件存在,读入配置文件
			FileInputStream in = new FileInputStream(this.getFile(ds));
			return (Chart) xstream.fromXML(in);
		}
		else
		{// 配置文件不存在,直接保存
			return null;
		}
	}

	/**
	 * 保存配置文件
	 * 
	 * @param chart
	 */
	public void saveAs(Chart chart, DataSet ds) throws IOException
	{
		xstream = new XStream();
		xstream.alias("chart", Chart.class);
		xstream.alias("project", Project.class);

		FileOutputStream out = new FileOutputStream(this.getFile(ds));
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");// 设置编码方式
		xstream.toXML(chart, writer);
		out.close();
	}

	/**
	 * 获得查询统计方案
	 * 
	 * @param name
	 *            查询方案id
	 * @return
	 */
	public Project getProjectByName(String name, DataSet ds)
			throws IOException, ClassNotFoundException
	{
		xstream = new XStream();
		xstream.alias("chart", Chart.class);
		xstream.alias("project", Project.class);
		File file = this.getFile(ds);
		if (!file.exists())
		{
			return null;
		}
		FileInputStream in = new FileInputStream(this.getFile(ds));
		Chart chart = (Chart) xstream.fromXML(in);
		in.close();
		for (Project p : chart.getProjects())
		{
			if (name.equals(p.getName()))
			{
				return p;
			}
		}
		return null;
	}

	/**
	 * 根据数据集uri，初始化查询方案下拉列表
	 * 
	 * @param dataSetUri
	 * @param name
	 *            查询方案id
	 * @return
	 */
	public String initSelect(DataSet ds, String name) throws IOException,
			ClassNotFoundException
	{
		xstream = new XStream();
		xstream.alias("chart", Chart.class);
		xstream.alias("project", Project.class);
		File file = this.getFile(ds);
		if (!file.exists())
		{// 没有保存查询方案
			return "该数据集下没有保存查询方案.";
		}
		FileInputStream in = new FileInputStream(file);
		Chart chart = (Chart) xstream.fromXML(in);
		in.close();
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='p_proname' id='p_proname'>");
		for (Project p : chart.getProjects())
		{
			if (name.equals(p.getName()))
			{// 获得查询方案,设置选中
				sb.append("<option value='" + p.getName() + "' selected>"
						+ p.getTitle() + "</option>");
			}else{
				sb.append("<option value='" + p.getName() + "'>" + p.getTitle()
						+ "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * 检测查询方案名称是否重复
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkChartName(String name, DataSet ds)
			throws DocumentException
	{
		File file = this.getFile(ds);
		if (file.exists())
		{
			Document doc = new SAXReader().read(file);// 加载所有page页面
			List<Node> node = doc.selectNodes("/chart/projects/project/name");
			for (Node n : node)
			{
				if (name.equals(n.getText()))
				{// 查询方案重复
					return false;
				}
			}
		}
		else
		{// 文件不存在
			return true;
		}
		return true;
	}

}
