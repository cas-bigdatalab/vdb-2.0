package vdb.mydb.vtl;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.view.JeeConfig;
import org.apache.velocity.tools.view.VelocityView;

public class VdbVelocityView extends VelocityView
{
	static final String BEFORE_VELOCITY_INIT = "BEFORE_VELOCITY_INIT";

	public VdbVelocityView(JeeConfig arg0)
	{
		super(arg0);
	}

	@Override
	protected void configure(JeeConfig arg0, VelocityEngine arg1)
	{
		super.configure(arg0, arg1);
		BeforeVelocityInit beforeVelocityInit = (BeforeVelocityInit) arg0
				.getServletContext().getAttribute(BEFORE_VELOCITY_INIT);
		if (beforeVelocityInit != null)
			beforeVelocityInit.initVelocity(arg1);
	}
}
