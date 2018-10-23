package vdb.mydb.rpc.service;

public interface JobExecutorService extends VdbEngineService
{
	public <T> T execute(RPCCallable<T> job) throws Exception;

	public void asyncExecute(RPCRunnable job);
}