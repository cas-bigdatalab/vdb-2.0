package vdb.report.resstat.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import vdb.mydb.VdbManager;
import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.dbstats.vo.comparator.EntityBytesComparator;
import vdb.report.resstat.dbstats.vo.comparator.EntityRecordNumComparator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;
import vdb.report.resstat.vo.DatasetIndicator;
import vdb.report.util.ColorUtil;
import vdb.tool.generic.FormatTool;

/**
 * 将数据质量统计指标解析为字符串，供页面Chart组件渲染
 * 
 * @author 苏贤明
 * 
 */
public class IndicatorToStringUtil
{
	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体记录数的柱状图和拆线图字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getHistogramLineTextofEntityRecords(
			DbIndicator dbIndicator, String space)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer recordNumText = new StringBuffer();
		recordNumText.append("<chart><series>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<chart></chart>";
		}

		int dsinSize = dbIndicator.getEntityIndicatorList().size();
		for (int i = 0; i < dsinSize; i++)
		{
			recordNumText.append("<value xid='" + i + "'>"
					+ dbIndicator.getEntityIndicatorList().get(i).getTitle()
					+ "</value>");
		}
		recordNumText.append("</series><graphs><graph gid='0'>");

		for (int i = 0; i < dsinSize; i++)
		{
			recordNumText.append("<value xid='"
					+ i
					+ "'"
					+ " color='"
					+ color.getNextColor()
					+ "'>"
					+ dbIndicator.getEntityIndicatorList().get(i)
							.getRecordNum() + "</value>");
		}
		recordNumText.append(" </graph></graphs></chart>");

		return recordNumText.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体记录数的饼图字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getPieTextofEntityRecords(DbIndicator dbIndicator,
			String space)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer recordNumAmpieText = new StringBuffer();
		recordNumAmpieText.append("<pie>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<pie></pie>";
		}
		// 按利用率从大到小排序
		List<EntityIndicator> list = dbIndicator.getEntityIndicatorList();
		Collections.sort(list, Collections
				.reverseOrder(new EntityRecordNumComparator()));

		int num = list.size() >= 11 ? 11 : list.size();

		for (int i = 0; i < num; i++)
		{
			recordNumAmpieText.append("<slice title='" + list.get(i).getTitle()
					+ "' color='" + color.getNextColor() + "'>"
					+ list.get(i).getRecordNum() + "</slice>");

		}

		// 大于11条，以“其他”显示
		if (list.size() > 11)
		{
			String title = "其他";
			int recordNum = 0;
			for (int i = 11; i < list.size(); i++)
			{
				recordNum += list.get(i).getRecordNum();
			}
			recordNumAmpieText.append("<slice title='" + title + "' color='"
					+ color.getNextColor() + "'>" + recordNum + "</slice>");
		}
		recordNumAmpieText.append("</pie>");
		return recordNumAmpieText.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体利用率的柱状图和拆线图字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getHistogramLineTextOfEntityUtilization(
			DbIndicator dbIndicator, String space)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer usedRateText = new StringBuffer();
		usedRateText.append("<chart><series>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<chart></chart>";
		}

		int dsinSize = dbIndicator.getEntityIndicatorList().size();
		for (int i = 0; i < dsinSize; i++)
		{
			usedRateText.append("<value xid='" + i + "'>"
					+ dbIndicator.getEntityIndicatorList().get(i).getTitle()
					+ "</value>");
		}
		usedRateText.append("</series><graphs><graph gid='0'>");

		for (int i = 0; i < dsinSize; i++)
		{
			usedRateText.append("<value xid='"
					+ i
					+ "'"
					+ " color='"
					+ color.getNextColor()
					+ "'>"
					+ 100
					* dbIndicator.getEntityIndicatorList().get(i)
							.getIntegrityRate() + "</value>");
		}
		usedRateText.append(" </graph></graphs></chart>");

		return usedRateText.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体所占字节数的柱状图和拆线图字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getHistogramLineTextOfEntityBytes(
			DbIndicator dbIndicator, String space)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer sizeText = new StringBuffer();
		sizeText.append("<chart><series>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<chart></chart>";
		}

