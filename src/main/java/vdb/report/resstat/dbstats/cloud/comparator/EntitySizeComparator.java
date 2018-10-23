package vdb.report.resstat.dbstats.cloud.comparator;

import java.util.Comparator;

import vdb.report.resstat.dbstats.cloud.EntityCircle;


/**
 * EntityCircle的数据大小比较器，默认按照升序排序
 * 
 * @author Administrator
 * 
 */
public class EntitySizeComparator implements Comparator<EntityCircle>
{

	public int compare(EntityCircle ec1, EntityCircle ec2)
	{

		if (ec1.getDataSize() > ec2.getDataSize())
			return 1;
		else if (ec1.getDataSize() < ec2.getDataSize())
			return -1;
		else
			return 0;
	}

}
