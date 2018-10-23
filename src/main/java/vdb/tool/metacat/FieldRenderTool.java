package vdb.tool.metacat;

import java.io.StringWriter;

import org.apache.velocity.context.Context;

import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;

public class FieldRenderTool
{
	private Context _context;

	public String edit(Field field)
	{
		_context.put("field", field);
		String tot = field.getTypeName() + "/editor.html";
		return render(tot);
	}

	public String edit2(Field field, AnyBean bean) throws Exception
	{
		String s1 = edit(field);
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		ctx.put("field", field);
		ctx.put("bean", bean);
		return VdbManager.getInstance().getVelocityEngine().evaluate(s1, ctx);
	}

	public void init(Object obj)
	{
		_context = (Context) obj;
	}

	private String render(String tot)
	{
		StringWriter sw = new StringWriter();
		try
		{
			VdbManager.getEngine().getVelocityEngine().render(
					"/WEB-INF/typelib/" + tot, _context, sw);
		}

		catch (Exception e)
		{
			e.printStackTrace();
			return tot;
		}
		return sw.toString();
	}

	public String view(Field field)
	{
		_context.put("field", field);
		String tot = field.getTypeName() + "/viewer.html";
		return render(tot);
	}

	public String view2(Field field, AnyBean bean) throws Exception
	{
		String s1 = view(field);
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		ctx.put("field", field);
		ctx.put("bean", bean);
		return VdbManager.getInstance().getVelocityEngine().evaluate(s1, ctx);
	}
}
