package vdb.report.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import vdb.mydb.VdbManager;

//FIXME: IpSeeker
@SuppressWarnings("unchecked")
public class IPSeeker
{
	// FIXME: IpLocation
	private class IPLocation
	{
		public String area;

		public String country;

		public IPLocation()
		{
			country = area = "";
		}

		public IPLocation getCopy()
		{
			IPLocation ret = new IPLocation();
			ret.country = country;
			ret.area = area;
			return ret;
		}
	}

	private static final byte AREA_FOLLOWED = 0x01;

	// FIXME: configure IpSeeker in spring application context, and set file
	// path via implements ServletContextAware
	private static final String IP_FILE_LOCATION = VdbManager.getEngine()
			.getRealPath("/WEB-INF/QQWry.dat");

	private static IPSeeker instance = new IPSeeker();

	private static final int IP_RECORD_LENGTH = 7;

	private static final byte NO_AREA = 0x2;

	/**
	 * @return 单一实例
	 */
	public static IPSeeker getInstance()
	{
		return instance;
	}

	private byte[] b3;

	private byte[] b4;

	private byte[] buf;

	private long ipBegin, ipEnd;

	private Hashtable ipCache;

	private RandomAccessFile ipFile;

	private IPLocation loc;

	private MappedByteBuffer mbb;

	private IPSeeker()
	{
		ipCache = new Hashtable();
		loc = new IPLocation();
		buf = new byte[100];
		b4 = new byte[4];
		b3 = new byte[3];
		try
		{
			ipFile = new RandomAccessFile(IP_FILE_LOCATION, "r");
		}
		catch (FileNotFoundException e)
		{
			ipFile = null;

		}
		// 如果打开文件成功，读取文件头信息
		if (ipFile != null)
		{
			try
			{
				ipBegin = readLong4(0);
				ipEnd = readLong4(4);
				if (ipBegin == -1 || ipEnd == -1)
				{
					ipFile.close();
					ipFile = null;
				}
			}
			catch (IOException e)
			{
				ipFile = null;
			}
		}
	}

	private int compareByte(byte b1, byte b2)
	{
		if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
			return 1;
		else if ((b1 ^ b2) == 0)// 判断是否相等
			return 0;
		else
			return -1;
	}

	private int compareIP(byte[] ip, byte[] beginIp)
	{
		for (int i = 0; i < 4; i++)
		{
			int r = compareByte(ip[i], beginIp[i]);
			if (r != 0)
				return r;
		}
		return 0;
	}

	public String getAddress(String ip)
	{
		String country = getCountry(ip).equals(" CZ88.NET") ? ""
				: getCountry(ip);
		String area = getArea(ip).equals(" CZ88.NET") ? "" : getArea(ip);
		String address = country + " " + area;
		return address.trim();
	}

	public String getArea(byte[] ip)
	{
		if (ipFile == null)
			return "错误的IP数据库文件";
		String ipStr = IpUtil.getIpStringFromBytes(ip);
		if (ipCache.containsKey(ipStr))
		{
			IPLocation loc = (IPLocation) ipCache.get(ipStr);
			return loc.area;
		}
		else
		{
			IPLocation loc = getIPLocation(ip);
			ipCache.put(ipStr, loc.getCopy());
			return loc.area;
		}
	}

	public String getArea(String ip)
	{
		return getArea(IpUtil.getIpByteArrayFromString(ip));
	}

	public String getCountry(byte[] ip)
	{
		if (ipFile == null)
			return "错误的IP数据库文件";
		String ipStr = IpUtil.getIpStringFromBytes(ip);
		if (ipCache.containsKey(ipStr))
		{
			IPLocation loc = (IPLocation) ipCache.get(ipStr);
			return loc.country;
		}
		else
		{
			IPLocation loc = getIPLocation(ip);
			ipCache.put(ipStr, loc.getCopy());
			return loc.country;
		}
	}

	public String getCountry(String ip)
	{
		return getCountry(IpUtil.getIpByteArrayFromString(ip));
	}

