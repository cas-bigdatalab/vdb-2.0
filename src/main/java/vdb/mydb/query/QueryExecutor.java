package vdb.mydb.query;

import java.util.List;

import vdb.mydb.bean.AnyBean;
import cn.csdb.commons.jsp.Pageable;

public interface QueryExecutor extends Pageable<AnyBean>
{
	public List<AnyBean> list() throws Exception;

	public AnyBean single() throws Exception;
}
