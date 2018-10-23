/*
 * Created on 2006-6-11
 */
package vdb.mydb.bean;

import vdb.metacat.Entity;
import vdb.metacat.Field;

public interface BeanTreeVisitor
{
	public void afterBeanVisit(AnyBean bean);

	public void afterCollectionVisit(Entity entity, Field collectionField);

	public boolean beforeBeanVisit(AnyBean bean);

	public boolean beforeCollectionVisit(Entity entity, Field collectionField);
}
