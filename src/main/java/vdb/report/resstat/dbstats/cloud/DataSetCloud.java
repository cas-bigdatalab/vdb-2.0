package vdb.report.resstat.dbstats.cloud;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.report.resstat.dbstats.cloud.comparator.EntityAsscosiateNumComparator;
import vdb.report.resstat.dbstats.cloud.comparator.EntitySizeComparator;
import vdb.report.resstat.dbstats.cloud.comparator.EntityXComparator;
import vdb.report.resstat.dbstats.cloud.comparator.EntityYComparator;
import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.util.ColorUtil;
import vdb.tool.generic.FormatTool;

public class DataSetCloud
{

	/** 数据集实体个数 */
	private DataSet _dataSet;

	/** 数据集统计指标，用于获取实体的数据量 */
	private DbIndicator _dsin;

	/** 数据集的实体圆圈列表 */
	private List<EntityCircle> _entityCircleList;

	/** 实体圆圈间的最小间距 */
	private double _minDistance = 40;

	/** 最内层圆圈个数 */
	private int _innerCircleNum = 2;

	/** 外层依次增加的圆圈个数 */
	private int _circleNumIncrement = 3;

	/** 实体圆圈的最小半径 */
	private double _minCircleRadius = 20;

	/** XY范围的中心位置 */
	private double _x0;

	/** XY范围的中心位置 */
	private double _y0;

	/** 客户端的最小X坐标 */
	private double _minX = 0;

	/** 客户端的最小Y坐标 */
	private double _minY = 0;

	/** 客户端的最大X坐标 */
	private double _maxX = 2000;

	/** 客户端的最大Y坐标 */
	private double _maxY = 1400;

	/** 已经确定位置的实体圆圈的列表 */
	private List<EntityCircle> _havePlacedEntityCircleList;

	/** 布局层的半径和相应布局层最大实体圆圈的半径 */
	private List<Double[]> _layerRadius;