		int dsinSize = dbIndicator.getEntityIndicatorList().size();
		for (int i = 0; i < dsinSize; i++)
		{
			if (dbIndicator.getEntityIndicatorList().get(i).getBytes() != -1)
			{
				sizeText.append("<value xid='"
						+ i
						+ "'>"
						+ dbIndicator.getEntityIndicatorList().get(i)
								.getTitle() + "</value>");
			}
		}
		sizeText.append("</series><graphs><graph gid='0'>");

		for (int i = 0; i < dsinSize; i++)
		{
			if (dbIndicator.getEntityIndicatorList().get(i).getBytes() != -1)
			{
				sizeText.append("<value xid='"
						+ i
						+ "'"
						+ " color='"
						+ color.getNextColor()
						+ "'>"
						+ dbIndicator.getEntityIndicatorList().get(i)
								.getBytes() + "</value>");
			}
		}
		sizeText.append(" </graph></graphs></chart>");
		return sizeText.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体所占字节数的饼状字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getPieTextOfEntityBytes(DbIndicator dbIndicator,
			String space)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer sizeTextPie = new StringBuffer();
		sizeTextPie.append("<pie>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<pie></pie>";
		}

		// 按利用率从大到小排序
		List<EntityIndicator> list = dbIndicator.getEntityIndicatorList();
		Collections.sort(list, Collections
				.reverseOrder(new EntityBytesComparator()));

		int num = list.size() >= 11 ? 11 : list.size();

		for (int i = 0; i < num; i++)
		{
			if (list.get(i).getBytes() != -1)
			{
				sizeTextPie.append("<slice title='" + list.get(i).getTitle()
						+ "' color='" + color.getNextColor() + "'>"
						+ list.get(i).getBytes() + "</slice>");
			}

		}

		// 大于11条，以“其他”显示
		if (list.size() > 11)
		{
			String title = "其他";
			double volumn = 0;
			for (int i = 11; i < list.size(); i++)
			{
				if (list.get(i).getBytes() != -1)
				{
					volumn += list.get(i).getBytes();
				}
			}
			sizeTextPie.append("<slice title='" + title + "' color='"
					+ color.getNextColor() + "'>" + volumn + "</slice>");
		}

