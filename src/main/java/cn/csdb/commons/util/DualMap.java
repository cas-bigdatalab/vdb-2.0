/*
 * Created on 2005-2-16
 */
package cn.csdb.commons.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DualMap用以描述具有双向关联关系的Map。
 * <p>
 * 譬如：对于专家(E)和论文(A)之间的多对多关系可以描述在DualMap里面。
 * 用户可以通过调用line构建某个E(i)和A(i)的关联关系，然后通过DualMap的
 * getPeers(E(i))或者getPeers(A(i))即可获取指定对象的关联对象列表。
 * 
 * @author bluejoe
 */
public class DualMap
{
	private Map _map;

	public DualMap()
	{
		_map = new HashMap();
	}

	public void line(Object o1, Object o2)
	{
		Object[] o = { o1, o2 };

		for (int i = 0; i < 2; i++)
		{
			o1 = o[i];
			o2 = o[1 - i];

			Map lm = (Map) _map.get(o1);
			if (lm == null)
			{
				lm = new LinkedHashMap();
				_map.put(o1, lm);
			}

			lm.put(o2, new Object());
		}
	}

	public Map getPeers(Object o)
	{
		return (Map) _map.get(o);
	}
}
