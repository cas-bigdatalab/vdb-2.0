package vdb.mydb.jsp.action.editor;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.bean.BeanTreeVisitor;
import vdb.mydb.typelib.render.FieldRender;
import cn.csdb.commons.action.JspAction;

public class DeleteBeans implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String tid = request.getParameter("tid");
		Entity thisTable = (Entity) VdbManager.getInstance().getCatalog()
				.fromId(tid);

		// AuthenticatorContext ac = new AuthenticatorContext(
		// (HttpServletRequest) request, (HttpServletResponse) response,
		// servletContext);
		// // ��ȡ��ǰ�û�
		// Principal thisUser = ac.getUserPrincipal();
		// ac.assertPrivelege(thisTable, thisUser, "edit");
		String id = request.getParameter("id");

		final PrintWriter out = response.getWriter();

		out.println("<ul>");
		new AnyBeanDao(thisTable).visitRecords(id, new BeanTreeVisitor()
		{
			private Field _collectionField;

			public void afterBeanVisit(AnyBean dr)
			{
				try
				{
					out.println("</ul>");
				}
				catch (Exception e)
				{
				}
			}

			public void afterCollectionVisit(Entity entity,
					Field collectionField)
			{
				try
				{
					out.println("</ul>");
					out.println("</li>");
				}
				catch (Exception e)
				{
				}
			}

			public boolean beforeBeanVisit(AnyBean bean)
			{
				Entity table = bean.getEntity();

				try
				{
					if (_collectionField != null
							&& _collectionField.isWeakCollection())
					{
						out.println("<li><img src='/images/unlink.gif'>");
						out
								.println(new FieldRender(table.getTitleField(),
										bean).view2());
						out.println("</li>");
						out.println("<ul>");
					}
					else
					{
						out
								.println("<li><img src='/images/delete.gif'><span style='text-decoration: line-through'>");
						out.print(new FieldRender(table.getTitleField(), bean)
								.view2());
						out.println("</span></li>");
						out.println("<ul>");
					}
				}
				catch (Exception e)
				{
				}

				return true;
			}

			public boolean beforeCollectionVisit(Entity entity,
					Field collectionField)
			{
				_collectionField = collectionField;

				try
				{
					out.println("<li><span style='color:blue'>"
							+ entity.getTitle());
					out.println("</span><ul>");
				}
				catch (Exception e)
				{
				}

				return true;
			}
		});
		out.println("</ul>");
	}

}
