package cn.csdb.commons.jsp;

import java.util.List;

public interface Pageable<T>
{
	public int size() throws Exception;

	public List<T> list(int beginning, int size) throws Exception;
}
