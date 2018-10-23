package vdb.report.resstat.dbstats.vo.comparator;

import java.util.Comparator;

import vdb.report.resstat.dbstats.vo.EntityIndicator;

/**
 * Entity记录数比较器，默认按照升序排序
 * 
 * @author 苏贤明
 * 
 */
public class EntityRecordNumComparator implements Comparator<EntityIndicator>
{

	public int compare(EntityIndicator ei1, EntityIndicator ei2)
	{

		if (ei1.getRecordNum() > ei2.getRecordNum())
			return 1;
		else if (ei1.getRecordNum() < ei2.getRecordNum())
			return -1;
		else
			return 0;
	}

}
