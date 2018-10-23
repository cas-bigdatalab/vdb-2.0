package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.metacat.Field;
import vdb.metacat.Relation;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDeleteRelation extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		// 获取关系对象
		Relation rf = (Relation) VdbManager.getEngine().getCatalog()
				.fromId(mid);
		String tempId = "";
		if(rf.isManyToMany()){
			tempId = "Relation@m2m@"+rf.getId();
		}
		else{
			tempId = "Relation@m2o@"+rf.getId();
		}
		request.setAttribute("tempId", tempId);
		request.setAttribute("dsid", rf.getDataSet().getId());
		// zhouxun 20100418
		DataSet ds = rf.getDataSet();
		ds.removeRelation(rf);

		VdbEntity eA = (VdbEntity) rf.getKeyA().getTarget();
		Field[] eaFields = eA.getFields();
		for (int i = 0; i < eaFields.length; i++)
		{
			if ((eaFields[i].isReference() || eaFields[i].isCollection())
			// 与rf中的值作比较
					&&
					// 要删除的A中关联field与关系中的B对象一致
					((eaFields[i].getRelationKey().getId()).equals(rf.getKeyB()
							.getId())))
			{
				eA.removeField(eaFields[i]);
			}
		}
		eA.removeReferenceKey(rf.getKeyB());
		eA.removeCollectionKey(rf.getKeyB());

		VdbEntity eB = (VdbEntity) rf.getKeyB().getTarget();
		Field[] ebFields = eB.getFields();
		for (int i = 0; i < ebFields.length; i++)
		{
			if ((ebFields[i].isReference() || ebFields[i].isCollection())
			// 与rf中的值作比较
					&&
					// 要删除的B中关联field与关系中的A对象一致
					((ebFields[i].getRelationKey().getId()).equals(rf.getKeyA()
							.getId())))
			{
				eB.removeField(ebFields[i]);
			}
		}
		eB.removeReferenceKey(rf.getKeyA());
		eB.removeCollectionKey(rf.getKeyA());

		VdbManager.getEngine().getCatalogManager().saveDataSet(rf.getDataSet());
		VdbManager.getEngine().getCatalog().cacheIn(ds);
	}
}
