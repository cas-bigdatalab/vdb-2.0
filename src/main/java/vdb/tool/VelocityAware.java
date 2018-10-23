package vdb.tool;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

public interface VelocityAware
{
	public void setVelocityContext(Context context);

	public void setVelocityEngine(VelocityEngine engine);
}