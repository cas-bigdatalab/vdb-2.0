package vdb.mydb.typelib.render;

import org.apache.velocity.context.Context;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.tool.metacat.FieldRenderTool;

public class FieldRender
{
	private AnyBean _bean;

	private Field _field;

	public FieldRender(Field field, AnyBean bean)
	{
		_field = field;
		_bean = bean;
	}

	public String view1() throws Exception
	{
		FieldRenderTool cv = new FieldRenderTool();
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		cv.init(ctx);
		return cv.view(_field);
	}

	public String view2() throws Exception
	{
		FieldRenderTool cv = new FieldRenderTool();
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		cv.init(ctx);
		return cv.view2(_field, _bean);
	}
}
