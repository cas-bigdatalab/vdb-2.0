package vdb.mydb.rpc.service;

import java.io.Serializable;

import vdb.mydb.rpc.simplex.SimpleAnyBean;

public interface BeanFetchService extends VdbEngineService
{
	SimpleAnyBean getBean(String entityUri, Serializable id) throws Exception;
}
