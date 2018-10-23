import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import vdb.mydb.VdbManager;
import vdb.mydb.rpc.VdbEngineServerConnector;
import vdb.mydb.rpc.service.FileFetchService;
import vdb.mydb.rpc.service.JobExecutorService;
import vdb.mydb.rpc.service.RPCCallable;
import vdb.mydb.rpc.service.RPCRunnable;
import vdb.mydb.rpc.simplex.SimpleFile;

class MyCall implements RPCCallable<String>, RPCRunnable
{
	public String call() throws Exception
	{
		System.out.print("call()");
		return VdbManager.getEngine().getDomain().getTitle();
	}

	public void run()
	{
		System.out.print("wokao!!!!");
	}
}

public class JettyTest
{
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		String urlPattern = "http://localhost:4444/vdbrpc/%s";
		VdbEngineServerConnector conn = new VdbEngineServerConnector();
		conn.setUrlPattern(urlPattern);
		FileFetchService service = conn.getService(FileFetchService.class);
		SimpleFile file = service.getFile("cms", "upload",
				"/2010-05/1f628d0728aec8130128aecbc674001a");
		System.out.println(file);
		InputStream is = service.openStream("cms", "upload",
				"/2010-05/1f628d0728aec8130128aecbc674001a");

		FileOutputStream os = new FileOutputStream(new File("./out"));
		while (true)
		{
			int read = is.read();
			if(read < 0)
				break;
			
			os.write(read);
		}

		is.close();
		os.close();
		
		JobExecutorService jes = conn.getService(JobExecutorService.class);
		String s = jes.execute(new MyCall());

		System.out.println(s);
		jes.asyncExecute(new MyCall());
	}
}
