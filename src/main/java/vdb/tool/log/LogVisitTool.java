package vdb.tool.log;

import java.util.Date;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.dao.DataModificationDao;
import vdb.log.dao.UserLoginLogDao;
import vdb.log.vo.DataAccessLog;
import vdb.log.vo.DataModifications;
import vdb.log.vo.UserLoginLog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.tool.VdbTool;
import vdb.tool.metacat.UriTool;
import cn.csdb.commons.jsp.Pageable;

public class LogVisitTool extends VdbTool
{
	public Pageable<DataAccessLog> getDataVisitLogs(String itemUri)
	{
		UriTool ut = new UriTool();
		String entityUri = ut.getEntityUri(itemUri);
		Entity entity = VdbManager.getEngine().getCatalog().fromUri(entityUri);
		String beanId = ut.getId(itemUri);
		DataAccessLogDao dao = new DataAccessLogDao();
		return dao.getDataVisitationLogs(entity, beanId);
	}

	public Pageable<DataAccessLog> getDataModificationLogs(String itemUri)
	{
		UriTool ut = new UriTool();
		String entityUri = ut.getEntityUri(itemUri);
		Entity entity = VdbManager.getEngine().getCatalog().fromUri(entityUri);
		String beanId = ut.getId(itemUri);
		DataAccessLogDao dao = new DataAccessLogDao();
		return dao.getDataModificationLogs(entity, beanId);
	}

	public Pageable<DataAccessLog> getUserAccessLogs(DataSet dataset,
			String userName)
	{
		DataAccessLogDao dao = new DataAccessLogDao();
		return dao.getUserAccessLogsInDataset(dataset, userName);
	}

	public Pageable<UserLoginLog> getUserLoginLogs(String userName)
	{
		UserLoginLogDao dao = new UserLoginLogDao();
		return dao.getUserLoginLogs(userName);
	}

	public Pageable<DataModifications> getEntityModifications(Entity entity,
			Date startDate, Date endDate)
	{
		DataModificationDao dao = new DataModificationDao();
		return dao.getEntityModifications(entity, startDate, endDate);
	}

	public Date getDataSetLastModificationTime(DataSet dataset)
	{
		DataModificationDao dao = new DataModificationDao();
		return dao.getDataSetLastModificationTime(dataset.getUri());
	}

	public Date getEntityLastModificationTime(Entity entity)
	{
		DataModificationDao dao = new DataModificationDao();
		return dao.getEntityLastModificationTime(entity.getUri());
	}

	/**
	 * @author jack
	 * @param entityUri
	 * @return String
	 */
	public String getStrByEntityLastModificationTime(DataSet ds)
	{
		DataModificationDao dao = new DataModificationDao();
		return dao.getStrByEntityLastModificationTime(ds.getUri());
	}
}
