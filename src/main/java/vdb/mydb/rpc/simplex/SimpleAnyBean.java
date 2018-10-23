package vdb.mydb.rpc.simplex;

import java.util.Map;

public interface SimpleAnyBean
{

	public Map<String, Object> getBean();

	public void setBean(Map<String, Object> bean);

	public String getDataSetUri();

	public void setDataSetUri(String dataSetUri);
}
