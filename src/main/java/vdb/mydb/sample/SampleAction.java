package vdb.mydb.sample;

import java.util.Date;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;

public class SampleAction implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		vc.put("now", new Date());
	}
}
