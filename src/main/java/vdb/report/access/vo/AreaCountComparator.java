package vdb.report.access.vo;

import java.util.Comparator;

public class AreaCountComparator implements Comparator<AreaCountRecord>
{

	public int compare(AreaCountRecord acr1, AreaCountRecord acr2)
	{
		if (acr1.getCount() - acr2.getCount() > 0)
			return 1;
		else if (acr1.getCount() - acr2.getCount() < 0)
			return -1;
		else
			return 0;
	}

}
