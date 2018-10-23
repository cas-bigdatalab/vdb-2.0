package vdb.litesync.summary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SummaryReader
{
	private InputStream _is;

	public SummaryReader(File file) throws FileNotFoundException
	{
		_is = new FileInputStream(file);
	}

	public SummaryReader(InputStream is)
	{
		_is = is;
	}

	public Summary read() throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(_is));
		Summary summary = new Summary();

		while (true)
		{
			String line = reader.readLine();
			if (line == null)
				break;

			if (line.startsWith("#"))
			{
				continue;
			}

			StringTokenizer st = new StringTokenizer(line, "\t");
			String path = st.nextToken();
			SummaryEntry entry = new SummaryEntry();
			entry.setPath(path);
			entry.setKind(EntryKind.forName(st.nextToken()));
			entry.setLength(Long.parseLong(st.nextToken()));
			entry.setDigest(st.nextToken());

			summary.addEntry(entry);
		}

		reader.close();
		return summary;
	}
}
