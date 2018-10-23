package vdb.report.resstat.dbstats.cloud.comparator;

import java.util.Comparator;

import vdb.report.resstat.dbstats.cloud.EntityCircle;


/**
 * EntityCircle的关联实体个数比较器，默认按照升序排序
 * 
 * @author Administrator
 * 
 */
public class EntityAsscosiateNumComparator implements Comparator<EntityCircle>
{

	public int compare(EntityCircle ec1, EntityCircle ec2)
	{
		return ec1.getAsscociateNum() - ec2.getAsscociateNum();
	}

}
