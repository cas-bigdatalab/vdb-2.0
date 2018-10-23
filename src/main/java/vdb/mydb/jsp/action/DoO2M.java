/*
 * Created on 2006-8-28
 */
package vdb.mydb.jsp.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSONObject;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBeanDao;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoO2M implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// String thisEntityUri = request.getParameter("thisTableUri");
		String thisFieldUri = request.getParameter("thisFieldUri");
		// String thisId = request.getParameter("thisId");
		String thatId = request.getParameter("thatId");
		String mode = request.getParameter("mode");

		try
		{
			// �ӱ�collection��
			Field thisField = (Field) VdbManager.getInstance().getCatalog()
					.fromUri(thisFieldUri);
			// ���
			Entity thatEntity = thisField.getRelationKey().getTarget();
			String IdColName = thatEntity.getIdentifier().getField()
					.getColumnName();

			// Ȩ��
			// AuthenticatorContext ac = new AuthenticatorContext(
			// (HttpServletRequest) request,
			// (HttpServletResponse) response, servletContext);
			// Principal thisUser = ac.getUserPrincipal();
			// ac.assertPrivelege(thatEntity, thisUser, "edit");

			// ���DAO
			AnyBeanDao dao = new AnyBeanDao(thatEntity);

			// �����
			String thatTableName = thisField.getRelationKey().getTarget()
					.getTableName();
			// ��������
			/*
			 * String thatColName =
			 * thisField.getRelationKey().getPeer().getColumnName();
			 */

			String s = "";
			StringSql whereFilter = null;
			// if("link".equals(mode)){
			// t��
			// s = "update " + thatTableName
			// + " set " + thatColName + " = " + thisId
			// + " where ID = " + thatId;
			// }

			if ("unlink".equals(mode))
			{
				// ȥ��t��
				s = " " + IdColName + "  = ? ";
				whereFilter = new StringSql(s, thatId);
			}
			else
			{
				// "unlinkAll".equals(mode)ȥ������t��
				if (thatId == null || "".equals(thatId))
				{
					s = " 0=1 ";// ִ����Чɾ����䣬����ɾ���κμ�¼
					whereFilter = new StringSql(s);
				}
				else
				{
					Serializable[] ids = thatId.split(";");
					List<Serializable> params = Arrays.asList(ids);

					for (int k = 0; k < ids.length; k++)
					{
						if (k == 0)
						{
							s = " " + IdColName + " = ? ";
						}
						else
						{
							s += " or " + IdColName + "  = ? ";
						}
					}
					whereFilter = new StringSql(s);
					whereFilter.addParameters(params);
				}
			}

			// ִ��sql
			// System.err.println(s);

			dao.getJdbcSource().deleteRecords(thatTableName, whereFilter);

			// ����
			JSONObject ok = new JSONObject();
			ok.put("code", 200);
			ok.write(response.getWriter());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