	public List getIPEntries(String s)
	{
		List ret = new ArrayList();
		try
		{
			// 映射IP信息文件到内存中
			if (mbb == null)
			{
				FileChannel fc = ipFile.getChannel();
				mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
				mbb.order(ByteOrder.LITTLE_ENDIAN);
			}

			int endOffset = (int) ipEnd;
			for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH)
			{
				int temp = readInt3(offset);
				if (temp != -1)
				{
					IPLocation loc = getIPLocation(temp);
					if (loc.country.indexOf(s) != -1
							|| loc.area.indexOf(s) != -1)
					{
						IPEntry entry = new IPEntry();
						entry.setCountry(loc.country);
						entry.setArea(loc.area);
						readIP(offset - 4, b4);
						entry.setBeginIp(IpUtil.getIpStringFromBytes(b4));
						readIP(temp, b4);
						entry.setEndIp(IpUtil.getIpStringFromBytes(b4));
						ret.add(entry);
					}
				}
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		return ret;
	}

	public List getIPEntriesDebug(String s)
	{
		List ret = new ArrayList();
		long endOffset = ipEnd + 4;
		for (long offset = ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH)
		{
			// 读取结束IP偏移
			long temp = readLong3(offset);
			if (temp != -1)
			{
				IPLocation loc = getIPLocation(temp);
				if (loc.country.indexOf(s) != -1 || loc.area.indexOf(s) != -1)
				{
					IPEntry entry = new IPEntry();
					entry.setCountry(loc.country);
					entry.setArea(loc.area);
					readIP(offset - 4, b4);
					entry.setBeginIp(IpUtil.getIpStringFromBytes(b4));
					readIP(temp, b4);
					entry.setEndIp(IpUtil.getIpStringFromBytes(b4));
					ret.add(entry);
				}
			}
		}
		return ret;
	}

	private IPLocation getIPLocation(byte[] ip)
	{
		IPLocation info = null;
		long offset = locateIP(ip);
		if (offset != -1)
			info = getIPLocation(offset);
		if (info == null)
		{
			info = new IPLocation();
			info.country = "未知国家";
			info.area = "未知地区";
		}
		return info;
	}

	/**
	 * @param offset
	 * @return
	 */
	private IPLocation getIPLocation(int offset)
	{
		// 跳过4字节ip
		mbb.position(offset + 4);
		byte b = mbb.get();
		if (b == AREA_FOLLOWED)
		{
			// 读取国家偏移
			int countryOffset = readInt3();
			// 跳转至偏移处
			mbb.position(countryOffset);
			b = mbb.get();
			if (b == NO_AREA)
			{
				loc.country = readString(readInt3());
				mbb.position(countryOffset + 4);
			}
			else
				loc.country = readString(countryOffset);
			// 读取地区标志
			loc.area = readArea(mbb.position());
		}
		else if (b == NO_AREA)
		{
			loc.country = readString(readInt3());
			loc.area = readArea(offset + 8);
		}
		else
		{
			loc.country = readString(mbb.position() - 1);
			loc.area = readArea(mbb.position());
		}
		return loc;
	}

	private IPLocation getIPLocation(long offset)
	{
		try
		{
			// 跳过4字节ip
			ipFile.seek(offset + 4);
			byte b = ipFile.readByte();
			if (b == AREA_FOLLOWED)
			{
				// 读取国家偏移
				long countryOffset = readLong3();
				// 跳转至偏移处
				ipFile.seek(countryOffset);
				b = ipFile.readByte();
				if (b == NO_AREA)
				{
					loc.country = readString(readLong3());
					ipFile.seek(countryOffset + 4);
				}
				else
					loc.country = readString(countryOffset);
				// 读取地区标志
				loc.area = readArea(ipFile.getFilePointer());
			}
			else if (b == NO_AREA)
			{
				loc.country = readString(readLong3());
				loc.area = readArea(offset + 8);
			}
			else
			{
				loc.country = readString(ipFile.getFilePointer() - 1);
				loc.area = readArea(ipFile.getFilePointer());
			}
			return loc;
		}
		catch (IOException e)
		{
			return null;
		}
	}

	private long getMiddleOffset(long begin, long end)
	{
		long records = (end - begin) / IP_RECORD_LENGTH;
		records >>= 1;
		if (records == 0)
			records = 1;
		return begin + records * IP_RECORD_LENGTH;
	}

	private long locateIP(byte[] ip)
	{
		long m = 0;
		int r;
		readIP(ipBegin, b4);
		r = compareIP(ip, b4);
		if (r == 0)
			return ipBegin;
		else if (r < 0)
			return -1;
		for (long i = ipBegin, j = ipEnd; i < j;)
		{
			m = getMiddleOffset(i, j);
			readIP(m, b4);
			r = compareIP(ip, b4);
			// log.debug(IpUtil.getIpStringFromBytes(b));
			if (r > 0)
				i = m;
			else if (r < 0)
			{
				if (m == j)
				{
					j -= IP_RECORD_LENGTH;
					m = j;
				}
				else
					j = m;
			}
			else
				return readLong3(m + 4);
		}
		m = readLong3(m + 4);
		readIP(m, b4);
		r = compareIP(ip, b4);
		if (r <= 0)
			return m;
		else
			return -1;
	}

	/**
	 * @param offset
	 * @return
	 */
	private String readArea(int offset)
	{
		mbb.position(offset);
		byte b = mbb.get();
		if (b == 0x01 || b == 0x02)
		{
			int areaOffset = readInt3();
			if (areaOffset == 0)
				return "未知地区";
			else
				return readString(areaOffset);
		}
		else
			return readString(offset);
	}

	private String readArea(long offset) throws IOException
	{
		ipFile.seek(offset);
		byte b = ipFile.readByte();
		if (b == 0x01 || b == 0x02)
		{
			long areaOffset = readLong3(offset + 1);
			if (areaOffset == 0)
				return "未知地区";
			else
				return readString(areaOffset);
		}
		else
			return readString(offset);
	}

	private int readInt3()
	{
		return mbb.getInt() & 0x00FFFFFF;
	}

	private int readInt3(int offset)
	{
		mbb.position(offset);
		return mbb.getInt() & 0x00FFFFFF;
	}

	private void readIP(int offset, byte[] ip)
	{
		mbb.position(offset);
		mbb.get(ip);
		byte temp = ip[0];
		ip[0] = ip[3];
		ip[3] = temp;
		temp = ip[1];
		ip[1] = ip[2];
		ip[2] = temp;
	}

	private void readIP(long offset, byte[] ip)
	{
		try
		{
			ipFile.seek(offset);
			ipFile.readFully(ip);
			byte temp = ip[0];
			ip[0] = ip[3];
			ip[3] = temp;
			temp = ip[1];
			ip[1] = ip[2];
			ip[2] = temp;
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private long readLong3()
	{
		long ret = 0;
		try
		{
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		}
		catch (IOException e)
		{
			return -1;
		}
	}

	private long readLong3(long offset)
	{
		long ret = 0;
		try
		{
			ipFile.seek(offset);
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		}
		catch (IOException e)
		{
			return -1;
		}
	}

	private long readLong4(long offset)
	{
		long ret = 0;
		try
		{
			ipFile.seek(offset);
			ret |= (ipFile.readByte() & 0xFF);
			ret |= ((ipFile.readByte() << 8) & 0xFF00);
			ret |= ((ipFile.readByte() << 16) & 0xFF0000);
			ret |= ((ipFile.readByte() << 24) & 0xFF000000);
			return ret;
		}
		catch (IOException e)
		{
			return -1;
		}
	}

	private String readString(int offset)
	{
		try
		{
			mbb.position(offset);
			int i;
			for (i = 0, buf[i] = mbb.get(); buf[i] != 0; buf[++i] = mbb.get())
				;
			if (i != 0)
				return IpUtil.getString(buf, 0, i, "GBK");
		}
		catch (IllegalArgumentException e)
		{
			System.out.println(e.getMessage());
		}
		return "";
	}

	private String readString(long offset)
	{
		try
		{
			ipFile.seek(offset);
			int i;
			for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile
					.readByte())
				;
			if (i != 0)
				return IpUtil.getString(buf, 0, i, "GBK");
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		return "";
	}
}