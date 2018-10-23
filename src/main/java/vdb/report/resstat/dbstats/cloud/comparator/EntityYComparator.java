package vdb.report.resstat.dbstats.cloud.comparator;

import java.util.Comparator;

import vdb.report.resstat.dbstats.cloud.EntityCircle;


public class EntityYComparator implements Comparator<EntityCircle>
{

	public int compare(EntityCircle ec1, EntityCircle ec2)
	{
		if (ec1.getY() - ec2.getY() > 0)
			return 1;
		else if (ec1.getY() - ec2.getY() < 0)
			return -1;
		else
			return 0;
	}

}
