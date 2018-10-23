package vdb.tool.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.vo.DataAccessLog;
import vdb.metacat.DataSet;

public class DataAccessLogProviderTool {
	
	public List<DataAccessLog> getDataAccessLogByDateAndDS(DataSet ds,String dateString) throws Exception{
		DataAccessLogDao dao = new DataAccessLogDao();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(dateString);
		
		return dao.getDataAccessLogByDateAndDS(ds, date);
	}
}