	/**
	 * 构造方法
	 * 
	 * @param dataSet
	 *            数据集
	 * @param dsin
	 *            数据集统计指标
	 */
	public DataSetCloud(DataSet dataSet, DbIndicator dsin)
	{
		_dataSet = dataSet;
		_dsin = dsin;
		_x0 = (_maxX - _minX) / 2;
		_y0 = (_maxY - _minY) / 2;
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param dataSet
	 *            数据集
	 * @param dsin
	 *            数据集统计指标
	 * @param distance
	 *            实体间的最短距离
	 * @param innerCircleNum
	 *            最内层的实体个数
	 * @param circleNumIncrement
	 *            外层依次增加的实体个数
	 * @param minCircleRadius
	 *            云图中最小的实体圆（即数据量为0时）
	 */
	public DataSetCloud(DataSet dataSet, DbIndicator dsin, double distance,
			int innerCircleNum, int circleNumIncrement, double minCircleRadius)
	{
		this(dataSet, dsin);
		_minDistance = distance;
		_innerCircleNum = innerCircleNum;
		_circleNumIncrement = circleNumIncrement;
		_minCircleRadius = minCircleRadius;
	}

	/**
	 * 构造方法
	 * 
	 * @param dataSet
	 *            数据集
	 * @param dsin
	 *            数据集统计指标
	 * @param distance
	 *            实体间的最短距离
	 * @param innerCircleNum
	 *            最内层的实体个数
	 * @param circleNumIncrement
	 *            外层依次增加的实体个数
	 * @param minCircleRadius
	 *            云图中最小的实体圆（即数据量为0时）
	 * @param minx
	 *            客户端的最小X坐标
	 * @param miny
	 *            客户端的最小Y坐标
	 * @param maxx
	 *            客户端的最大X坐标
	 * @param maxy
	 *            客户端的最大Y坐标
	 */
	public DataSetCloud(DataSet dataSet, DbIndicator dsin, double distance,
			int innerCircleNum, int circleNumIncrement, double minCircleRadius,
			double minx, double miny, double maxx, double maxy)
	{
		this(dataSet, dsin);
		_minDistance = distance;
		_innerCircleNum = innerCircleNum;
		_circleNumIncrement = circleNumIncrement;
		_minCircleRadius = minCircleRadius;
		_minX = minx;
		_minY = miny;
		_maxX = maxx;
		_maxY = maxy;
		_x0 = (_maxX - _minX) / 2;
		_y0 = (_maxY - _minY) / 2;
	}

	/**
	 * 生成数据集的云图
	 * 
	 * @return 返回云图的Document文档
	 */
	public String generateCloud()
	{
		Element rootElement = new Element("chart");
		Document doc = new Document(rootElement);

		if (_dataSet == null || _entityCircleList == null
				|| _entityCircleList.size() == 0)
		{
			return doc.toString();
		}
		else
		{
			// 计算布局的层数
			int layerNum = 0;
			int entityNum = _innerCircleNum;
			if (entityNum > _entityCircleList.size())
				layerNum = 1;
			else
			{
				layerNum++;
				while (entityNum < _entityCircleList.size())
				{
					entityNum = entityNum + _innerCircleNum + layerNum
							* _circleNumIncrement;
					layerNum++;
				}
			}

			for (int i = 0; i < layerNum; i++)
			{
				if (i == 0)
				{
					// 得到最内层的数据实体圆圈列表
					// 注：这里innerList和_entityCircleList为两个不同的引用，但其具体的值(如get(0))却指向同一个引用
					List<EntityCircle> innerList = getSubEntityCircleList(
							_entityCircleList, 0, _innerCircleNum);

					// 将最内层圆圈实体列表按照数据量从大到小排序
					Collections.sort(innerList, Collections
							.reverseOrder(new EntitySizeComparator()));

					if (innerList.size() == 1)
					{
						innerList.get(0).setX(_x0);
						innerList.get(0).setY(_y0);
						Double[] d = new Double[2];
						d[0] = 0d;
						d[1] = innerList.get(0).getR();
						_layerRadius.add(d);
					}
					else
					{
						double innerRadius = computeInnerRadius(innerList);
						Double[] d = new Double[2];
						d[0] = new Double(innerRadius);
						d[1] = innerList.get(0).getR();
						_layerRadius.add(d);
						setInnerEntityCirclePosition(innerList, innerRadius);
					}
					_havePlacedEntityCircleList.addAll(innerList);
				}
				else
				{
					List<EntityCircle> notPlacedEntityCircleList = getNotPlacedEntityCircleList();
					// 根据与已经确定好坐标位置的实体圆圈的列表的关联个数来对未确定好坐标位置的实体圆圈列表进行排序
					sortNotPlacedEntityCircleList(notPlacedEntityCircleList);

					int begin = 0;
					int end = _innerCircleNum + i * _circleNumIncrement;

					List<EntityCircle> list = getSubEntityCircleList(
							notPlacedEntityCircleList, begin, end);

					Collections.sort(list, Collections
							.reverseOrder(new EntitySizeComparator()));

					double layerRadius = _layerRadius.get(i - 1)[0]
							+ _layerRadius.get(i - 1)[1] + _minDistance
							+ list.get(0).getR();

					// 判断在此半径条件下，此层的实体圆圈是否会重叠，如果重叠，则增加层半径
					while (Math.sin(Math.PI / end) * layerRadius < ((2 * list
							.get(0).getR() + _minDistance) / 2))
					{
						layerRadius = layerRadius + _minDistance / 3;
					}

					Double[] d = new Double[2];
					d[0] = new Double(layerRadius);
					d[1] = list.get(0).getR();
					_layerRadius.add(d);

					setOutterEntityCirclePosition(list, layerRadius,
							_layerRadius.get(i - 1)[0], end
									- _circleNumIncrement);

					_havePlacedEntityCircleList.addAll(list);
				}
			}

			// 坐标位置都确定完毕后，查看所有实体圆圈的坐标位置是否超过XY范围，如果超过或者剩余很多，同比例放大或者缩小
			// adjustPositions(); //调整坐标的过程放到Flash解析时实现
			createDocument(doc);

			XMLOutputter outputter = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			format.setEncoding("UTF-8");
			outputter.setFormat(format);

			try
			{
				String path = VdbManager.getEngine().getServletContext()
						.getRealPath("/WEB-INF/vdb/model/" + _dataSet.getUri());
				path = path + "/reports";
				File dir = new File(path);

				if (!dir.exists())
					dir.mkdirs();

				File f = new File(path + "/" + "cloud.xml");

				outputter.output(doc, new FileOutputStream(f));

			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			String outputString = outputter.outputString(doc);
			int begin = outputString.indexOf("<chart>");
			int end = outputString.length();
			outputString = outputString.substring(begin, end);
			return outputString.replaceAll("\"", "\'");

		}
	}

	/**
	 * 构造方法调用的初始化方法
	 */
	private void init()
	{
		_entityCircleList = new ArrayList<EntityCircle>();
		_havePlacedEntityCircleList = new ArrayList<EntityCircle>();
		_layerRadius = new ArrayList<Double[]>();

		Entity[] entities = _dataSet.getEntities();
		EntityCircle ec = null;
		for (Entity entity : entities)
		{
			ec = new EntityCircle(entity);
			// 获得实体数据量的大小，并给实体圆圈赋值
			for (EntityIndicator ei : _dsin.getEntityIndicatorList())
			{
				if (ei.getTableName().equals(entity.getTableName()))
				{
					ec.setDataSize(ei.getBytes());
					break;
				}
			}
			// 获得与此实体关联的数据实体的个数
			ec.setAsscociateNum(entity.getReferenceKeys().length
					+ entity.getCollectionKeys().length);
			_entityCircleList.add(ec);
		}

		// 根据数据量的最大、最小值计算半径
		List<EntityCircle> ecList = getSubEntityCircleList(_entityCircleList,
				0, _entityCircleList.size());
		Collections.sort(ecList, Collections
				.reverseOrder(new EntitySizeComparator()));
		computeEntityCircleRadius(ecList);

		// 将数据实体圆圈列表按照关联个数降序排列
		Collections.sort(_entityCircleList, Collections
				.reverseOrder(new EntityAsscosiateNumComparator()));
	}

	/**
	 * 根据数据量大小计算实体圆圈的半径
	 * 
	 * @param ecList
	 *            数据实体列表
	 */
	private void computeEntityCircleRadius(List<EntityCircle> ecList)
	{
		// 找到最大数据量
		double maxsize = ecList.get(0).getDataSize();
		double minsize = ecList.get(ecList.size() - 1).getDataSize();
		double selfsize;
		for (EntityCircle ec : ecList)
		{
			selfsize = ec.getDataSize();
			ec.setR(computeCircleRadius(minsize, maxsize, selfsize));
		}
	}

	/**
	 * 根据数据集数据实体的最大、最小数据量计算实体圆圈的半径
	 * 
	 * @param 数据集中的最大数据量
	 * @param 数据集中的最小数据量
	 * @return 半径
	 */
	private double computeCircleRadius(double minsize, double maxsize,
			double selfsize)
	{
		// 数据集中所有圆圈的差距不会超过2倍

		if (selfsize == minsize)
			return _minCircleRadius;
		else if (selfsize == maxsize)
			return 2 * _minCircleRadius;
		else if (maxsize == minsize)
			return _minCircleRadius;
		else
		{
			double temp = ((Math.PI / 2) / (maxsize - minsize))
					* (selfsize - minsize);
			return _minCircleRadius + _minCircleRadius * Math.sin(temp);
		}
	}

	/**
	 * 获得指定列表、指定位置的子列表
	 * 
	 * @param begin
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return
	 */
	private List<EntityCircle> getSubEntityCircleList(List<EntityCircle> list,
			int begin, int end)
	{
		List<EntityCircle> enList = new ArrayList<EntityCircle>();
		if (end > list.size())
			end = list.size();
		for (int i = begin; i < end; i++)
		{
			enList.add(list.get(i));
		}
		return enList;
	}

	/**
	 * 计算云图最内层的布局半径
	 * 
	 * @param list
	 *            最内层的实体圆圈列表
	 * @return 最内层的布局半径
	 */
	private double computeInnerRadius(List<EntityCircle> list)
	{
		// 获得最内层实体圆圈的最大半径(第一个元素)
		double maxEntityRadius = list.get(0).getR();

		int num = list.size();

		if (num == 0)
			return 0;

		// 计算两个实体圆圈圆心间的最小距离
		double distance = 2 * maxEntityRadius + _minDistance;

		return distance / (2 * (Math.sin(Math.PI / num)));
	}

	/**
	 * 设定内层（第一层）实体圆圈列表的位置
	 * 
	 * @param list
	 *            实体圈圈列表
	 * @param r
	 *            内层的半径
	 */
	private void setInnerEntityCirclePosition(List<EntityCircle> list, double r)
	{
		list.get(0).setX(_x0);
		list.get(0).setY(_y0 - r);

		int num = list.size();
		double tempX, tempY, judge;
		for (int i = 1; i < num; i++)
		{
			tempX = list.get(i - 1).getX();
			tempY = list.get(i - 1).getY();

			judge = (tempX - _x0) / r;
			if (judge < -1)
				judge = -1;
			else if (judge > 1)
				judge = 1;

			if ((tempY - _y0) <= 0)
			{
				list.get(i).setX(
						_x0
								+ r
								* Math.cos((Math.acos(judge) - (2 * Math.PI)
										/ num)));
			}
			else
			{
				list.get(i).setX(
						_x0
								+ r
								* Math.cos((Math.acos(judge) + (2 * Math.PI)
										/ num)));
			}

			judge = (tempY - _y0) / r;
			if (judge < -1)
				judge = -1;
			else if (judge > 1)
				judge = 1;
			if ((tempX - _x0) >= 0)
			{
				list.get(i).setY(
						_y0
								+ r
								* Math.sin((Math.asin(judge) + (2 * Math.PI)
										/ num)));
			}
			else
			{
				list.get(i).setY(
						_y0
								+ r
								* Math.sin((Math.asin(judge) - (2 * Math.PI)
										/ num)));
			}
		}
	}

	/**
	 * 获得未确定坐标位置的实体圆圈列表
	 * 
	 * @return
	 */
	private List<EntityCircle> getNotPlacedEntityCircleList()
	{
		List<EntityCircle> notPlacedEntityCircleList = new ArrayList<EntityCircle>();

		for (EntityCircle ec : _entityCircleList)
		{
			if (!_havePlacedEntityCircleList.contains(ec))
				notPlacedEntityCircleList.add(ec);
		}
		return notPlacedEntityCircleList;
	}

	/**
	 * 冒泡法排序，也可采用其他算法 根据与已经确定好坐标位置的实体圆圈的列表的关联个数来对未确定好坐标位置的实体圆圈列表进行排序
	 * 
	 * @param list
	 *            未确定好坐标位置的实体圆圈列表
	 */
	private void sortNotPlacedEntityCircleList(List<EntityCircle> list)
	{
		EntityCircle temp;
		for (int i = 0; i < list.size() - 1; i++)
		{
			for (int j = i + 1; j < list.size(); j++)
			{
				int asscosiateNum_i, asscosiateNum_j;
				List<EntityCircle> tempList = getAssociateEntityWithHavePlacedEntityCircleList(list
						.get(i));
				if (tempList.size() == 2 && tempList.get(1) == null)
				{
					asscosiateNum_i = 0;
				}
				else
				{
					asscosiateNum_i = tempList.size();
				}

				tempList = getAssociateEntityWithHavePlacedEntityCircleList(list
						.get(j));
				if (tempList.size() == 2 && tempList.get(1) == null)
				{
					asscosiateNum_j = 0;
				}
				else
				{
					asscosiateNum_j = tempList.size();
				}
				if (asscosiateNum_i < asscosiateNum_j)
				{
					temp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, temp);
				}
			}
		}
	}

	/**
	 * 获得指定实体圆圈与已经确定好坐标位置的实体圆圈的列表的关联实体圆圈
	 * 
	 * @param ec
	 * @return
	 */
	private List<EntityCircle> getAssociateEntityWithHavePlacedEntityCircleList(
			EntityCircle ec)
	{
		List<EntityCircle> list = new ArrayList<EntityCircle>();

		List<String> havePlacedTableNameList = new ArrayList<String>();
		Map<String, EntityCircle> map = new HashMap<String, EntityCircle>();

		for (EntityCircle circle : _havePlacedEntityCircleList)
		{
			havePlacedTableNameList.add(circle.getEntity().getTableName());
			map.put(circle.getEntity().getTableName(), circle);
		}
		RelationKey[] relationkeys = ec.getEntity().getReferenceKeys();
		RelationKey[] collectionkeys = ec.getEntity().getCollectionKeys();
		for (RelationKey rk : relationkeys)
		{
			if (havePlacedTableNameList.contains(rk.getTarget().getTableName()))
				list.add(map.get(rk.getTarget().getTableName()));
		}
		for (RelationKey rk : collectionkeys)
		{
			if (havePlacedTableNameList.contains(rk.getTarget().getTableName()))
				list.add(map.get(rk.getTarget().getTableName()));
		}
		// 如果指定圆圈与任何已经确定好坐标位置的实体圆圈均不关联，则将最后一个确定好坐标位置的圆圈添加到返回列表中
		if (list.size() == 0)
		{
			list.add(_havePlacedEntityCircleList
					.get(_havePlacedEntityCircleList.size() - 1));
			list.add(null);// 用于标识此list为没有任何关联关系的list
		}
		return list;
	}

	/**
	 * 设定外层实体圆圈列表的位置
	 * 
	 * @param list
	 *            外层实体圆圈列表
	 * @param r
	 *            外层半径
	 * @param preR
	 *            上一层半径
	 * @param preLayerEntityNum
	 *            上一层实体圈圈的个数
	 */
	private void setOutterEntityCirclePosition(List<EntityCircle> list,
			double r, double preR, int preLayerEntityNum)
	{
		double tempX, tempY, x = 0, y = 0, judge;
		int num = list.size();
		List<Double[]> dotList = new ArrayList<Double[]>();
		Double[] d;
		for (int i = 0; i < num; i++)
		{
			EntityCircle ec = list.get(i);

			if (i == 0)
			{
				List<EntityCircle> assList = getAssociateEntityWithHavePlacedEntityCircleList(ec);
				// 如果与内层的已经确定坐标位置的多个实体圆圈关联，则取第一个实体圈圈（A），即将此实体放在实体圈圈（A）的旁边
				tempX = assList.get(0).getX();
				tempY = assList.get(0).getY();

				if (tempX - _x0 == 0 && tempY - _y0 == 0)
				{
					judge = 0;
					list.get(i).setX(_x0);
				}
				else
				{
					judge = (tempX - _x0) / preR;
					if (judge < -1)
						judge = -1;
					else if (judge > 1)
						judge = 1;

					if ((tempY - _y0) <= 0)
					{
						list.get(i).setX(
								_x0
										+ r
										* Math.cos((Math.acos(judge) - Math.PI
												/ preLayerEntityNum)));
					}
					else
					{
						list.get(i).setX(
								_x0
										+ r
										* Math.cos((Math.acos(judge) + Math.PI
												/ preLayerEntityNum)));
					}
				}

				if ((tempX - _x0) == 0 && tempY - _y0 == 0)
				{
					judge = 0;
					list.get(i).setY(_y0 - r);
				}
				else
				{
					judge = (tempY - _y0) / preR;
					if (judge < -1)
						judge = -1;
					else if (judge > 1)
						judge = 1;
					if ((tempX - _x0) >= 0)
					{
						list.get(i).setY(
								_y0
										+ r
										* Math.sin((Math.asin(judge) + Math.PI
												/ preLayerEntityNum)));
					}
					else
					{
						list.get(i).setY(
								_y0
										+ r
										* Math.sin((Math.asin(judge) - Math.PI
												/ preLayerEntityNum)));
					}
				}
			}
			else
			{
				// 计算出此层其他点的坐标位置
				if (i == 1)
				{
					tempX = list.get(i - 1).getX();
					tempY = list.get(i - 1).getY();
				}
				else
				{
					tempX = x;
					tempY = y;
				}

				judge = (tempX - _x0) / r;
				if (judge < -1)
					judge = -1;
				else if (judge > 1)
					judge = 1;
				if ((tempY - _y0) <= 0)
				{
					x = _x0
							+ r
							* Math
									.cos((Math.acos(judge) - (2 * Math.PI)
											/ num));
				}
				else
				{
					x = _x0
							+ r
							* Math
									.cos((Math.acos(judge) + (2 * Math.PI)
											/ num));
				}

				judge = (tempY - _y0) / r;
				if (judge < -1)
					judge = -1;
				else if (judge > 1)
					judge = 1;
				if ((tempX - _x0) >= 0)
				{
					y = _y0
							+ r
							* Math
									.sin((Math.asin(judge) + (2 * Math.PI)
											/ num));
				}
				else
				{
					y = _y0
							+ r
							* Math
									.sin((Math.asin(judge) - (2 * Math.PI)
											/ num));
				}
				d = new Double[2];
				d[0] = x;
				d[1] = y;
				dotList.add(d);
			}
		}
		// 第一个已经确定好位置，不再考虑
		List<EntityCircle> tempList = getSubEntityCircleList(list, 1, list
				.size());
		// 按关联关系排序
		sortNotPlacedEntityCircleList(tempList);
		num = tempList.size();
		// 再次遍历，确定其他实体圆圈的坐标位置
		for (int i = 0; i < num; i++)
		{
			EntityCircle ec = tempList.get(i);
			List<EntityCircle> assList = getAssociateEntityWithHavePlacedEntityCircleList(ec);
			// 如果第一个的关联个数都为0,则后面的关系个数均为0
			if ((assList.size() == 2) && (assList.get(1) == null))
				assList = null;
			int n = getEntityCirclePosition(assList, dotList).intValue();
			Double[] de = dotList.get(n);
			ec.setX(de[0]);
			ec.setY(de[1]);
			dotList.remove(n);
		}
	}

	/**
	 * 获得实体的坐标位置
	 * 
	 * @param assList
	 *            与某实体关联的实体圆圈列表（列表中的实体已经确定好坐标位置）
	 * @param dotList
	 *            还未分配实体圆圈的坐标点列表
	 */
	private Integer getEntityCirclePosition(List<EntityCircle> assList,
			List<Double[]> dotList)
	{
		// 如果assList为空，则返回第一个点坐标
		if (assList == null)
		{
			return 0;
		}
		double[] distance = new double[dotList.size()];

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (int i = 0; i < dotList.size(); i++)
		{
			for (int j = 0; j < assList.size(); j++)
			{
				double x = assList.get(j).getX();
				double y = assList.get(j).getY();
				distance[i] = distance[i]
						+ Math.sqrt(Math.pow((x - dotList.get(i)[0]), 2)
								+ Math.pow(y - dotList.get(i)[1], 2));
			}
			map.put(new Double(distance[i]).toString(), Integer.valueOf(i));
		}
		Arrays.sort(distance);
		return map.get(new Double(distance[0]).toString());
	}

	/**
	 * 坐标位置都确定完毕后，查看所有实体圆圈的坐标位置是否超过XY范围，如果超过或者剩余很多，同比例放大或者缩小
	 */
	/**
	 * private void adjustPositions(){ Collections.sort(_entityCircleList, new
	 * EntityXComparator()); double minx =
	 * _entityCircleList.get(0).getX()-_entityCircleList.get(0).getR(); double
	 * maxx =
	 * _entityCircleList.get(_entityCircleList.size()-1).getX()+_entityCircleList.get(_entityCircleList.size()-1).getR();
	 * 
	 * Collections.sort(_entityCircleList, new EntityYComparator()); double miny =
	 * _entityCircleList.get(0).getY()-_entityCircleList.get(0).getR(); double
	 * maxy =
	 * _entityCircleList.get(_entityCircleList.size()-1).getY()+_entityCircleList.get(_entityCircleList.size()-1).getR();
	 * 
	 * //假定页面FLASH范围为600*450,预留40的空间 minx= minx+20; miny= miny+20; maxx=
	 * maxx+20; maxy= maxy+20; int x=1000; int y=700; double scalex =
	 * 2*(maxx-_x0)/x > 2*(_x0-minx)/x ? 2*(maxx-_x0)/x:2*(_x0-minx)/x; double
	 * scaley = 2*(maxy-_y0)/y > 2*(_y0-miny)/y ? 2*(maxy-_y0)/y:2*(_y0-miny)/y;
	 * double scale = (scalex > scaley) ? scalex :scaley;
	 * //半径应该缩放的比例是多少？？应该就是X和Y的缩放比例中比较大的 //double scale = (maxx-minx)/(x-40)
	 * >(maxy-miny)/(y-40) ? (maxx-minx)/(x-40) : (maxy-miny)/(y-40);
	 * 
	 * for(EntityCircle enCircle :_entityCircleList){
	 * enCircle.setX((enCircle.getX()-_x0)/scalex + x/2);
	 * enCircle.setY((enCircle.getY()-_y0)/scaley + y/2);
	 * enCircle.setR(enCircle.getR()/scale); } }
	 */
	/**
	 * 根据_entityCircleList的值创建相应的DOM树
	 * 
	 * @param doc
	 */
	private void createDocument(Document doc)
	{
		// 测试结果代码
		// System.out.println("Test Cloud Start......");
		// for(EntityCircle ec :_havePlacedEntityCircleList)
		// {
		// System.out.println("数据实体："+ec.getEntity().getName()+"-->"+ec.getEntity().getTableName()+"-->关联个数："+ec.getAsscociateNum());
		// System.out.println("X坐标："+ec.getX()+"-->"+"Y坐标："+ec.getY()+"-->"+"半径R："+ec.getR());
		//			
		// }
		// System.out.println("Test Cloud End......");

		// 构造DOM树结构
		Collections.sort(_entityCircleList, new EntityXComparator());
		double minx = _entityCircleList.get(0).getX()
				- _entityCircleList.get(0).getR();
		double maxx = _entityCircleList.get(_entityCircleList.size() - 1)
				.getX()
				+ _entityCircleList.get(_entityCircleList.size() - 1).getR();

		Collections.sort(_entityCircleList, new EntityYComparator());
		double miny = _entityCircleList.get(0).getY()
				- _entityCircleList.get(0).getR();
		double maxy = _entityCircleList.get(_entityCircleList.size() - 1)
				.getY()
				+ _entityCircleList.get(_entityCircleList.size() - 1).getR();

		FormatTool formatter = new FormatTool();

		/** 构造DOM树 */
		Element rootElement = doc.getRootElement();

		// 创建extent节点
		Element extent = new Element("extent");
		rootElement.addContent(extent);

		Element minxElement = new Element("minx");
		Element maxxElement = new Element("maxx");
		Element minyElement = new Element("miny");
		Element maxyElement = new Element("maxy");
		Element x0 = new Element("x0");
		Element y0 = new Element("y0");

		minxElement.setText(new Double(minx).toString());
		maxxElement.setText(new Double(maxx).toString());
		minyElement.setText(new Double(miny).toString());
		maxyElement.setText(new Double(maxy).toString());
		x0.setText(new Double(_x0).toString());
		y0.setText(new Double(_y0).toString());

		extent.addContent(minxElement);
		extent.addContent(maxxElement);
		extent.addContent(minyElement);
		extent.addContent(maxyElement);
		extent.addContent(x0);
		extent.addContent(y0);

		// 创建bullets节点
		Element bullets = new Element("bullets");
		rootElement.addContent(bullets);

		Element bullet, x, y, size, items, bytes, color, title, des, num;
		ColorUtil cu = new ColorUtil();// 为圆圈分配颜色
		for (EntityCircle ec : _havePlacedEntityCircleList)
		{
			bullet = new Element("bullet");
			bullet.setAttribute("id", ec.getEntity().getTableName());

			x = new Element("x");
			x.setText(new Double(ec.getX()).toString());

			y = new Element("y");
			y.setText(new Double(ec.getY()).toString());

			size = new Element("size");
			size.setText(new Double(ec.getR() * 2).toString());

			items = new Element("items");
			items.setText("2445");

			bytes = new Element("bytes");
			bytes.setText(new Double(ec.getDataSize()).toString());

			color = new Element("color");
			color.setText(cu.getNextColor());

			title = new Element("title");
			// <![CDATA[记录数:{value}条]]>
			title.setContent(new CDATA(ec.getEntity().getTitle()));

			num = new Element("num");
			num.setText("数据量："
					+ formatter.formatBytes(new Double(ec.getDataSize())));

			des = new Element("des");
			des.setContent(new CDATA(ec.getEntity().getDescription()));

			bullet.addContent(x);
			bullet.addContent(y);
			bullet.addContent(size);
			bullet.addContent(items);
			bullet.addContent(bytes);
			bullet.addContent(color);
			bullet.addContent(title);
			bullet.addContent(num);
			bullet.addContent(des);

			bullets.addContent(bullet);
		}

		// 创建edges节点
		Element edges = new Element("edges");
		rootElement.addContent(edges);

		// 遍历实体的字段
		Entity[] entities = _dataSet.getEntities();
		List<Relation> relations = new ArrayList<Relation>();// 存储字段上的关系
		Map<Relation, Element> relationMapToElement = new HashMap<Relation, Element>();// 关系与TO节点的对应关系
		Relation rel;

		Element edge, from, to, titles;

		for (Entity entity : entities)
		{
			Field[] fields = entity.getFields();
			for (Field field : fields)
			{
				if (field.getRelationKey() != null)
				{
					rel = field.getRelationKey().getRelation();
					if (!relations.contains(rel))
					{// 一个关系如果已经被使用过，则不再添加新的EDGE节点
						edge = new Element("edge");
						from = new Element("from");
						from.setText(field.getEntity().getTableName());
						from.setAttribute("des", field.getTitle());

						to = new Element("to");
						to.setText(field.getRelationKey().getTarget()
								.getTableName());
						relationMapToElement.put(rel, to);

						titles = new Element("titles");
						title = new Element("title");
						title.setText(rel.getTitle());
						titles.addContent(title);

						edge.addContent(from);
						edge.addContent(to);
						edge.addContent(titles);

						edges.addContent(edge);

						// 将关系添加到LIST中
						relations.add(rel);
					}
					else
					{// 一个关系如果被使用过，则取出现有字段的TITLE添加到关系对应的TO节点中
						relationMapToElement.get(rel).setAttribute("des",
								field.getTitle());
					}

				}
			}
		}

		// 测试生成XML文档代码
		XMLOutputter outputter = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		outputter.setFormat(format);

		try
		{
			String path = VdbManager.getEngine().getServletContext()
					.getRealPath("/WEB-INF/vdb/model/" + _dataSet.getUri());
			path = path + "/reports";
			File dir = new File(path);

			if (!dir.exists())
				dir.mkdirs();

			File f = new File(path + "/" + "cloud.xml");

			outputter.output(doc, new FileOutputStream(f));

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	public void setDataSet(DataSet set)
	{
		_dataSet = set;
		init();
	}

	public double getMinDistance()
	{
		return _minDistance;
	}

	public void setMinDistance(double distance)
	{
		_minDistance = distance;
	}

	public int getInnerCircleNum()
	{
		return _innerCircleNum;
	}

	public void setInnerCircleNum(int circleNum)
	{
		_innerCircleNum = circleNum;
	}

	public int getCircleNumIncrement()
	{
		return _circleNumIncrement;
	}

	public void setCircleNumIncrement(int numIncrement)
	{
		_circleNumIncrement = numIncrement;
	}

	public double getMinX()
	{
		return _minX;
	}

	public void setMinX(double minx)
	{
		_minX = minx;
		_x0 = (_maxX - _minX) / 2;
	}

	public double getMinY()
	{
		return _minY;
	}

	public void setMinY(double miny)
	{
		_minY = miny;
		_y0 = (_maxY - _minY) / 2;
	}

	public double getMaxX()
	{
		return _maxX;
	}

	public void setMaxX(double maxx)
	{
		_maxX = maxx;
		_x0 = (_maxX - _minX) / 2;
	}

	public double getMaxY()
	{
		return _maxY;
	}

	public void setMaxY(double maxy)
	{
		_maxY = maxy;
		_y0 = (_maxY - _minY) / 2;
	}
}
