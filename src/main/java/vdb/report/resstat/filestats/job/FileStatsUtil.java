package vdb.report.resstat.filestats.job;

import java.util.concurrent.Executors;

import vdb.metacat.DataSet;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;

public class FileStatsUtil {
	
	public void doFileStats(DataSet ds, FileRepository fr,RepositoryIndicator ri) {
		if (fr != null) {
			doFileStats(fr.getRoot(),ri);
			// 关闭线程池
			FileStatsThread.executor.shutdown();
		}
	}

	private void doFileStats(RepositoryFile file,RepositoryIndicator ri) {

		//判断连接池是否被关闭
		if(FileStatsThread.executor.isShutdown()){
			FileStatsThread.executor = Executors.newFixedThreadPool(5);
		}
		
		Thread t = new FileStatsThread(file,ri,0);
		FileStatsThread.executor.execute(t);
		
		synchronized (FileStatsThread.lock) {
			try{
				FileStatsThread.lock.wait();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
