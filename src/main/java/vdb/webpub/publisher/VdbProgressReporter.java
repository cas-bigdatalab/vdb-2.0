package vdb.webpub.publisher;

public class VdbProgressReporter implements ProgressReporter
{
	String _prompt;

	long _max;

	long _pos;

	long _leftTime = -1;

	long _startPoint = 0;

	long _elapse;

	public long getPos()
	{
		return _pos;
	}

	public void setPos(long current)
	{
		_pos = current;
	}

	public void stepIt()
	{
		_pos++;
		long now = System.currentTimeMillis();
		_elapse = now - _startPoint;
		if (_startPoint != 0)
		{
			_leftTime = _elapse * (_max - _pos) / _pos;
		}
	}

	public String getPrompt()
	{
		return _prompt;
	}

	public void setPrompt(String prompt)
	{
		_prompt = prompt;
	}

	public long getMax()
	{
		return _max;
	}

	public void setMax(long max)
	{
		_max = max;

		_leftTime = -1;
		_startPoint = System.currentTimeMillis();
	}

	public void stepIt(String prompt)
	{
		stepIt();
		setPrompt(prompt);
	}

	public long getLeftTime()
	{
		return _leftTime;
	}

	public long getElapse()
	{
		return _elapse;
	}
}
