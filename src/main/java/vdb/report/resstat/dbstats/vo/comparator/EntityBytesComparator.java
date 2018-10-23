package vdb.report.resstat.dbstats.vo.comparator;

import java.util.Comparator;

import vdb.report.resstat.dbstats.vo.EntityIndicator;

/**
 * Entity数据量比较器，默认按照升序排序
 * 
 * @author 苏贤明
 * 
 */
public class EntityBytesComparator implements Comparator<EntityIndicator>
{

	public int compare(EntityIndicator ei1, EntityIndicator ei2)
	{

		if (ei1.getBytes() > ei2.getBytes())
			return 1;
		else if (ei1.getBytes() < ei2.getBytes())
			return -1;
		else
			return 0;
	}

}
