package vdb.mydb.vtl.action;

import java.text.MessageFormat;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.tool.ui.UrlsTool;

public abstract class EntityAction implements VspAction
{
	public Entity getEntity(VspContext vc) throws Exception
	{
		String uri = vc.getRequestURI();
		String turi = UrlsTool.getUriFromFileName(uri);

		// û��ָ����ID
		if (turi == null)
		{
			throw new Exception(MessageFormat.format(
					"bad request, entity uri is null", new Object[] {}));
		}

		Entity table = (Entity) VdbManager.getInstance().getCatalog().fromUri(
				turi);

		// û���ҵ���
		if (table == null)
		{
			throw new Exception(MessageFormat.format(
					"failed to find entity with uri: `{0}`",
					new Object[] { turi }));
		}

		vc.put("entity", table);
		return table;
	}
}
