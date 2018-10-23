package vdb.tool.webpub;

import java.net.URL;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssTool
{
	public SyndFeed getFeed(String url) throws Exception
	{
		try
		{
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			return feed;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
