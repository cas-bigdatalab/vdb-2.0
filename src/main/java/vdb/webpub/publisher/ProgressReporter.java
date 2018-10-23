package vdb.webpub.publisher;

public interface ProgressReporter
{
	public long getPos();

	public void setPos(long current);

	public void stepIt();

	public String getPrompt();

	public void setPrompt(String prompt);

	public long getMax();

	public void setMax(long max);

	public void stepIt(String prompt);

	public long getLeftTime();

	public long getElapse();
}
