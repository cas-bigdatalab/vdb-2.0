package vdb.mydb.rpc.service;

public class JobExecutorServiceImpl implements JobExecutorService
{
	public <T> T execute(RPCCallable<T> job) throws Exception
	{
		return job.call();
	}

	public void asyncExecute(RPCRunnable job)
	{
		new Thread(job).start();
	}
}
