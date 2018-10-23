package vdb.mydb.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.context.Context;

import vdb.mydb.VdbManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class ScriptExecutor
{
	private JdbcSource _jdbcSource;

	private BufferedReader _reader;

	public ScriptExecutor(JdbcSource jdbcSource, File file) throws Exception
	{
		this(jdbcSource, new FileReader(file));
	}

	public ScriptExecutor(JdbcSource jdbcSource, Reader reader)
			throws Exception
	{
		_jdbcSource = jdbcSource;
		_reader = new BufferedReader(reader);
		_reader.mark(0);
	}

	public ScriptExecutor(JdbcSource jdbcSource, String script)
			throws Exception
	{
		this(jdbcSource, new StringReader(script));
	}

	public void execute() throws Exception
	{
		execute(null);
	}

	public void execute(Context context) throws Exception
	{
		_reader.reset();
		while (true)
		{
			String line = _reader.readLine();
			if (line == null)
				break;
			if (line.startsWith("#"))
				continue;

			try
			{
				_jdbcSource.executeUpdate(new StringSql(context == null ? line
						: VdbManager.getInstance().getVelocityEngine()
								.evaluate(line, context)));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