		sizeTextPie.append("</pie>");
		return sizeTextPie.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内指定实体所有字段利用率的柱状图字符串
	 * 
	 * @param dbIndicator
	 *            数据质量统计指标
	 * @param id
	 *            实体ID
	 * @return
	 */
	public static String getHistogramTextOfEntityFieldUsedRate(
			DbIndicator dbIndicator, String id)
	{
		ColorUtil color = new ColorUtil();
		StringBuffer fieldUsedRateText = new StringBuffer();
		fieldUsedRateText.append("<chart><series>");

		// 判断数据质量统计指标是否为空
		if (dbIndicator == null || dbIndicator.getEntityIndicatorList() == null
				|| dbIndicator.getEntityIndicatorList().size() == 0)
		{
			return "<chart></chart>";
		}

		int dsinSize = dbIndicator.getEntityIndicatorList().size();
		for (int i = 0; i < dsinSize; i++)
		{
			if (id.equals(dbIndicator.getEntityIndicatorList().get(i).getId()))
			{
				int size = dbIndicator.getEntityIndicatorList().get(i)
						.getFieldIndicatorList().size();
				for (int j = 0; j < size; j++)
				{
					fieldUsedRateText.append("<value xid='"
							+ j
							+ "'>"
							+ dbIndicator.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(j).getTitle()
							+ "</value>");
				}
				fieldUsedRateText.append("</series><graphs><graph gid='0'>");

				for (int x = 0; x < size; x++)
				{
					fieldUsedRateText.append("<value xid='"
							+ x
							+ "'"
							+ " color='"
							+ color.getNextColor()
							+ "'>"
							+ 100
							* dbIndicator.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(x)
									.getIntegrityRate() + "</value>");
				}

				fieldUsedRateText.append(" </graph></graphs></chart>");

				break;
			}
		}
		return fieldUsedRateText.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体的基本信息（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getTableTextofEntityData(DatasetIndicator dsin,
			String space)
	{
		FormatTool formatTool = new FormatTool();
		StringBuffer tableData = new StringBuffer();
		tableData.append("<rows>");

		DbIndicator dbIndicator = null;
		List<RepositoryIndicator> riList = null;
		// 判断数据质量统计指标是否为空
		if (dsin == null)
		{
			return "<rows></rows>";
		}
		else
		{
			dbIndicator = dsin.getDbIndicator();
			riList = dsin.getRepositoryIndicatorList();
		}

		// 如果数据库指标为空，则不显示其指标
		if (dbIndicator != null)
		{
			// 得到集合的长度
			int dsinSize = dbIndicator.getEntityIndicatorList().size();
			// 空间利用率用变量
			String doublevar = null;
			int usedrate = 10000;

			for (int i = 0; i < dsinSize; i++)
			{
				// 序号
				tableData.append("<row id='" + (i + 1) + "'>" + "<cell>"
						+ (i + 1) + "</cell>");
				// 表名称
				tableData.append("<cell>"
						+ dbIndicator.getEntityIndicatorList().get(i)
								.getTitle() + "</cell>");
				// 大小(KB)
				if (dbIndicator.getEntityIndicatorList().get(i).getBytes() == -1)
				{
					tableData.append("<cell>--无法提取--</cell>");
				}
				else
				{
					tableData.append("<cell>"
							+ formatTool
									.formatBytes(dbIndicator
											.getEntityIndicatorList().get(i)
											.getBytes()) + "</cell>");
				}
				// 记录数
				tableData.append("<cell>"
						+ dbIndicator.getEntityIndicatorList().get(i)
								.getRecordNum() + "</cell>");
				// 空间利用率
				doublevar = toString(Double.valueOf(Math.round(usedrate
						* dbIndicator.getEntityIndicatorList().get(i)
								.getIntegrityRate())
						/ (double) 100));
				tableData.append("<cell>" + doublevar + "%" + "</cell>");
				// 统计日期
				tableData.append("<cell>"
						+ dbIndicator.getEntityIndicatorList().get(i)
								.getLastStatsTime() + "</cell></row>");
			}

			// 数据库空间大小
			dsinSize++;
			tableData.append("<row id='" + dsinSize + "'>");
			tableData.append("<cell colspan='2'>" + "关系型数据量"
					+ "</cell><cell></cell>");
			if (dbIndicator.getBytes() >= 0)
			{
				tableData.append("<cell colspan='4'>"
						+ formatTool.formatBytes(dbIndicator.getBytes())
						+ "</cell>");
				tableData
						.append("<cell></cell><cell></cell><cell></cell></row>");
			}
			else
			{
				tableData.append("<cell colspan='4'>--无法提取--</cell>");
				tableData
						.append("<cell></cell><cell></cell><cell></cell></row>");
			}

			// 文件大小
			long fileBytes = 0;
			long fileItems = 0;
			RepositoryIndicator ri;
			if (riList != null && riList.size() != 0)
			{
				for (int i = 0; i < riList.size(); i++)
				{
					ri = riList.get(i);
					fileBytes = fileBytes + ri.getBytes();
					fileItems = fileItems + ri.getItems();
				}
			}
			dsinSize++;
			tableData.append("<row id='" + dsinSize + "'>");
			tableData.append("<cell colspan='2'>" + "文件型数据量"
					+ "</cell><cell></cell>");
			if (dbIndicator.getBytes() >= 0)
			{
				tableData.append("<cell colspan='4'>"
						+ formatTool.formatBytes(fileBytes) + "</cell>");
				tableData
						.append("<cell></cell><cell></cell><cell></cell></row>");
			}
			else
			{
				tableData.append("<cell colspan='4'>--无法提取--</cell>");
				tableData
						.append("<cell></cell><cell></cell><cell></cell></row>");
			}

			// 文件个数
			dsinSize++;
			tableData.append("<row id='" + dsinSize + "'>");
			tableData.append("<cell colspan='2'>" + "文件个数"
					+ "</cell><cell></cell>");
			tableData.append("<cell colspan='4'>" + fileItems + "</cell>");
			tableData.append("<cell></cell><cell></cell><cell></cell></row>");

			// 总数据量
			dsinSize++;
			tableData.append("<row id='" + dsinSize + "'>");
			tableData.append("<cell colspan='2'>" + "总数据量"
					+ "</cell><cell></cell>");
			tableData.append("<cell colspan='4'>"
					+ formatTool
							.formatBytes(fileBytes + dbIndicator.getBytes())
					+ "</cell>");
			tableData.append("<cell></cell><cell></cell><cell></cell></row>");
		}

		tableData.append("</rows>");
		return tableData.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内指定实体所有字段的基本信息（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param id
	 *            实体ID
	 * @return
	 */
	public static String getTableTextOfEntityFieldValue(DbIndicator dsin,
			String id)
	{
		StringBuffer utilization = new StringBuffer();
		utilization.append("<rows>");

		// 判断数据质量统计指标是否为空
		if (dsin == null || dsin.getEntityIndicatorList() == null
				|| dsin.getEntityIndicatorList().size() == 0)
		{
			return "<rows></rows>";
		}

		// 获得集合的长度
		int dsinSize = dsin.getEntityIndicatorList().size();
		// 空间利用率用变量
		String doublevar = null;
		int usedrate = 10000;

		for (int i = 0; i < dsinSize; i++)
		{
			if (id.equals(dsin.getEntityIndicatorList().get(i).getId()))
			{
				int row = 0;
				int size = dsin.getEntityIndicatorList().get(i)
						.getFieldIndicatorList().size();
				for (int j = 0; j < size; j++)
				{
					row++;
					utilization.append("<row id='" + row + "'>");
					// 实体名称
					utilization.append("<cell>"
							+ dsin.getEntityIndicatorList().get(i).getTitle()
							+ "</cell>");
					// 属性名称
					utilization.append("<cell>"
							+ dsin.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(j).getTitle()
							+ "</cell>");
					// 库中字段利用率
					doublevar = toString(Double.valueOf(Math.round(usedrate
							* dsin.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(j)
									.getIntegrityRate())
							/ (double) 100));
					utilization.append("<cell>" + doublevar + "%"
							+ "</cell></row>");
				}
				break;
			}
		}
		utilization.append("</rows>");
		return utilization.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内指定实体所有字段的利用率（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param id
	 *            实体ID
	 * @return
	 */
	public static String getTableTextofFieldUsedRate(DbIndicator dsin, String id)
	{
		StringBuffer utilization = new StringBuffer();
		utilization.append("<rows>");

		// 判断数据质量统计指标是否为空
		if (dsin == null || dsin.getEntityIndicatorList() == null
				|| dsin.getEntityIndicatorList().size() == 0)
		{
			return "<rows></rows>";
		}

		// 获得集合的长度
		int dsinSize = dsin.getEntityIndicatorList().size();
		// 空间利用率用变量
		String doublevar = null;
		int usedrate = 10000;

		for (int i = 0; i < dsinSize; i++)
		{
			if (id.equals(dsin.getEntityIndicatorList().get(i).getId()))
			{
				int row = 0;
				int size = dsin.getEntityIndicatorList().get(i)
						.getFieldIndicatorList().size();
				for (int j = 0; j < size; j++)
				{
					row++;
					utilization.append("<row id='" + row + "'>");
					// 属性名称
					utilization.append("<cell>"
							+ dsin.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(j).getTitle()
							+ "</cell>");
					// 库中字段利用率
					doublevar = toString(Double.valueOf(Math.round(usedrate
							* dsin.getEntityIndicatorList().get(i)
									.getFieldIndicatorList().get(j)
									.getIntegrityRate())
							/ (double) 100));
					utilization.append("<cell>" + doublevar + "%"
							+ "</cell></row>");
				}
				break;
			}
		}
		utilization.append("</rows>");
		return utilization.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体所占的字节数（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getTableTextofEntityBytes(DbIndicator dsin,
			String space)
	{
		FormatTool formatTool = new FormatTool();
		StringBuffer dataVolume = new StringBuffer();
		dataVolume.append("<rows>");

		// 判断数据质量统计指标是否为空
		if (dsin == null || dsin.getEntityIndicatorList() == null
				|| dsin.getEntityIndicatorList().size() == 0)
		{
			return "<rows></rows>";
		}

		// 得到集合的长度
		int dsinSize = dsin.getEntityIndicatorList().size();

		for (int i = 0; i < dsinSize; i++)
		{
			dataVolume.append("<row id='" + (i + 1) + "'>");
			// 表名称
			dataVolume.append("<cell>"
					+ dsin.getEntityIndicatorList().get(i).getTitle()
					+ "</cell>");
			// 大小(KB)
			if (dsin.getEntityIndicatorList().get(i).getBytes() == -1)
			{
				dataVolume.append("<cell>--无法提取--</cell></row>");
			}
			else
			{
				dataVolume.append("<cell>"
						+ formatTool.formatBytes(dsin.getEntityIndicatorList()
								.get(i).getBytes()) + "</cell></row>");
			}
		}
		dataVolume.append("</rows>");
		return dataVolume.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体所占的记录数（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getTableTextofEntityRecords(DbIndicator dsin,
			String space)
	{
		StringBuffer records = new StringBuffer();
		records.append("<rows>");

		// 判断数据质量统计指标是否为空
		if (dsin == null || dsin.getEntityIndicatorList() == null
				|| dsin.getEntityIndicatorList().size() == 0)
		{
			return "<rows></rows>";
		}

		// 得到集合的长度
		int dsinSize = dsin.getEntityIndicatorList().size();

		for (int i = 0; i < dsinSize; i++)
		{
			records.append("<row id='" + (i + 1) + "'>");
			// 表名称
			records.append("<cell>"
					+ dsin.getEntityIndicatorList().get(i).getTitle()
					+ "</cell>");
			// 记录数
			records.append("<cell>"
					+ dsin.getEntityIndicatorList().get(i).getRecordNum()
					+ "</cell></row>");
		}
		records.append("</rows>");
		return records.toString();
	}

	/**
	 * 根据数据集数据质量统计指标得到数据集内所有实体的利用率（页面Chart组件将返回的字符串解析为表格）
	 * 
	 * @param dsin
	 *            数据质量统计指标
	 * @param space
	 * @return
	 */
	public static String getTableTextOfEntityUtilization(DbIndicator dsin,
			String space)
	{
		StringBuffer utilization = new StringBuffer();
		utilization.append("<rows>");

		// 判断数据质量统计指标是否为空
		if (dsin == null || dsin.getEntityIndicatorList() == null
				|| dsin.getEntityIndicatorList().size() == 0)
		{
			return "<rows></rows>";
		}

		// 得到集合的长度
		int dsinSize = dsin.getEntityIndicatorList().size();
		// 空间利用率用变量
		String doublevar = null;
		int usedrate = 10000;

		for (int i = 0; i < dsinSize; i++)
		{
			utilization.append("<row id='" + (i + 1) + "'>");
			// 表名称
			utilization.append("<cell>"
					+ dsin.getEntityIndicatorList().get(i).getTitle()
					+ "</cell>");
			// 空间利用率
			doublevar = toString(Double.valueOf(Math.round(usedrate
					* dsin.getEntityIndicatorList().get(i).getIntegrityRate())
					/ (double) 100));
			utilization.append("<cell>" + doublevar + "%" + "</cell></row>");
		}
		utilization.append("</rows>");
		return utilization.toString();
	}

	/**
	 * 根据数据集的URI得到云图字符串
	 * 
	 * @param dsuri
	 *            数据集的URI
	 * @return
	 */
	public static String getDataSetCloud(String dsuri)
	{
		String path = VdbManager.getEngine().getServletContext().getRealPath(
				"/WEB-INF/vdb/model/" + dsuri);
		path = path + "/reports";

		File f = new File(path);
		if (!f.exists())
			f.mkdirs();

		f = new File(path + "/cloud.xml");
		if (!f.exists())
			return "";

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(f);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		DOMSource ds = new DOMSource(doc);

		// 得到转换器工厂类的实例
		TransformerFactory tf = TransformerFactory.newInstance();
		StringWriter sw = new StringWriter();
		;
		try
		{
			StreamResult sr = new StreamResult(sw);
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(ds, sr);
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}

		String outputString = sw.toString();
		int begin = outputString.indexOf("<chart>");
		int end = outputString.length();
		outputString = outputString.substring(begin, end);
		return outputString.replaceAll("\"", "\'");
	}

	/**
	 * 将double类型数字转换为字符串
	 * 
	 * @param dou
	 *            数字
	 * @return
	 */
	private static String toString(double dou)
	{
		BigDecimal bigdecimal = new BigDecimal(dou);
		BigDecimal var = bigdecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
		return var.toString();
	}
}