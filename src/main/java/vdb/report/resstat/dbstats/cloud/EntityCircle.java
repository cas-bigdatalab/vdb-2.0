package vdb.report.resstat.dbstats.cloud;

import vdb.metacat.Entity;

public class EntityCircle
{

	/** 实体圆X坐标 */
	private double _x;

	/** 实体圆Y坐标 */
	private double _y;

	/** 实体圓R半径 */
	private double _r;

	/** 数据实体的数据量 */
	private double _dataSize;

	/** 与此实体关联的数据实体的个数 */
	private int _asscociateNum;

	private Entity _entity;

	public EntityCircle(Entity entity)
	{
		_entity = entity;
	}

	public double getX()
	{
		return _x;
	}

	public void setX(double x)
	{
		_x = x;
	}

	public double getY()
	{
		return _y;
	}

	public void setY(double y)
	{
		_y = y;
	}

	public double getR()
	{
		return _r;
	}

	public void setR(double r)
	{
		_r = r;
	}

	public double getDataSize()
	{
		return _dataSize;
	}

	public void setDataSize(double size)
	{
		_dataSize = size;
	}

	public int getAsscociateNum()
	{
		return _asscociateNum;
	}

	public void setAsscociateNum(int num)
	{
		_asscociateNum = num;
	}

	public Entity getEntity()
	{
		return _entity;
	}

	public void setEntity(Entity entity)
	{
		_entity = entity;
	}

	public boolean equals(Object o)
	{
		if (o != null)
		{
			EntityCircle ec = (EntityCircle) o;
			if (ec != null
					&& ec.getEntity().getTableName().equals(
							this.getEntity().getTableName()))
				return true;
			else
				return false;
		}
		return false;
	}
}
